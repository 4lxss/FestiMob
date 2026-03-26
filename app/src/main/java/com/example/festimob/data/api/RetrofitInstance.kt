package com.example.festimob.data.api

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://app.asilisk.fr/api-festijeux/"
    val json = Json {
    // ignorer des données qui seraient proposés par l'APO
    // mais absente de notre classe de données (Festival)
        ignoreUnknownKeys = true
    }
    val api : APIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json;charset=utf-8".toMediaType()))
                .build()
                .create(APIService::class.java)
    }
}