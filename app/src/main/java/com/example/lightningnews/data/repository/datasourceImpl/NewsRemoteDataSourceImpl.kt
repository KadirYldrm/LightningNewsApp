package com.example.lightningnews.data.repository.datasourceImpl

import com.example.lightningnews.data.api.NewsAPIService
import com.example.lightningnews.data.model.APIResponse
import com.example.lightningnews.data.repository.datasource.NewsRemoteDataSource
import retrofit2.Response

class NewsRemoteDataSourceImpl(
        private val newsAPIService: NewsAPIService
) : NewsRemoteDataSource {

    override suspend fun getTopHeadlines(category: String,country: String, page: Int): Response<APIResponse> {
        return newsAPIService.getTopHeadlines(category,country, page)
    }

    override suspend fun getSearchedNews(
            country: String, searchQuery: String, page: Int
    ): Response<APIResponse> {
        return newsAPIService.getSearchedTopHeadlines(country, searchQuery, page)
    }
}