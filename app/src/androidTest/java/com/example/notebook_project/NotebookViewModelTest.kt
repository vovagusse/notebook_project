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

    fun printTest(testName: String, begin: Boolean){
        println()
        println("===============================================")
        val spoopty_doot = if (begin) "BEGIN" else "END"
        println("(${spoopty_doot})  test: $testName")
        println("===============================================")
    }

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
        printTest("testUpsertNotebook", true)
        val nb = Notebook(
            0,
            "note.md",
            "Note",
            Date(1717149000),
            Date(1717149300)
        )
        val d = "blah\\ *blah*\\ ***blah***"
        val stuff = nb.copy(id=1, notebook_name ="Note (1)", uri="note (1).md")
        viewModel.upsertNotebook(stuff, d)

        val all = viewModel.notebookUiModel.getOrAwaitValue().notebooks
        assertThat(all).contains(stuff)
        printTest("testUpsertNotebook", false)
    }
    @Test
    fun testGetNotebookByName() {
        printTest("testGetNotebookByName", true)
        val nb = Notebook(
            0,
            "note.md",
            "Note",
            Date(1717149000),
            Date(1717149300)
        )
        val d = "blah\\ *blah*\\ ***blah***"

        viewModel.upsertNotebook(nb.copy(id=1, notebook_name ="Note (1)", uri="note (1).md"), d)
        viewModel.upsertNotebook(nb.copy(id=2, notebook_name ="Note (2)", uri="note (2).md"), d)
        viewModel.upsertNotebook(nb.copy(id=3, notebook_name ="Note (3)", uri="note (3).md"), d)
        viewModel.upsertNotebook(nb.copy(id=4, notebook_name ="Note (4)", uri="note (4).md"), d)
        viewModel.upsertNotebook(nb.copy(id=5, notebook_name ="Note (5)", uri="note (5).md"), d)

        val found_query = viewModel.getNotebookByName("Note (2)")
        assertThat(found_query)
            .isNotEqualTo(null)
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

        viewModel.upsertNotebook(nb.copy(id=1, notebook_name ="Note (1)", uri="note (1).md"), d)
        viewModel.upsertNotebook(nb.copy(id=2, notebook_name ="Note (2)", uri="note (2).md"), d)
        viewModel.upsertNotebook(nb.copy(id=3, notebook_name ="Note (3)", uri="note (3).md"), d)
        viewModel.upsertNotebook(nb.copy(id=4, notebook_name ="Note (4)", uri="note (4).md"), d)
        viewModel.upsertNotebook(nb.copy(id=5, notebook_name ="Note (5)", uri="note (5).md"), d)

        viewModel.deleteNotebook("Note (2)")

        val all = viewModel.notebookUiModel.getOrAwaitValue().notebooks
        assertThat(all.find{it.notebook_name == "Note (2)"})
            .isEqualTo(null)
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
        viewModel.upsertNotebook(nb.copy(id=1), d)
        viewModel.upsertNotebook(nb.copy(id=2), d)
        viewModel.upsertNotebook(nb.copy(id=3), d)
        viewModel.upsertNotebook(nb.copy(id=4), d)
        viewModel.upsertNotebook(nb.copy(id=5), d)
        // [0; 4]
        val position = 3
        val all = viewModel.notebookUiModel.getOrAwaitValue().notebooks
        val notebook_at_pos = all[position]
        val query_notebook_at_pos = viewModel.getNotebookByPosition(position)
        assertThat(notebook_at_pos).isEqualTo(query_notebook_at_pos)
    }
    @Test
    fun renameNotebook() {
        val old_uri: String = "note.md"
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
        assertThat(toSearch == all.find{
            it.notebook_name == toSearch.notebook_name
                    && it.uri == toSearch.uri
        }).isTrue()
    }
    @Test
    fun testChangeSortOrder(){
        viewModel.changeSortOrder(SortOrder.ASCENDING)
        var state = viewModel.notebookUiModel.getOrAwaitValue().sortOrder
        assertThat(state).isEqualTo(SortOrder.ASCENDING)
        viewModel.changeSortOrder(SortOrder.DESCENDING)
        state = viewModel.notebookUiModel.getOrAwaitValue().sortOrder
        assertThat(state).isEqualTo(SortOrder.DESCENDING)
    }
    @Test
    fun testChangeSortParam(){
        viewModel.changeSortParam (SortingParameter.BY_NAME)
        var state = viewModel.notebookUiModel.getOrAwaitValue().sortParam
        assertThat(state).isEqualTo(SortingParameter.BY_NAME)
        viewModel.changeSortParam(SortingParameter.BY_DATE_LAST_EDITED)
        state = viewModel.notebookUiModel.getOrAwaitValue().sortParam
        assertThat(state).isEqualTo(SortingParameter.BY_DATE_LAST_EDITED)
        viewModel.changeSortParam(SortingParameter.BY_DATE_OF_CREATION)
        state = viewModel.notebookUiModel.getOrAwaitValue().sortParam
        assertThat(state).isEqualTo(SortingParameter.BY_DATE_OF_CREATION)
    }
    @Test
    fun testChangeStorageType(){
        viewModel.changeStorageType (StorageType.INTERNAL)
        var state = viewModel.notebookUiModel.getOrAwaitValue().storageType
        assertThat(state).isEqualTo(StorageType.INTERNAL)
        viewModel.changeStorageType (StorageType.EXTERNAL)
        state = viewModel.notebookUiModel.getOrAwaitValue().storageType
        assertThat(state).isEqualTo(StorageType.EXTERNAL)
    }
    @Test
    fun changeFolder(){
        val folder = "folder1"
        viewModel.changeFolder(folder)
        val vm_folder = viewModel.getFolder()
        assertThat(vm_folder).isEqualTo(folder)
    }
    @Test
    fun testChangeTheme(){
        viewModel.changeTheme (UserTheme.SYSTEM)
        var state = viewModel.notebookUiModel.getOrAwaitValue().theme
        assertThat(state).isEqualTo(UserTheme.SYSTEM)
        viewModel.changeTheme (UserTheme.DARK)
        state = viewModel.notebookUiModel.getOrAwaitValue().theme
        assertThat(state).isEqualTo(UserTheme.DARK)
        viewModel.changeTheme (UserTheme.LIGHT)
        state = viewModel.notebookUiModel.getOrAwaitValue().theme
        assertThat(state).isEqualTo(UserTheme.LIGHT)
    }
}