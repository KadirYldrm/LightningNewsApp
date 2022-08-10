package com.example.lightningnews.domain.usecase

import com.example.lightningnews.data.model.Article
import com.example.lightningnews.domain.repository.NewsRepository

class SaveNewsUseCase(private val newsRepository: NewsRepository) {

    suspend fun execute(article: Article)=newsRepository.saveNews(article)
}