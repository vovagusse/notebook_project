package com.example.notebook_project.db.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.NotebookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date


@OptIn(ExperimentalCoroutinesApi::class)
class NotebookViewModel(application: Application) : AndroidViewModel(application) {

    var _notebooks : LiveData<List<Notebook>>

    private var _storageType = MutableLiveData(StorageType.INTERNAL)
//    val storageType : LiveData<StorageType> = _storageType
    private var _sortType = MutableLiveData(SortType.ASCENDING)
//    val sortType : LiveData<SortType> = _sortType
    private var _sortParam = MutableLiveData(SortingParameter.BY_NAME)
//    val sortParam : LiveData<SortingParameter> = _sortParam
    private val repository: NotebookRepository

    init {
        Log.i("print", "viewmodel init")
        repository = NotebookRepository(application)
        _notebooks = repository.get_notebooks()
    }

    override fun onCleared() {
        this.print()
        super.onCleared()
        Log.i("print", "cleared viewmodel")
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

//    fun getNotebookByName(name: String) : Notebook {
//        viewModelScope.launch {
//            return this@NotebookViewModel
//                .repository
//                .getNotebookByName(name)
//        }
//
//    }

    fun getAllNotebooks(){
        Log.i("print", "getAllNotebooks: called")
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
                viewModelScope.launch (Dispatchers.Main) {
                    delay(500)

                    _notebooks =
                        repository.getAllNotebooksOrderedByNameASC()
                }
            }
            SortingParameter.BY_DATE_OF_CREATION -> {
                viewModelScope.launch (Dispatchers.Main){
                    delay(500)

                    _notebooks =
                        repository.getAllNotebooksOrderedByTimeCreationASC()
                }
            }
            SortingParameter.BY_DATE_LAST_EDITED -> {
                viewModelScope.launch (Dispatchers.Main) {
                    delay(500)

                    _notebooks =
                        repository.getAllNotebooksOrderedByTimeEditedASC()

                }
            }
            null -> {}
        }
    }

    private fun getAllNotebooksByParamDesc() {
        when (_sortParam.value){
            SortingParameter.BY_NAME -> {
                viewModelScope.launch (Dispatchers.Main) {
                    delay(500)

                    _notebooks=
                        repository.getAllNotebooksOrderedByNameDESC()
                }
            }
            SortingParameter.BY_DATE_OF_CREATION -> {
                viewModelScope.launch (Dispatchers.Main){
                    delay(500)

                    _notebooks=
                        repository.getAllNotebooksOrderedByTimeCreationDESC()
                }
            }
            SortingParameter.BY_DATE_LAST_EDITED -> {
                viewModelScope.launch (Dispatchers.Main) {
                    delay(500)

                    _notebooks=
                        repository.getAllNotebooksOrderedByTimeEditedDESC()

                }
            }
            null -> {}
        }
    }

    fun changeSortType(newSortType: SortType) {
        _sortType.value = newSortType
        getAllNotebooks()
    }

    fun changeStorageType(newStorageType: StorageType){
        _storageType.value = newStorageType
        getAllNotebooks()
    }

    fun changeSortParam(sortParam: SortingParameter){
        _sortParam.value = sortParam
        getAllNotebooks()
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