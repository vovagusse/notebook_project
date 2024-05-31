package com.example.notebook_project.db.repository

import com.example.notebook_project.db.entities.Notebook
import kotlinx.coroutines.flow.Flow

interface NotebookRepository {
    fun observeNotebooks() : Flow<List<Notebook>>
    suspend fun upsertNotebook(notebook: Notebook)
    suspend fun deleteNotebook(notebook: Notebook)
    suspend fun deleteNotebookByName(name: String)
}