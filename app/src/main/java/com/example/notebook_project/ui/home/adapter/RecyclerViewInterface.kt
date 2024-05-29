package com.example.notebook_project.ui.home.adapter

interface RecyclerViewInterface {
    fun onContextButtonDelete(position: Int)
    fun readNotebookBody(position: Int) : String
}
