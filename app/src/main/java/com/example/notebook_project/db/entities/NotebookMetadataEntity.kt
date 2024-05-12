package com.example.notebook_project.db.entities

data class NotebookMetadataEntity (
    val notebook_name: String,
    val description: String,
    val dateTimeOfCreation: String,
    val dateTimeLastEdited: String
)