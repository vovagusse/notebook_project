package com.example.notebook_project.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.notebook_project.db.entities.Notebook


@Dao
interface NotebookDao {
    //Upsert query
    @Upsert(entity = Notebook::class)
    suspend fun upsertNewNotebook(notebook: Notebook)


    //Select ALL queries

    @Query("SELECT * FROM notebook ORDER BY notebook_name ASC")
    fun getAllNotebooksOrderedNameASC(): LiveData<List<Notebook>>

    @Query("SELECT * FROM notebook ORDER BY notebook_name DESC")
    fun getAllNotebooksOrderedNameDESC(): LiveData<List<Notebook>>

    @Query("SELECT * FROM notebook ORDER BY notebook_dateTimeOfCreation ASC")
    fun getAllNotebooksOrderedTimeCreationASC(): LiveData<List<Notebook>>

    @Query("SELECT * FROM notebook ORDER BY notebook_dateTimeOfCreation DESC")
    fun getAllNotebooksOrderedTimeCreationDESC(): LiveData<List<Notebook>>

    @Query("SELECT * FROM notebook ORDER BY notebook_dateTimeLastEdited ASC")
    fun getAllNotebooksOrderedTimeEditedASC(): LiveData<List<Notebook>>

    @Query("SELECT * FROM notebook ORDER BY notebook_dateTimeLastEdited DESC")
    fun getAllNotebooksOrderedTimeEditedDESC(): LiveData<List<Notebook>>

    @Query("SELECT * FROM notebook WHERE notebook_name = :notebook_name")
    suspend fun getNotebookByName(
        notebook_name: String) : Notebook

    @Query("SELECT * FROM notebook " +
            "WHERE notebook_dateTimeOfCreation = :notebook_dateOfCreation")
    suspend fun getNotebookByDateOfCreation(
        notebook_dateOfCreation: String) : Notebook

    @Query("SELECT * FROM notebook " +
            "WHERE notebook_dateTimeLastEdited = :notebook_dateTimeLastEdited")
    suspend fun getNotebookByDateLastEdited(
        notebook_dateTimeLastEdited: String) : Notebook



    //Deletion queries

    @Delete
    suspend fun deleteNotebook(notebook: Notebook)


    @Query("DELETE FROM notebook " +
            "WHERE notebook_name = :notebook_name")
    suspend fun deleteByName(notebook_name: String)
}