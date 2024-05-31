package com.example.notebook_project.db.repository

import android.app.Application
import android.content.Context
import com.example.notebook_project.db.NotebookDatabase
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.Notebook
import kotlinx.coroutines.flow.Flow

class NotebookRepository_Impl private constructor(context: Context) : NotebookRepository{

    companion object {
        @Volatile
        private var INSTANCE: NotebookRepository_Impl? = null

        fun getInstance(context: Context): NotebookRepository_Impl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }

                val instance = NotebookRepository_Impl(context)
                INSTANCE = instance
                instance
            }
        }
    }

    private val _notebooks: Flow<List<Notebook>>
    private val notebookDao: NotebookDao

    init {
        val db = NotebookDatabase.getDatabase(context)
        notebookDao = db.getNotebookDao()
        _notebooks = notebookDao.getAllNotebooks()
    }

    override fun observeNotebooks() = _notebooks
    //update/insert
    override suspend fun upsertNotebook(notebook: Notebook) {
        notebookDao.upsertNewNotebook(notebook)
    }
    //delete query
    override suspend fun deleteNotebook(notebook: Notebook) {
        notebookDao.deleteNotebook(notebook)
    }

    override suspend fun deleteNotebookByName(name: String) {
        notebookDao.deleteByName(name)
    }
}