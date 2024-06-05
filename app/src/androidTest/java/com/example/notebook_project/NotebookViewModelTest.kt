package com.example.notebook_project

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.FakeNotebookRepository
import com.example.notebook_project.db.repository.FakeUserPreferencesRepository
import com.example.notebook_project.db.repository.SortOrder
import com.example.notebook_project.db.repository.SortingParameter
import com.example.notebook_project.db.repository.StorageType
import com.example.notebook_project.db.repository.UserTheme
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.util.getOrAwaitValue
import com.example.notebook_project.util.makeFileName
import com.google.common.truth.Truth.assertThat

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date


@RunWith(AndroidJUnit4::class)
@MediumTest
class NotebookViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var viewModel: NotebookViewModel
    @Before
    fun setUp() {
        val fakeContext =  ApplicationProvider.getApplicationContext<Context>()
        viewModel = NotebookViewModel(
            FakeNotebookRepository.getInstance(),
            FakeUserPreferencesRepository(),
            fakeContext as Application
        )
    }
    @Test
    fun testUpsertNotebook(){
        val nb = Notebook(
            0,
            "note.md",
            "Note",
            Date(1717149000),
            Date(1717149300)
        )
        val d = "blah\\ *blah*\\ ***blah***"
        val stuff = nb.copy(id=1, notebook_name ="Note (1)", uri="note (1).md")
        viewModel.notebookUiModel.getOrAwaitValue()
        viewModel.upsertNotebook(stuff, d)
        val all = viewModel.notebookUiModel.getOrAwaitValue().notebooks
        assertThat(all).contains(stuff)
    }
    @Test
    fun testGetNotebookByName() {
        val nb = Notebook(
            0,
            "note.md",
            "Note",
            Date(1717149000),
            Date(1717149300)
        )
        val d = "blah\\ *blah*\\ ***blah***"
        val two = nb.copy(id=2, notebook_name ="Note (2)", uri="note (2).md")
        viewModel.upsertNotebook(nb.copy(id=1, notebook_name ="Note (1)", uri="note (1).md"), d)
        viewModel.upsertNotebook(two, d)
        viewModel.upsertNotebook(nb.copy(id=3, notebook_name ="Note (3)", uri="note (3).md"), d)
        viewModel.upsertNotebook(nb.copy(id=4, notebook_name ="Note (4)", uri="note (4).md"), d)
        viewModel.upsertNotebook(nb.copy(id=5, notebook_name ="Note (5)", uri="note (5).md"), d)
        viewModel.notebookUiModel.getOrAwaitValue()
        val found_query = viewModel.getNotebookByName(two.notebook_name)
        assertThat(found_query).isEqualTo(two)
    }
    @Test
    fun testDeleteNotebook(){
        val nb = Notebook(
            0,
            "note.md",
            "Note",
            Date(1717149000),
            Date(1717149300)
        )
        val d = "blah\\ *blah*\\ ***blah***"
        val two = nb.copy(id=2, notebook_name ="Note (2)", uri="note (2).md")
        viewModel.upsertNotebook(nb.copy(id=1, notebook_name ="Note (1)", uri="note (1).md"), d)
        viewModel.upsertNotebook(two, d)
        viewModel.upsertNotebook(nb.copy(id=3, notebook_name ="Note (3)", uri="note (3).md"), d)
        viewModel.upsertNotebook(nb.copy(id=4, notebook_name ="Note (4)", uri="note (4).md"), d)
        viewModel.upsertNotebook(nb.copy(id=5, notebook_name ="Note (5)", uri="note (5).md"), d)
        viewModel.deleteNotebook(two.notebook_name)
        viewModel.notebookUiModel.getOrAwaitValue()
        val foundQuery = viewModel.getNotebookByName(two.notebook_name).also {
            println(it)
        }
        assertThat(foundQuery).isNotEqualTo(two)
    }
    @Test
    fun testGetNotebookByPosition() {
        val nb = Notebook(
            0,
            "note.md",
            "Note",
            Date(1717149000),
            Date(1717149300)
        )
        val d = "blah\\ *blah*\\ ***blah***"
        viewModel.notebookUiModel.getOrAwaitValue()
        viewModel.upsertNotebook(nb.copy(id=1), d)
        viewModel.upsertNotebook(nb.copy(id=2), d)
        viewModel.upsertNotebook(nb.copy(id=3), d)
        viewModel.upsertNotebook(nb.copy(id=4), d)
        viewModel.upsertNotebook(nb.copy(id=5), d)
        viewModel.notebookUiModel.getOrAwaitValue()
        // [0; 4]
        val position = 3
        val all = viewModel.notebookUiModel.getOrAwaitValue().notebooks
        val notebook_at_pos = all[position]
        val query_notebook_at_pos = viewModel.getNotebookByPosition(position)
        viewModel.notebookUiModel.getOrAwaitValue()
        assertThat(notebook_at_pos).isEqualTo(query_notebook_at_pos)
    }
    @Test
    fun renameNotebook() {
        val old_uri = "note.md"
        val nb = Notebook(
            0,
            old_uri,
            "Note",
            Date(1717149000),
            Date(1717149300)
        )
        val data = "bla bla bla some text"
        viewModel.upsertNotebook(nb, data)
        val new_name = "Note (1)"
        viewModel.upsertNotebook(
            nb.copy(notebook_name = new_name),
            "$data and something else at the end"
        )
        val all = viewModel.notebookUiModel.getOrAwaitValue().notebooks
        val toSearch = Notebook(
            nb.id,
            makeFileName(new_name),
            new_name,
            Date(1717149000),
            Date(1717149300),
        )
        viewModel.notebookUiModel.getOrAwaitValue()
        assertThat(toSearch == all.find{
            it.notebook_name == toSearch.notebook_name
                    && it.uri == toSearch.uri
        }).isTrue()
    }
    @Test
    fun testChangeSortOrder(){
        var state = viewModel.notebookUiModel.getOrAwaitValue().sortOrder
        println(state)
        viewModel.changeSortOrder(SortOrder.ASCENDING)
        state = viewModel.notebookUiModel.getOrAwaitValue().sortOrder
        println(state)
        assertThat(state).isEqualTo(SortOrder.ASCENDING)

        state = viewModel.notebookUiModel.getOrAwaitValue().sortOrder
        println(state)
        viewModel.changeSortOrder(SortOrder.DESCENDING)
        state = viewModel.notebookUiModel.getOrAwaitValue().sortOrder
        println(state)
        assertThat(state).isEqualTo(SortOrder.DESCENDING)
    }
    @Test
    fun testChangeSortParam(){
        var state = viewModel.notebookUiModel.getOrAwaitValue().sortParam
        println(state)
        viewModel.changeSortParam (SortingParameter.BY_NAME)
        state = viewModel.notebookUiModel.getOrAwaitValue().sortParam
        println(state)
        assertThat(state).isEqualTo(SortingParameter.BY_NAME)

        state = viewModel.notebookUiModel.getOrAwaitValue().sortParam
        println(state)
        viewModel.changeSortParam(SortingParameter.BY_DATE_LAST_EDITED)
        state = viewModel.notebookUiModel.getOrAwaitValue().sortParam
        println(state)
        assertThat(state).isEqualTo(SortingParameter.BY_DATE_LAST_EDITED)

        state = viewModel.notebookUiModel.getOrAwaitValue().sortParam
        println(state)
        viewModel.changeSortParam(SortingParameter.BY_DATE_OF_CREATION)
        state = viewModel.notebookUiModel.getOrAwaitValue().sortParam
        println(state)
        assertThat(state).isEqualTo(SortingParameter.BY_DATE_OF_CREATION)
    }
    @Test
    fun testChangeStorageType(){
        viewModel.notebookUiModel.getOrAwaitValue().storageType
        viewModel.changeStorageType (StorageType.INTERNAL)
        var state = viewModel.notebookUiModel.getOrAwaitValue().storageType
        assertThat(state).isEqualTo(StorageType.INTERNAL)

        viewModel.notebookUiModel.getOrAwaitValue().storageType
        viewModel.changeStorageType (StorageType.EXTERNAL)
        state = viewModel.notebookUiModel.getOrAwaitValue().storageType
        assertThat(state).isEqualTo(StorageType.EXTERNAL)
    }
    @Test
    fun testChangeTheme(){
        viewModel.notebookUiModel.getOrAwaitValue()
        viewModel.changeTheme (UserTheme.SYSTEM)
        var state = viewModel.notebookUiModel.getOrAwaitValue().theme
        viewModel.notebookUiModel.getOrAwaitValue()
        assertThat(state).isEqualTo(UserTheme.SYSTEM)

        viewModel.notebookUiModel.getOrAwaitValue()
        viewModel.changeTheme (UserTheme.DARK)
        state = viewModel.notebookUiModel.getOrAwaitValue().theme
        viewModel.notebookUiModel.getOrAwaitValue()
        assertThat(state).isEqualTo(UserTheme.DARK)

        viewModel.notebookUiModel.getOrAwaitValue()
        viewModel.changeTheme (UserTheme.LIGHT)
        state = viewModel.notebookUiModel.getOrAwaitValue().theme
        viewModel.notebookUiModel.getOrAwaitValue()
        assertThat(state).isEqualTo(UserTheme.LIGHT)
    }
}


//@Test
//fun changeFolder(){
//    viewModel.notebookUiModel.getOrAwaitValue()
//    val folder = "folder1"
//    viewModel.changeFolder(folder)
//    val vm_folder = viewModel.getFolder()
//    viewModel.notebookUiModel.getOrAwaitValue()
//    assertThat(vm_folder).isEqualTo(folder)
//}