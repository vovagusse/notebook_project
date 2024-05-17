package com.example.notebook_project.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date


@Parcelize
@Entity(tableName = "notebook")
data class Notebook (
    @PrimaryKey(true)
    @ColumnInfo("id")
    val id: Long = 0,

    @ColumnInfo("uri")
    val uri: String,

    @ColumnInfo("notebook_name")
    val notebook_name: String,

    @ColumnInfo("notebook_dateTimeOfCreation")
    val dateTimeOfCreation: Date,

    @ColumnInfo("notebook_dateTimeLastEdited")
    val dateTimeLastEdited: Date
) : Parcelable