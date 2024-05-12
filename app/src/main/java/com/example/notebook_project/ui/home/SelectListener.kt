package com.example.notebook_project.ui.home;

import com.example.notebook_project.db.NotebookTuple;

public interface SelectListener {
    fun onItemClicked(notebookTuple: NotebookTuple)
}
