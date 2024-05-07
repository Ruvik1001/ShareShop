package com.grishina.about_product.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("search")
    fun searchRecipes(
        @Query("str") query: String,
    ): Call<String>
}