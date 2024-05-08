package com.example.notebook_project.db

import android.content.Context
import androidx.room.Room

object Dependencies {
    private lateinit var appContext: Context

    fun init(context: Context){
        this.appContext = context
    }

    private val appDatabase: AppDatabase by lazy{
        Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                "database.db"
            )
            .createFromAsset("room_article.db")
            .build()
    }
}