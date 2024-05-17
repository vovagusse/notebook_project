package com.example.notebook_project.db.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.Notebook

interface NotebookRepository {


    //update/insert
    suspend fun upsertNotebook(notebook: Notebook)
    //getall queries
    suspend fun getAllNotebooksOrderedByNameASC(): LiveData<List<Notebook>>
    suspend fun getAllNotebooksOrderedByNameDESC(): LiveData<List<Notebook>>
    suspend fun getAllNotebooksOrderedByTimeCreationASC(): LiveData<List<Notebook>>
    suspend fun getAllNotebooksOrderedByTimeCreationDESC(): LiveData<List<Notebook>>
    suspend fun getAllNotebooksOrderedByTimeEditedASC(): LiveData<List<Notebook>>
    suspend fun getAllNotebooksOrderedByTimeEditedDESC(): LiveData<List<Notebook>>

    //find query
    suspend fun getNotebookByName(name: String) : Notebook

    //delete query
    suspend fun deleteNotebook(notebook: Notebook)
    suspend fun deleteNotebookByName(name: String)
}