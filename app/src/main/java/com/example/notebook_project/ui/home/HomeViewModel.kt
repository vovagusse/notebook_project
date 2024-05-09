package com.example.notebook_project.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notebook_project.db.NotebookTuple
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.dao.NotebookDao_Impl
import com.example.notebook_project.db.repository.NotebookRepository

class HomeViewModel : ViewModel() {

    private val _notebooks = MutableLiveData<List<NotebookTuple>>()
    val notebooks: LiveData<List<NotebookTuple>> = _notebooks


    fun loadNotebooks() {

    }
}