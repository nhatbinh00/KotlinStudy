package com.example.kotlinknowledge.app.di

import com.example.kotlinknowledge.data.remote.api.ProductServices
import com.example.kotlinknowledge.data.remote.mapper.RemoteErrorMapper
import com.example.kotlinknowledge.data.remote.mapper.RemoteErrorMapperImpl
import com.example.kotlinknowledge.data.repositories.AuthenticationRepositoryImpl
import com.example.kotlinknowledge.data.repositories.ProductRepositoryImpl
import com.example.kotlinknowledge.domain.repositories.AuthenticationRepository
import com.example.kotlinknowledge.domain.repositories.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused", "TooManyFunctions")
@Module
@InstallIn(SingletonComponent::class)
internal interface DataInterfaceRemoteModule {
    @Binds
    @Singleton
    fun remoteErrorMapper(implRemote: RemoteErrorMapperImpl): RemoteErrorMapper

    @Binds
    @Singleton
    fun provideAuthenticationRepository(impl: AuthenticationRepositoryImpl): AuthenticationRepository

    @Binds
    @Singleton
    fun provideProductRepository(impl: ProductRepositoryImpl): ProductRepository

}
