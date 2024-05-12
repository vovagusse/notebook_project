package com.example.notebook_project.db

import androidx.room.ColumnInfo

data class NotebookTuple(
    @ColumnInfo(name="id")
    val id: Long,
    @ColumnInfo(name="uri")
    val uri: String,
    @ColumnInfo(name="metadatanotebook_name")
    val notebook_name: String,
    @ColumnInfo(name="metadatadateTimeOfCreation")
    val dateTimeOfCreation: String,
    @ColumnInfo(name="metadatadateTimeLastEdited")
    val dateTimeLastEdited: String
)
