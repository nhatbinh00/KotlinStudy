package com.example.kotlinknowledge.data.remote.interceptor

import com.example.kotlinknowledge.app.constant.AppConst
import com.example.kotlinknowledge.app.constant.AppKey
import com.example.kotlinknowledge.ulti.SharedPrefs
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = SharedPrefs.get(AppKey.token, AppConst.DEFAULT_VALUE)

        val requestBuilder = originalRequest.newBuilder()
            .addHeader(AppConst.CONTENT_TYPE, AppConst.APPLICATION_JSON)
            .addHeader(AppConst.AUTHORIZATION, AppConst.BEARER + "$token")

        val response = chain.proceed(requestBuilder.build())
        if (response.code == 401) {
            val newTokens = runBlocking { refreshToken() }
            if (newTokens != null) {
                SharedPrefs.put(AppKey.token, newTokens.first)
                SharedPrefs.put(AppKey.refreshToken, newTokens.second)
                val newRequest = originalRequest.newBuilder()
                    .addHeader(AppConst.AUTHORIZATION, AppConst.BEARER + "${newTokens.first}")
                    .build()
                return chain.proceed(newRequest)
            }
        }
        return response
    }

    private fun refreshToken(): Pair<String, String>? {
        val client = OkHttpClient()
        val refreshToken = SharedPrefs.get(AppKey.refreshToken, AppConst.DEFAULT_VALUE)
        val requestBody = AppConst.REFRESH_TOKEN_BODY_TEMPLATE.format(refreshToken)
            .toRequestBody(AppConst.APPLICATION_JSON.toMediaType())

        val request = Request.Builder()
            .url(AppConst.BASE_URL + AppConst.REFRESH_TOKEN_URL)
            .post(requestBody)
            .addHeader(AppConst.CONTENT_TYPE, AppConst.APPLICATION_JSON)
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                val jsonObject = JSONObject(json)
                val newToken = jsonObject.getString(AppKey.token)
                val newRefreshToken = jsonObject.getString(AppKey.refreshToken)
                Pair(newToken, newRefreshToken)
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
