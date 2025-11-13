package com.example.listi.network

import android.content.Context
import com.example.listi.AuthInterceptor
import com.example.listi.TokenManager
import com.example.listi.data.api.ProductService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.0.96:8080/"

    private lateinit var retrofit: Retrofit

    fun init(context: Context) {
        val tokenManager = TokenManager(context)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager))
            .addInterceptor(logging)
            .build()

        // Importante para que funcione correctamente
        val json = Json { ignoreUnknownKeys = true }

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) //
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    val loginService: LoginService by lazy { retrofit.create(LoginService::class.java) }
    val productService: ProductService by lazy { retrofit.create(ProductService::class.java) }

    val categoryService: CategoryService by lazy { retrofit.create(CategoryService::class.java) }
}

