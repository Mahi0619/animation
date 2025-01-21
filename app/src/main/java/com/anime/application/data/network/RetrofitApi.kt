package com.anime.application.data.network

import com.google.gson.GsonBuilder
import com.anime.application.base.SharedPref
import com.anime.application.others.Cons
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {

    private val retrofit: Retrofit by lazy {
        val gson = GsonBuilder().create()
        Retrofit.Builder()
            .baseUrl(Cons.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getOkHttpClient())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
    private fun getOkHttpClient(): OkHttpClient {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${SharedPref.getInstance().getToken()}")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .build()
                chain.proceed(request)
            }
            .build()
    }


}