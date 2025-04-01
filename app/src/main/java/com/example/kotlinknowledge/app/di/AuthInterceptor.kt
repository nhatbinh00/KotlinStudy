package com.example.kotlinknowledge.app.di

import com.example.kotlinknowledge.app.constant.AppKey
import com.example.kotlinknowledge.ulti.SharedPrefs
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = SharedPrefs.get(AppKey.token, "")

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer $token")

        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            synchronized(this) {
                val newToken = refreshToken()
                if (!newToken.isNullOrEmpty()) {
                    SharedPrefs.put(AppKey.token, newToken)
                    val newRequest = originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer $newToken")
                        .build()
                    return chain.proceed(newRequest)
                }
            }
        }
        return response
    }

    private fun refreshToken(): String? {
        val client = OkHttpClient()
        val refreshToken = SharedPrefs.get(AppKey.refreshToken, "")
        val requestBody = """{
            "refreshToken": "$refreshToken",
            "expiresInMins": 30
        }""".toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url("https://dummyjson.com/auth/refresh")
            .post(requestBody)
            .addHeader("Content-Type", "application/json")
            .build()

        return try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val json = response.body?.string()
                JSONObject(json).getString("token")
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
