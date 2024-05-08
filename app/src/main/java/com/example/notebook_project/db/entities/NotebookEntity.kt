package com.example.notebook_project.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notebook",)
data class NotebookEntity (
    @PrimaryKey(true)
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("uri")
    val uri: String,

    @Embedded("metadata")
    val metadata: NotebookMetadataEntity
)