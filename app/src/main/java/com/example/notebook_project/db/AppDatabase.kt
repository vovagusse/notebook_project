package com.example.notebook_project.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.NotebookEntity


@Database(
    version = 1,
    entities = [NotebookEntity::class],
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserNotebookDao(): NotebookDao

}