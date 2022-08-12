package com.example.lightningnews.domain.usecase

import com.example.lightningnews.data.model.APIResponse
import com.example.lightningnews.data.util.Resource
import com.example.lightningnews.domain.repository.NewsRepository

class GetNewsHeadLinesUseCase(private val newsRepository: NewsRepository) {

    suspend fun execute(category: String,country:String,page:Int): Resource<APIResponse>{
        return newsRepository.getNewsHeadlines(category,country, page)
    }
}