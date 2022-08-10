package com.example.lightningnews.presentation.di

import com.example.lightningnews.data.db.ArticleDAO
import com.example.lightningnews.data.repository.datasource.NewsLocalDataSource
import com.example.lightningnews.data.repository.datasourceImpl.NewsLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(articleDAO: ArticleDAO):NewsLocalDataSource{

        return NewsLocalDataSourceImpl(articleDAO)
    }
}