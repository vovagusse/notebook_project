package com.example.notebook_project.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notebook_project.db.NotebookTuple
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.dao.NotebookDao_Impl
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.util.StorageType


class HomeViewModel : ViewModel() {

    private val _notebooks = MutableLiveData<List<NotebookTuple>>()
    val notebooks: LiveData<List<NotebookTuple>> = _notebooks
    private val _spans = MutableLiveData<Int>(2)
    val spans : LiveData<Int> = _spans

    // Returns true, if reading went without errors
    fun loadNotebooks(storageType: StorageType, folder: String?) : Boolean {
        if (storageType == StorageType.EXTERNAL_STORAGE){
            folder?.let{

            }?.apply {
                return false
            }
        }
        return true
    }


    fun changeSpansTo(newSpans: Int){
        _spans.value = newSpans
    }
}