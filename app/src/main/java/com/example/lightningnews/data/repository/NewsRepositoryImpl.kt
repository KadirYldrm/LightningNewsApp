package com.example.lightningnews.data.repository

import com.example.lightningnews.data.model.APIResponse
import com.example.lightningnews.data.model.Article
import com.example.lightningnews.data.repository.datasource.NewsLocalDataSource
import com.example.lightningnews.data.repository.datasource.NewsRemoteDataSource
import com.example.lightningnews.data.util.Resource
import com.example.lightningnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class NewsRepositoryImpl(
        private val newsRemoteDataSource: NewsRemoteDataSource,
        private val newsLocalDataSource: NewsLocalDataSource
) : NewsRepository {
    override suspend fun getNewsHeadlines(category: String, country: String, page: Int): Resource<APIResponse> {
        return responseToResource(newsRemoteDataSource.getTopHeadlines(category, country, page))
    }

    override suspend fun getSearchedNews(
            country: String, searchQuery: String, page: Int
    ): Resource<APIResponse> {
        return responseToResource(newsRemoteDataSource.getSearchedNews(
                country, searchQuery, page
        ))
    }

    private fun responseToResource(response: Response<APIResponse>): Resource<APIResponse> {

        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)

            }
        }
        return Resource.Error(response.message())

    }


    override suspend fun saveNews(article: Article) {
        newsLocalDataSource.saveArticleToDB(article)
    }

    override suspend fun deleteNews(article: Article) {
        newsLocalDataSource.deleteArticlesFromDB(article)
    }

    override fun getSavedNews(): Flow<List<Article>> {
        return newsLocalDataSource.getSavedArticles()
    }

}