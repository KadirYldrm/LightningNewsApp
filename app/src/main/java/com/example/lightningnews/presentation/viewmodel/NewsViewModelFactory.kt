package com.example.lightningnews.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lightningnews.domain.usecase.*

class NewsViewModelFactory(
        private val app:Application,
        private val getNewsHeadLinesUseCase: GetNewsHeadLinesUseCase,
        private val getSearchedNewsUseCase: GetSearchedNewsUseCase,
        private val saveNewsUseCase: SaveNewsUseCase,
        private val getSavedNewsUseCase: GetSavedNewsUseCase,
        private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase
):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsVM(app,getNewsHeadLinesUseCase,getSearchedNewsUseCase,saveNewsUseCase,getSavedNewsUseCase,deleteSavedNewsUseCase) as T
    }
}