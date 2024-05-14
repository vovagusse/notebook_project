package com.example.notebook_project.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.Notebook


@Database(
    version = 1,
    entities = [Notebook::class],
    exportSchema = false
)
abstract class NotebookDatabase : RoomDatabase() {
    abstract fun getNotebookDao(): NotebookDao

    companion object{
        @Volatile
        private var INSTANCE: NotebookDatabase? = null

        fun getDatabase(context: Context) : NotebookDatabase{
            val temp = INSTANCE
            if (temp != null){
                return temp
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotebookDatabase::class.java,
                    "notebook_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}