package com.example.notebook_project.db.repository

import androidx.lifecycle.LiveData
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.Notebook

class NotebookRepository(private val notebookDao: NotebookDao) {

    val readAllData: LiveData<List<Notebook>> =
        notebookDao.getAllNotebooksOrderedNameASC()

    suspend fun upsertNotebook(notebook: Notebook){
        notebookDao.upsertNewNotebook(notebook)
    }

    suspend fun deleteNotebook(notebook: Notebook){
        notebookDao.deleteNotebook(notebook)
    }

    suspend fun deleteNotebookByName(name: String){
        notebookDao.deleteByName(name)
    }

    suspend fun getNotebookByName(name: String) : Notebook {
        return notebookDao.getNotebookByName(name)
    }
}