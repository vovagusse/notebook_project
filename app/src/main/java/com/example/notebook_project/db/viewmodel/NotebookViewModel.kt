package com.example.notebook_project.db.viewmodel

import android.R.attr.data
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.SortOrder
import com.example.notebook_project.db.repository.SortingParameter
import com.example.notebook_project.db.repository.StorageType
import com.example.notebook_project.db.repository.UserPreferences
import com.example.notebook_project.db.repository.UserPreferencesRepository
import com.example.notebook_project.db.repository.UserTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Date


class NotebookViewModel(
    val repository: NotebookRepository,
    private val userPrefsRepository: UserPreferencesRepository,
    val context: Application
) : AndroidViewModel(context) {

    private val notebookUiModelFlow = combine(
        repository.observeNotebooks(),
        userPrefsRepository.observeUserPreferences()
    )
    {  notebooks: List<Notebook>,
        prefs: UserPreferences
         ->
        return@combine NotebookUIModel(
            sortNotebooks(
                notebooks,
                prefs.sortOrder,
                prefs.sortParam),
            prefs.sortOrder,
            prefs.sortParam,
            prefs.storageType,
            prefs.userTheme
        )
    }
    val notebookUiModel = notebookUiModelFlow.asLiveData(viewModelScope.coroutineContext)

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
                        notebooks.sortedByDescending {it.notebook_name}
                    }
                    SortingParameter.BY_DATE_OF_CREATION -> {
                        notebooks.sortedByDescending { it.dateTimeOfCreation }
                    }
                    SortingParameter.BY_DATE_LAST_EDITED -> {
                        notebooks.sortedByDescending { it.dateTimeLastEdited }
                    }
                }
            }
        }
    }

    fun upsertNotebook(notebook: Notebook, data: String){
        _writeToFile(notebook.uri, data)
        _upsertNotebook(notebook)
    }
    fun getNotebookByName(name: String) : Notebook? =
        this.notebookUiModel.value?.notebooks?.find {
            it.notebook_name == name
        }
    fun readNotebookByName(name: String) : String {
        val query = getNotebookByName(name)
        return if (query!=null) _readFromFile(query.uri)
        else ""
    }
    fun readNotebookByUri(uri: String): String {
        return if (uri.isNotEmpty()) _readFromFile(uri)
        else ""
    }
    fun deleteNotebook(name: String) : Boolean{
        val query = getNotebookByName(name)
        if (query != null){
            _deleteFile(query.uri)
            _deleteNotebookByName(query.notebook_name)
            return true
        }
        return false
    }
    fun getNotebookByPosition(position: Int) : Notebook? =
        this.notebookUiModel.value?.notebooks?.get(position)
    fun renameNotebook(old_uri: String, new_uri: String) {
        val old_f = File(old_uri)
        old_f.renameTo(File(new_uri))
    }

    private fun _writeToFile(uri: String, data: String) {
        try {
            val fos: FileOutputStream = context.openFileOutput(uri, Context.MODE_PRIVATE)
            fos.write(data.toByteArray(charset=Charsets.UTF_8))
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    private fun _upsertNotebook(notebook: Notebook){
//        Log.i("print", "upsertNotebook: $notebook")
        viewModelScope.launch (Dispatchers.IO){
            repository.upsertNotebook(notebook)
        }
    }
    private fun _readFromFile(uri: String): String {
        return try {
            val fin: FileInputStream = context.openFileInput(uri)

            var content: Char = ' '
            val buffer = StringBuilder()
            val finbuff =
                BufferedReader(
                    InputStreamReader(
                        fin, StandardCharsets.UTF_8))
            while (finbuff.read().also{
                    content = it.toChar()
                } != -1) {
                buffer.append(content)
            }
            fin.close()
            finbuff.close()
            buffer.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            //return empty string
            ""
        }
    }
    private fun _deleteNotebook(notebook: Notebook){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteNotebook(notebook)
        }
    }
    private fun _deleteFile(uri: String){
        try {
            context.deleteFile(uri)
        } catch (e: IOException){
            e.printStackTrace()
        }
    }
    private fun _deleteNotebookByName(name: String){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteNotebookByName(name)
        }
    }

    fun changeSortOrder(sortOrder: SortOrder){
        viewModelScope.launch {
            userPrefsRepository.updateSortOrder(sortOrder)
        }
    }
    fun changeSortParam(sortParam: SortingParameter){
        viewModelScope.launch {
            userPrefsRepository.updateSortParam(sortParam)
        }
    }
    fun changeStorageType(storageType: StorageType){
        viewModelScope.launch {
            userPrefsRepository.updateStorageType(storageType)
        }
    }
    fun changeFolder(folder: String){
        viewModelScope.launch {
            userPrefsRepository.updateFolder(folder)
        }
    }
    fun getFolder() = runBlocking (Dispatchers.IO) {
        userPrefsRepository.userPreferencesFlow.asLiveData().value?.folder.toString()
    }
    fun changeTheme(theme: UserTheme){
        viewModelScope.launch {
            userPrefsRepository.updateUserTheme(theme)
        }
    }

    //==================================================================================

    //IO

    fun getFileLastModifiedDate(filename: String) : Date {
        return Date(File(filename).lastModified())
    }

//    fun saveNotebook(filename: String, body: String) = when (notebookUiModel.value?.storageType){
//        StorageType.INTERNAL -> {
//            //sdhsadh
//            true
//        }
//        StorageType.EXTERNAL -> {
//            //asdisadhsaid
//            true
//        }
//        null -> {
//            Log.e("saveFile()", "_storageType is nullPointer")
//            false
//        }
//    }

//    fun openNotebook(filename: String) = when (notebookUiModel.value?.storageType){
//        StorageType.INTERNAL -> {""}
//        StorageType.EXTERNAL -> {""}
//        null -> {
//            Log.e("openNotebook()", "_storageType is not specified")
//            ""
//        }
//    }

//    fun deleteNotebook(filename: String) = when (notebookUiModel.value?.storageType){
//        StorageType.INTERNAL -> {true}
//        StorageType.EXTERNAL -> {true}
//        null -> {
//            Log.e("deleteNotebook()", "_storageType is not specified")
//            false
//        }
//    }
//    fun print() {
//        Log.i("print", "BEGIN PRINT")
//        notebookUiModel.value?.notebooks?.forEach {
//            Log.i("print", it.toString())
//        }
//        Log.i("print", "END PRINT")
//    }


}

