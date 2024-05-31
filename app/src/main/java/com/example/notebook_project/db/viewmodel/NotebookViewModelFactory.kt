package com.example.notebook_project.db.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository_Impl

class NotebookViewModelFactory(
    private val repository: NotebookRepository,
    private val userPreferencesRepository: UserPreferencesRepository_Impl,
    private val applicationContext: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotebookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotebookViewModel(repository, userPreferencesRepository, applicationContext as Application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}