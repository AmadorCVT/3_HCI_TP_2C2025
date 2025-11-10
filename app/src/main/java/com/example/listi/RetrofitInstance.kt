package com.example.listi

import android.content.Context

import com.example.listi.services.LoginService
import com.example.listi.data.api.ProductService
import com.example.listi.services.CategoryService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

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

        retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // ✅ usar para Android Emulator
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val loginService: LoginService by lazy { retrofit.create(LoginService::class.java) }
    val productService: ProductService by lazy { retrofit.create(ProductService::class.java) }

    val categoryService: CategoryService by lazy { retrofit.create(CategoryService::class.java) }
}
