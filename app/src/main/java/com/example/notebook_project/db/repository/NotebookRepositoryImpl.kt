package com.example.notebook_project.db.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.Notebook

class NotebookRepositoryImpl(private val notebookDao: NotebookDao) : NotebookRepository{


    //update/insert
    override suspend fun upsertNotebook(notebook: Notebook){
        notebookDao.upsertNewNotebook(notebook)
    }
    //getall queries
    override suspend fun getAllNotebooksOrderedByNameASC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByNameASC()
    }
    override suspend fun getAllNotebooksOrderedByNameDESC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByNameDESC()
    }
    override suspend fun getAllNotebooksOrderedByTimeCreationASC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByTimeCreationASC()
    }
    override suspend fun getAllNotebooksOrderedByTimeCreationDESC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByTimeCreationDESC()
    }
    override suspend fun getAllNotebooksOrderedByTimeEditedASC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByTimeEditedASC()
    }
    override suspend fun getAllNotebooksOrderedByTimeEditedDESC(): LiveData<List<Notebook>>{
        return notebookDao.getAllNotebooksOrderedByTimeEditedDESC()
    }


    //find query
    override suspend fun getNotebookByName(name: String) : Notebook {
        return notebookDao
            .getNotebookByName(name)
    }

    //delete query
    override suspend fun deleteNotebook(notebook: Notebook){
        notebookDao.deleteNotebook(notebook)
    }

    override suspend fun deleteNotebookByName(name: String){
        notebookDao.deleteByName(name)
    }
}