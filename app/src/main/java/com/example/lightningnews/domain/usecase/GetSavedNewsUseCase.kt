package com.example.lightningnews.domain.usecase

import com.example.lightningnews.data.model.Article
import com.example.lightningnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetSavedNewsUseCase(private val newsRepository: NewsRepository) {

    fun execute():Flow<List<Article>>{
        return newsRepository.getSavedNews()
    }
}