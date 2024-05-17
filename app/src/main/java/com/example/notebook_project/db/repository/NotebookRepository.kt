package com.example.notebook_project.db.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.notebook_project.db.NotebookDatabase
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.Notebook

class NotebookRepository(application: Application) {

    private val _notebooks: LiveData<List<Notebook>>
    private val notebookDao: NotebookDao

    init{
        val db = NotebookDatabase.getDatabase(application)
        notebookDao = db.getNotebookDao()
        _notebooks = notebookDao.getAllNotebooksOrderedByNameASC()
    }

    fun get_notebooks(): LiveData<List<Notebook>>{
        return _notebooks
    }

    //update/insert
    suspend fun upsertNotebook(notebook: Notebook){
        notebookDao.upsertNewNotebook(notebook)
    }
    //getall queries
     suspend fun getAllNotebooksOrderedByNameASC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByNameASC()
    }
     suspend fun getAllNotebooksOrderedByNameDESC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByNameDESC()
    }
     suspend fun getAllNotebooksOrderedByTimeCreationASC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByTimeCreationASC()
    }
     suspend fun getAllNotebooksOrderedByTimeCreationDESC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByTimeCreationDESC()
    }
     suspend fun getAllNotebooksOrderedByTimeEditedASC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByTimeEditedASC()
    }
     suspend fun getAllNotebooksOrderedByTimeEditedDESC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByTimeEditedDESC()
    }


    //find query
     suspend fun getNotebookByName(name: String) : Notebook {
        return notebookDao
            .getNotebookByName(name)
    }

    //delete query
     suspend fun deleteNotebook(notebook: Notebook){
        notebookDao.deleteNotebook(notebook)
    }

     suspend fun deleteNotebookByName(name: String){
        notebookDao.deleteByName(name)
    }
}