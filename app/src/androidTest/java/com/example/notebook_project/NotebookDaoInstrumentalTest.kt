package com.example.notebook_project

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import androidx.test.filters.MediumTest
import com.example.notebook_project.db.NotebookDatabase
import com.example.notebook_project.db.dao.NotebookDao
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.util.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
@MediumTest
class NotebookDaoInstrumentalTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: NotebookDatabase
    private lateinit var dao : NotebookDao

    @Before
    fun setup(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(
                context,
                NotebookDatabase::class.java
            )
            .allowMainThreadQueries()
            .build()
        dao = db.getNotebookDao()
    }

    @Test
    fun testUpsert() = runBlocking {
        val entry = Notebook(0,"text.md", "Text",
            Date(1717149000L), Date(1717149300L))
        dao.upsertNewNotebook(entry)
        val allItems : List<Notebook> = dao
            .getAllNotebooks()
            .asLiveData()
            .getOrAwaitValue()
        assertThat(allItems).isNotEmpty()
    }

    @Test
    fun testGetAll() = runBlocking{
        val entry = Notebook(
            0,
            "text.md",
            "Text",
            Date(1717149000L),
            Date(1717149300L)
        )
        val dataset = listOf(
            entry.copy(id=1),
            entry.copy(id=2),
            entry.copy(id=3),
            entry.copy(id=4),
            entry.copy(id=5),
        )
        for (notebook in dataset) {
            dao.upsertNewNotebook(notebook)
        }

        val allItems : List<Notebook> = dao
            .getAllNotebooks()
            .asLiveData()
            .getOrAwaitValue()

        assertThat(allItems).isEqualTo(dataset)
    }

    @Test
    fun testDelete() = runBlocking {
        val entry = Notebook(
            0,
            "text.md",
            "Text",
            Date(1717149000L),
            Date(1717149300L)
        )
        dao.upsertNewNotebook(entry)
        dao.deleteNotebook(entry)

        val allItems : List<Notebook> = dao
            .getAllNotebooks()
            .asLiveData()
            .getOrAwaitValue()

        assertThat(allItems).doesNotContain(entry)
    }

    @Test
    fun testDeleteByName() = runBlocking {
        val entry = Notebook(
            0,
            "text.md",
            "Text",
            Date(1717149000L),
            Date(1717149300L)
        )
        dao.upsertNewNotebook(entry)
        dao.deleteByName(entry.notebook_name)

        val allItems : List<Notebook> = dao
            .getAllNotebooks()
            .asLiveData()
            .getOrAwaitValue()

        assertThat(allItems).doesNotContain(entry)
    }

    @After
    fun closeDb(){
        db.close()
    }
}