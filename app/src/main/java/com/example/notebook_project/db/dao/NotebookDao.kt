package com.example.notebook_project.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notebook_project.db.NotebookTuple
import com.example.notebook_project.db.entities.NotebookEntity


@Dao
interface NotebookDao {
    @Insert(entity = NotebookEntity::class)
    fun insertNewNotebook(notebook: NotebookEntity)

    @Query("SELECT id, " +
            "uri, metadatanotebook_name, " +
            "metadatadescription, " +
            "metadatadateTimeOfCreation, " +
            "metadatadateTimeLastEdited " +
            "FROM notebook")
    fun getAllNotebooks(): List<NotebookTuple>

    @Query("SELECT id, " +
            "uri, metadatanotebook_name, " +
            "metadatadescription, " +
            "metadatadateTimeOfCreation, " +
            "metadatadateTimeLastEdited " +
            "FROM notebook " +
            "WHERE id = :notebook_id")
    fun getNotebookById(
        notebook_id: Long): NotebookTuple

    @Query("SELECT id, " +
            "uri, metadatanotebook_name, " +
            "metadatadescription, " +
            "metadatadateTimeOfCreation, " +
            "metadatadateTimeLastEdited " +
            "FROM notebook " +
            "WHERE metadatanotebook_name = :notebook_name")
    fun getNotebookByName(
        notebook_name: String) : NotebookTuple

    @Query("SELECT id, " +
            "uri, metadatanotebook_name, " +
            "metadatadescription, " +
            "metadatadateTimeOfCreation, " +
            "metadatadateTimeLastEdited " +
            "FROM notebook " +
            "WHERE metadatadateTimeOfCreation " +
            "= :notebook_dateOfCreation")
    fun getNotebookByDateOfCreation(
        notebook_dateOfCreation: String) : NotebookTuple

    @Query("SELECT id, " +
            "uri, metadatanotebook_name, " +
            "metadatadescription, " +
            "metadatadateTimeOfCreation, " +
            "metadatadateTimeLastEdited " +
            "FROM notebook " +
            "WHERE metadatadateTimeLastEdited " +
            "= :notebook_dateTimeLastEdited")
    fun getNotebookByDateLastEdited(
        notebook_dateTimeLastEdited: String) : NotebookTuple


    @Query("DELETE " +
            "FROM notebook " +
            "WHERE id = :notebook_id")
    fun deleteById(notebook_id: Long)

    @Query("DELETE " +
            "FROM notebook " +
            "WHERE metadatanotebook_name" +
            " = :notebook_name")
    fun deleteByName(notebook_name: String)
}