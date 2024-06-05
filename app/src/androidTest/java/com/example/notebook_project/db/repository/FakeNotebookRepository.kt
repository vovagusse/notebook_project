package com.example.notebook_project.db.repository

import android.content.Context
import com.example.notebook_project.db.NotebookDatabase
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.Notebook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date

class FakeNotebookRepository : NotebookRepository {
    companion object {
        @Volatile
        private var INSTANCE: FakeNotebookRepository? = null
        fun getInstance(): FakeNotebookRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE?.let {
                    return it
                }
                val instance = FakeNotebookRepository()
                INSTANCE = instance
                instance
            }
        }
    }
    private val _entry = Notebook(
        0, "text.md", "Text",
        Date(1717149000L), Date(1717149300L)
    )
    private val dataset = mutableListOf(
        _entry.copy(id=1), _entry.copy(id=2),
        _entry.copy(id=3), _entry.copy(id=4))
    private val _notebooks
        : MutableStateFlow<List<Notebook>>
        = MutableStateFlow(dataset)
    override fun observeNotebooks() = _notebooks
    override suspend fun upsertNotebook(notebook: Notebook) {
        dataset.add(notebook)
        this._notebooks.value = dataset
    }
    override suspend fun deleteNotebook(notebook: Notebook) {
        dataset.remove(notebook)
        this._notebooks.value = dataset
    }
    override suspend fun deleteNotebookByName(name: String) {
        val find = dataset.find{it.notebook_name == name}
        find?.let { dataset.remove(it) }
        this._notebooks.value = dataset
    }
}