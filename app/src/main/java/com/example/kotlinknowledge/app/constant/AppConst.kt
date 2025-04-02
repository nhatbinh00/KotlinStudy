package com.example.kotlinknowledge.app.constant


object AppConst {
    const val BASE_URL = "https://dummyjson.com/"
    const val HEIGHT_IMG: Int = 1080
    const val WIDTH_IMG: Int = 1920
    const val DEFAULT_VALUE = ""
    const val BEARER = "Bearer "
    const val CONTENT_TYPE = "Content-Type"
    const val AUTHORIZATION = "Authorization"
    const val REFRESH_TOKEN_URL = "auth/refresh"
    const val APPLICATION_JSON = "application/json"
    const val REFRESH_TOKEN_BODY_TEMPLATE = """{
        "refreshToken": "%s",
        "expiresInMins": 30
    }"""
}