package com.example.lightningnews.presentation.di

import android.app.Application
import com.example.lightningnews.domain.usecase.*
import com.example.lightningnews.presentation.viewmodel.NewsViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class FactoryModule {

    @Singleton
    @Provides
    fun provideNewsViewModuleFactory(
            application: Application,
            getNewsHeadLinesUseCase: GetNewsHeadLinesUseCase,
            getSearchedNewsUseCase: GetSearchedNewsUseCase,
            saveNewsUseCase: SaveNewsUseCase,
            getSavedNewsUseCase: GetSavedNewsUseCase,
            deleteSavedNewsUseCase: DeleteSavedNewsUseCase
    ):NewsViewModelFactory {
        return NewsViewModelFactory(application,getNewsHeadLinesUseCase,getSearchedNewsUseCase,saveNewsUseCase,getSavedNewsUseCase,deleteSavedNewsUseCase)

    }
}