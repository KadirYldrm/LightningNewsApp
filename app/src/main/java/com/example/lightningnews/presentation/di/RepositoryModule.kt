package com.example.lightningnews.presentation.di

import com.example.lightningnews.data.repository.NewsRepositoryImpl
import com.example.lightningnews.data.repository.datasource.NewsLocalDataSource
import com.example.lightningnews.data.repository.datasource.NewsRemoteDataSource
import com.example.lightningnews.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(
            newsRemoteDataSource: NewsRemoteDataSource,
            newsLocalDataSource: NewsLocalDataSource
    ): NewsRepository {
        return NewsRepositoryImpl(newsRemoteDataSource,newsLocalDataSource)
    }
}