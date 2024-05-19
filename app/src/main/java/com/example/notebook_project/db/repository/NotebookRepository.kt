package com.example.notebook_project.db.repository

import android.app.Application
import android.content.Context
import com.example.notebook_project.db.NotebookDatabase
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.Notebook
import kotlinx.coroutines.flow.Flow

class NotebookRepository private constructor(context: Context) {

    private val _notebooks: Flow<List<Notebook>>
    private val notebookDao: NotebookDao

    init {
        val db = NotebookDatabase.getDatabase(context)
        notebookDao = db.getNotebookDao()
        _notebooks = notebookDao.getAllNotebooks()
    }

    fun getNotebooks() = _notebooks

    //update/insert
    suspend fun upsertNotebook(notebook: Notebook) {
        notebookDao.upsertNewNotebook(notebook)
    }

    //getall queries
    suspend fun getAllNotebooks(): Flow<List<Notebook>> {
        return notebookDao.getAllNotebooks()
    }

    //find query
    suspend fun getNotebookByName(name: String): Notebook {
        return notebookDao
            .getNotebookByName(name)
    }

    //delete query
    suspend fun deleteNotebook(notebook: Notebook) {
        notebookDao.deleteNotebook(notebook)
    }

    suspend fun deleteNotebookByName(name: String) {
        notebookDao.deleteByName(name)
    }

    companion object {
        @Volatile
        private var INSTANCE: NotebookRepository? = null

        fun getInstance(context: Context): NotebookRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = NotebookRepository(context)
                INSTANCE = instance
                instance
            }
        }
    }
}