package com.example.lightningnews.domain.usecase

import com.example.lightningnews.data.model.APIResponse
import com.example.lightningnews.data.util.Resource
import com.example.lightningnews.domain.repository.NewsRepository

class GetSearchedNewsUseCase(private val newsRepository: NewsRepository) {

    suspend fun execute(country: String, searchQuery: String, page: Int): Resource<APIResponse> {
        return newsRepository.getSearchedNews(country, searchQuery, page)
    }
}