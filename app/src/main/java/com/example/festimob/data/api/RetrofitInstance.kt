package com.example.festimob.data.api

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://app.asilisk.fr/api-festijeux/"
    val json = Json {
    // ignorer des données qui seraient proposés par l'APO
    // mais absente de notre classe de données (Festival)
        ignoreUnknownKeys = true
    }
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.HEADERS
    }

    private val cookieStore = mutableMapOf<String, MutableList<Cookie>>()

    private val cookieJar = object : CookieJar {
        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            val now = System.currentTimeMillis()
            val hostCookies = cookieStore.getOrPut(url.host) { mutableListOf() }

            hostCookies.removeAll { it.expiresAt <= now }
            cookies.forEach { newCookie ->
                hostCookies.removeAll {
                    it.name == newCookie.name &&
                        it.domain == newCookie.domain &&
                        it.path == newCookie.path
                }
                if (newCookie.expiresAt > now) {
                    hostCookies.add(newCookie)
                }
            }
            if (hostCookies.isEmpty()) {
                cookieStore.remove(url.host)
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            val now = System.currentTimeMillis()
            val hostCookies = cookieStore[url.host] ?: return emptyList()
            hostCookies.removeAll { it.expiresAt <= now }
            if (hostCookies.isEmpty()) {
                cookieStore.remove(url.host)
                return emptyList()
            }
            return hostCookies.filter { it.matches(url) }
        }
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor(loggingInterceptor)
        .build()

    val api : APIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json;charset=utf-8".toMediaType()))
                .build()
                .create(APIService::class.java)
    }
}
