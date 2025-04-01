package com.example.kotlinknowledge.app.di

import com.example.kotlinknowledge.app.constant.AppConst
import com.example.kotlinknowledge.app.constant.AppKey
import com.example.kotlinknowledge.data.remote.api.AuthenticationServices
import com.example.kotlinknowledge.data.remote.api.ProductServices
import com.example.kotlinknowledge.data.remote.responses.ErrorResponse
import com.example.kotlinknowledge.ulti.SharedPrefs
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private fun getClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
        val authInterceptor = AuthInterceptor()

        return OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
                .addInterceptor(authInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
        }.build()
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AppConst.BASE_URL)
            .client(getClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideProductServices(): ProductServices {
        return getRetrofit().create(ProductServices::class.java)
    }

    @Provides
    fun provideAuthenticationServices(): AuthenticationServices {
        return getRetrofit().create(AuthenticationServices::class.java)
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Provides
    fun errorResponseJsonAdapter(moshi: Moshi): JsonAdapter<ErrorResponse> = moshi.adapter()

    @Provides
    @Singleton
    fun errorResponseJsonMapAdapter(moshi: Moshi): JsonAdapter<Map<String, Any?>> = moshi.adapter(
        Types.newParameterizedType(
            Map::class.java,
            String::class.java,
            Any::class.java,
        ),
    )
}
