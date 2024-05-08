package com.example.notebook_project.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "notebook",
    indices = [Index("id"),
              Index(value= arrayOf("notebook_name", "date_time_of_creation")),
              Index(value= arrayOf("notebook_name", "date_time_last_edited")),
              ],
)
data class NotebookEntity (
    @PrimaryKey(true)
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("uri")
    val uri: String,

    @Embedded("metadata")
    val metadata: NotebookMetadataEntity
)