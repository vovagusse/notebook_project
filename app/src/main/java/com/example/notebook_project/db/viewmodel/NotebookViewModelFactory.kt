package com.example.notebook_project.db.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository

class NotebookViewModelFactory(
    private val repository: NotebookRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val context: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotebookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotebookViewModel(repository, userPreferencesRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}