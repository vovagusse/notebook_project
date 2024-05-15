package com.example.notebook_project.db.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.notebook_project.db.NotebookDatabase
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.NotebookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.Date


class NotebookViewModel(application: Application) : AndroidViewModel(application) {

    val _notebooks : LiveData<List<Notebook>>
    private val repository: NotebookRepository

    init {
        val notebookDao = NotebookDatabase
            .getDatabase(application)
            .getNotebookDao()

        repository = NotebookRepository(notebookDao)
        _notebooks = repository.readAllData
    }



    fun addNotebook(notebook: Notebook){
        viewModelScope.launch (Dispatchers.IO){
            repository.addNotebook(notebook)
        }
    }

    fun getNotebookByName(name: String) {
//        var n: Notebook
        viewModelScope.launch (Dispatchers.IO) {
            repository.getNotebookByName(name)
        }
    }


//    fun getFileLastModifiedDate(filename: String, context: Context) : Date {
//        return Date(File(filename).lastModified())
//    }
//
//    fun readFileInternal(filename: String, context: Context): String {
//        return try {
//            val inputStream: InputStream? = context.openFileInput(filename)
//            if (inputStream != null) {
//                val inputStreamReader = InputStreamReader(inputStream)
//                val bufferedReader = BufferedReader(inputStreamReader)
//                var receiveString: String? = ""
//                val stringBuilder = StringBuilder()
//                while ((bufferedReader.readLine().also { receiveString = it }) != null) {
//                    stringBuilder.append("\n").append(receiveString)
//                }
//                inputStream.close()
//                stringBuilder.toString()
//            } else {
//                ""
//            }
//        } catch (e: FileNotFoundException) {
//            Log.e("login activity", "File not found: $e")
//            ""
//        } catch (e: IOException) {
//            Log.e("login activity", "Can not read file: $e")
//            ""
//        }
//    }
//
//    fun saveFileInternal(filename: String, body: String, context: Context) : Boolean {
//        return try{
//            val outputStreamWriter = OutputStreamWriter(
//                context.openFileOutput(filename, Context.MODE_PRIVATE)
//            )
//            outputStreamWriter.write(body)
//            outputStreamWriter.close()
//            true
//        } catch(ex: IOException){
//            Log.e("SAVE_FILE_ERROR", "Couldnt save the file idk")
//            ex.printStackTrace()
//            false
//        }
//    }
//
//    fun isFileExistsInternal(filename: String, context: Context) : Boolean{
//        return File(filename).exists()
//    }
}