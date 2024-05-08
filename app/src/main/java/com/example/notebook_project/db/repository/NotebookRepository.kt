package com.example.notebook_project.db.repository

import com.example.notebook_project.db.NotebookTuple
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.NotebookEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotebookRepository(private val notebookDao: NotebookDao) {
    suspend fun insertNewNotebook(notebookEntity: NotebookEntity){
        withContext(Dispatchers.IO){
            this@NotebookRepository //class field
                .notebookDao
                .insertNewNotebook(notebookEntity)
        }
    }

    suspend fun getAllNotebooks(): List<NotebookTuple> {
        return withContext(Dispatchers.IO) {
            return@withContext notebookDao.getAllNotebooks()
        }
    }

    suspend fun getNotebookById(notebook_id: Long): NotebookTuple {
        return withContext(Dispatchers.IO) {
            return@withContext notebookDao.getNotebookById(notebook_id)
        }
    }

    suspend fun getNotebookByName(notebook_name: String) : NotebookTuple {
        return withContext(Dispatchers.IO) {
            return@withContext notebookDao.getNotebookByName(notebook_name)
        }
    }

    suspend fun getNotebookByDateOfCreation(notebook_dateOfCreation: String) : NotebookTuple {
        return withContext(Dispatchers.IO) {
            return@withContext notebookDao.getNotebookByDateOfCreation(notebook_dateOfCreation)
        }
    }

    suspend fun getNotebookByDateLastEdited(notebook_dateTimeLastEdited: String) : NotebookTuple {
        return withContext(Dispatchers.IO) {
            return@withContext notebookDao.getNotebookByDateLastEdited(notebook_dateTimeLastEdited)
        }
    }

    suspend fun deleteById(notebook_id: Long){
        withContext(Dispatchers.IO) {
            notebookDao.deleteById(notebook_id)
        }
    }

    suspend fun deleteByName(notebook_name: String){
        withContext(Dispatchers.IO) {
            notebookDao.deleteByName(notebook_name)
        }
    }
}