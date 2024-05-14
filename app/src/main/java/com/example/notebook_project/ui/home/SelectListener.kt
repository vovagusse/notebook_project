package com.example.notebook_project.ui.home;

import com.example.notebook_project.db.entities.Notebook

public interface SelectListener {
    fun onItemClicked(notebook: Notebook)
}
