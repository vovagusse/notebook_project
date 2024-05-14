package com.example.notebook_project.db.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notebook_project.db.NotebookDatabase
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.NotebookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotebookViewModel(application: Application) : AndroidViewModel(application) {

    private val _notebooks : LiveData<List<Notebook>>
    private val repository: NotebookRepository

    init {
        val notebookDao = NotebookDatabase
            .getDatabase(application)
            .getNotebookDao()

        repository = NotebookRepository(notebookDao)
        _notebooks = repository.readAllData
    }

    fun addNotebook(notebook: Notebook){
        viewModelScope.launch (Dispatchers.IO){
            repository.addNotebook(notebook)
        }
    }

}