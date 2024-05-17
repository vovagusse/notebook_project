package com.example.notebook_project.db.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notebook_project.db.NotebookDatabase
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.NotebookRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Date


@OptIn(ExperimentalCoroutinesApi::class)
class NotebookViewModel(application: Application) : AndroidViewModel(application) {

    private var _notebooks = MutableLiveData<List<Notebook>>()
    val notebooks : LiveData<List<Notebook>>
        get() = _notebooks
    private var _storageType = MutableLiveData(StorageType.INTERNAL)
//    val storageType : LiveData<StorageType> = _storageType
    private var _sortType = MutableLiveData(SortType.ASCENDING)
//    val sortType : LiveData<SortType> = _sortType
    private var _sortParam = MutableLiveData(SortingParameter.BY_NAME)
//    val sortParam : LiveData<SortingParameter> = _sortParam
    private val repository: NotebookRepository

    override fun onCleared() {
        super.onCleared()
        Log.i("print", "cleared vm")
    }

    init {
        val notebookDao = NotebookDatabase
            .getDatabase(application)
            .getNotebookDao()

        repository = NotebookRepositoryImpl(notebookDao)
        getAllNotebooks()
        this.print()
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
        viewModelScope.launch (Dispatchers.IO){
            repository.upsertNotebook(notebook)
        }
    }

//    fun getNotebookByName(name: String) : Notebook {
//        viewModelScope.launch {
//            return this@NotebookViewModel
//                .repository
//                .getNotebookByName(name)
//        }
//
//    }

    fun getAllNotebooks(){
        when (_sortType.value) {
            SortType.ASCENDING -> {
                getAllNotebooksByParamAsc()
            }
            SortType.DESCENDING -> {
                getAllNotebooksByParamDesc()
            }
            null -> {}
        }
    }

    private fun getAllNotebooksByParamAsc(){
        when (_sortParam.value){
            SortingParameter.BY_NAME -> {
                val result = viewModelScope.async {
                    withContext(Dispatchers.IO){
                        repository.getAllNotebooksOrderedByNameASC()
                    }

                }
                result.invokeOnCompletion {
                    if (it == null){
                        this@NotebookViewModel._notebooks.value = result.getCompleted().value
                    }
                }
            }
            SortingParameter.BY_DATE_OF_CREATION -> {
                val result = viewModelScope.async {
                    withContext(Dispatchers.IO){
                        repository.getAllNotebooksOrderedByTimeCreationASC()
                    }

                }
                result.invokeOnCompletion {
                    if (it == null){
                        this@NotebookViewModel._notebooks.value = result.getCompleted().value
                    }
                }
            }
            SortingParameter.BY_DATE_LAST_EDITED -> {
                val result = viewModelScope.async {
                    withContext(Dispatchers.IO){
                        repository.getAllNotebooksOrderedByTimeEditedASC()
                    }

                }
                result.invokeOnCompletion {
                    if (it == null){
                        this@NotebookViewModel._notebooks.value = result.getCompleted().value
                    }
                }
            }
            null -> {}
        }
    }

    private fun getAllNotebooksByParamDesc() {
        when (_sortParam.value){
            SortingParameter.BY_NAME -> {
                val result = viewModelScope.async {
                    withContext(Dispatchers.IO){
                        repository.getAllNotebooksOrderedByNameDESC()
                    }

                }
                result.invokeOnCompletion {
                    if (it == null){
                        this@NotebookViewModel._notebooks.value = result.getCompleted().value
                    }
                }
            }
            SortingParameter.BY_DATE_OF_CREATION -> {
                val result = viewModelScope.async {
                    withContext(Dispatchers.IO){
                        repository.getAllNotebooksOrderedByTimeCreationDESC()
                    }
                }
                result.invokeOnCompletion {
                    if (it == null){
                        this@NotebookViewModel._notebooks.value = result.getCompleted().value
                    }
                }
            }
            SortingParameter.BY_DATE_LAST_EDITED -> {
                val result = viewModelScope.async {
                    withContext(Dispatchers.IO){
                        repository.getAllNotebooksOrderedByTimeEditedDESC()
                    }

                }
                result.invokeOnCompletion {
                    if (it == null){
                        this@NotebookViewModel._notebooks.value = result.getCompleted().value
                    }
                }
            }
            null -> {}
        }
    }

    fun changeSortType(newSortType: SortType) {
        _sortType.value = newSortType
    }

    fun changeStorageType(newStorageType: StorageType){
        _storageType.value = newStorageType
    }

    fun changeSortParam(sortParam: SortingParameter){
        _sortParam.value = sortParam
    }

    //==================================================================================

    //IO

    fun getFileLastModifiedDate(filename: String, context: Context) : Date {
        return Date(File(filename).lastModified())
    }

    fun saveNotebook(filename: String, body: String) = when (_storageType.value){
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

    fun openNotebook(filename: String) = when (_storageType.value){
        StorageType.INTERNAL -> {""}
        StorageType.EXTERNAL -> {""}
        null -> {
            Log.e("openNotebook()", "_storageType is not specified")
            ""
        }
    }

    fun deleteNotebook(filename: String) = when (_storageType.value){
        StorageType.INTERNAL -> {true}
        StorageType.EXTERNAL -> {true}
        null -> {
            Log.e("deleteNotebook()", "_storageType is not specified")
            false
        }
    }

    fun print() {
        Log.i("print", "BEGIN PRINT")
        _notebooks.value?.forEach {
            Log.i("print", it.toString())
        }
        Log.i("print", "END PRINT")
    }
}