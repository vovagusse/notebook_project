package com.example.notebook_project.db.viewmodel

import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.SortOrder
import com.example.notebook_project.db.repository.SortingParameter
import com.example.notebook_project.db.repository.StorageType

data class NotebookUIModel(
    val notebooks: List<Notebook>,
    val sortOrder: SortOrder,
    val sortParam: SortingParameter,
    val storageType: StorageType
)