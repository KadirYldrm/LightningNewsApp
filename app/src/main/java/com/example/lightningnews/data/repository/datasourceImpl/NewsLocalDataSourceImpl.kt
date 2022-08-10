package com.example.lightningnews.data.repository.datasourceImpl

import com.example.lightningnews.data.db.ArticleDAO
import com.example.lightningnews.data.model.Article
import com.example.lightningnews.data.repository.datasource.NewsLocalDataSource
import kotlinx.coroutines.flow.Flow

class NewsLocalDataSourceImpl(
        private val articleDAO: ArticleDAO
) : NewsLocalDataSource {

    override suspend fun saveArticleToDB(article: Article) {
        articleDAO.insert(article)
    }

    override fun getSavedArticles(): Flow<List<Article>> {
        return articleDAO.getAllArticles()
    }

    override suspend fun deleteArticlesFromDB(article: Article) {
        articleDAO.deleteArticle(article)
    }

}