package com.example.notebook_project.db.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.SortOrder
import com.example.notebook_project.db.repository.SortingParameter
import com.example.notebook_project.db.repository.StorageType
import com.example.notebook_project.db.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date


class NotebookViewModel(
    val repository: NotebookRepository,
    private val userPrefsRepository: UserPreferencesRepository
) : ViewModel() {

    private val _sortOrderFlow = MutableStateFlow(SortOrder.ASCENDING)
    private val _sortParamFlow = MutableStateFlow(SortingParameter.BY_NAME)
    private val _storageTypeFlow = MutableStateFlow(StorageType.INTERNAL)

    private val notebookUiModelFlow = combine(
        repository.getNotebooks(),
        _sortOrderFlow,
        _sortParamFlow,
        _storageTypeFlow
    ){  notebooks: List<Notebook>,
        sortOrder: SortOrder,
        sortParam: SortingParameter,
        storageType: StorageType ->
        return@combine NotebookUIModel(
            sortNotebooks(
                notebooks,
                sortOrder,
                sortParam),
            sortOrder,
            sortParam,
            storageType
        )
    }
    val notebookUiModel = notebookUiModelFlow.asLiveData()

    private fun sortNotebooks(
        notebooks: List<Notebook>,
        sortOrder: SortOrder,
        sortParam: SortingParameter
    ): List<Notebook> {
        when (sortOrder) {
            SortOrder.ASCENDING -> {
                return when (sortParam) {
                    SortingParameter.BY_NAME -> {
                        notebooks.sortedBy {it.notebook_name}
                    }
                    SortingParameter.BY_DATE_OF_CREATION -> {
                        notebooks.sortedBy { it.dateTimeOfCreation }
                    }
                    SortingParameter.BY_DATE_LAST_EDITED -> {
                        notebooks.sortedBy { it.dateTimeLastEdited }
                    }
                }
            }
            SortOrder.DESCENDING -> {
                return when (sortParam) {
                    SortingParameter.BY_NAME -> {
                        notebooks.sortedBy {it.notebook_name}
                    }
                    SortingParameter.BY_DATE_OF_CREATION -> {
                        notebooks.sortedBy { it.dateTimeOfCreation }
                    }
                    SortingParameter.BY_DATE_LAST_EDITED -> {
                        notebooks.sortedBy { it.dateTimeLastEdited }
                    }
                }
            }
        }
    }

    fun deleteNotebook(notebook: Notebook){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteNotebook(notebook)
        }
    }

    fun deleteNotebookByName(name: String){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteNotebookByName(name)
        }
    }

    fun upsertNotebook(notebook: Notebook){
        Log.i("print", "upsertNotebook: $notebook")
        viewModelScope.launch (Dispatchers.IO){
            repository.upsertNotebook(notebook)
        }
    }

    //==================================================================================

    //IO

    fun getFileLastModifiedDate(filename: String, context: Context) : Date {
        return Date(File(filename).lastModified())
    }

    fun saveNotebook(filename: String, body: String) = when (notebookUiModel.value?.storageType){
        StorageType.INTERNAL -> {
            //sdhsadh
            true
        }
        StorageType.EXTERNAL -> {
            //asdisadhsaid
            true
        }
        null -> {
            Log.e("saveFile()", "_storageType is nullPointer")
            false
        }
    }

    fun openNotebook(filename: String) = when (notebookUiModel.value?.storageType){
        StorageType.INTERNAL -> {""}
        StorageType.EXTERNAL -> {""}
        null -> {
            Log.e("openNotebook()", "_storageType is not specified")
            ""
        }
    }

    fun deleteNotebook(filename: String) = when (notebookUiModel.value?.storageType){
        StorageType.INTERNAL -> {true}
        StorageType.EXTERNAL -> {true}
        null -> {
            Log.e("deleteNotebook()", "_storageType is not specified")
            false
        }
    }

    fun print() {
        Log.i("print", "BEGIN PRINT")
        notebookUiModel.value?.notebooks?.forEach {
            Log.i("print", it.toString())
        }
        Log.i("print", "END PRINT")
    }
}

