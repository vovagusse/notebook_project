package com.example.notebook_project.db.dao

import kotlinx.coroutines.flow.Flow
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Upsert
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.util.Converters


@Dao
@TypeConverters(Converters::class)
interface NotebookDao {
    //Upsert query
    @Upsert(entity = Notebook::class)
    suspend fun upsertNewNotebook(notebook: Notebook)

    //Select ALL queries

    @Query("SELECT * FROM notebook ORDER BY notebook_name ASC")
    fun getAllNotebooks(): Flow<List<Notebook>>

    // find queries
    @Query("SELECT * FROM notebook WHERE notebook_name = :notebook_name")
    suspend fun getNotebookByName(
        notebook_name: String) : Notebook


    //Deletion queries

    @Delete
    suspend fun deleteNotebook(notebook: Notebook)


    @Query("DELETE FROM notebook " +
            "WHERE notebook_name = :notebook_name")
    suspend fun deleteByName(notebook_name: String)
}