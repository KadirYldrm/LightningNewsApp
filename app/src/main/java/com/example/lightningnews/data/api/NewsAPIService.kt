package com.example.lightningnews.data.api

import com.example.lightningnews.BuildConfig
import com.example.lightningnews.data.model.APIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPIService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
            @Query("category")
            category: String,
            @Query("country")
            country: String,
            @Query("page")
            page: Int,
            @Query("apiKey")
            apiKey: String = BuildConfig.API_KEY
    ): Response<APIResponse>

    @GET("v2/top-headlines")
    suspend fun getSearchedTopHeadlines(
            @Query("country")
            country: String,
            @Query("q")
            searchQuery: String,
            @Query("page")
            page: Int,
            @Query("apiKey")
            apiKey: String = BuildConfig.API_KEY
    ): Response<APIResponse>
}