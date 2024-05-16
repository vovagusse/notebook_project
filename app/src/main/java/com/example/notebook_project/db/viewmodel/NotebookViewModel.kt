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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.OutputStreamWriter
import java.util.Date


class NotebookViewModel(application: Application) : AndroidViewModel(application) {

//    private val _notebooks = MutableLiveData<List<Notebook>>()
    val notebooks : LiveData<List<Notebook>>
    val _storageType = MutableLiveData(StorageType.INTERNAL)
    val _sortType = MutableLiveData(SortType.ASCENDING)

    private val repository: NotebookRepository

    init {
        val notebookDao = NotebookDatabase
            .getDatabase(application)
            .getNotebookDao()

        repository = NotebookRepository(notebookDao)
        notebooks = this.repository.readAllData
    }

    fun deleteNotebook(notebook: Notebook){
        viewModelScope.launch (Dispatchers.IO) {
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

    fun getNotebookByName(name: String) : String {
//        var n: Notebook
//        viewModelScope.launch (Dispatchers.IO) {
//            repository.getNotebookByName(name)
//        }
        return ""
    }

    //==========================================================


    fun getFileLastModifiedDate(filename: String, context: Context) : Date {
        return Date(File(filename).lastModified())
    }

    fun saveFileInternal(filename: String, body: String, context: Context) : Boolean {
        return try{
            val outputStreamWriter = OutputStreamWriter(
                context.openFileOutput(filename, Context.MODE_PRIVATE)
            )
            outputStreamWriter.write(body)
            outputStreamWriter.close()
            true
        } catch(ex: IOException){
            Log.e("SAVE_FILE_ERROR", "Couldnt save the file idk")
            ex.printStackTrace()
            false
        }
    }

    fun isFileExistsInternal(filename: String, context: Context) : Boolean{
        return File(filename).exists()
    }

    fun saveFile(filename: String, body: String) {
        when (_storageType.value){
            StorageType.INTERNAL -> {}
            StorageType.EXTERNAL -> {}
            null -> {
                Log.e("saveFile()", "_storageType is nullPointer")
            }
        }
    }
}