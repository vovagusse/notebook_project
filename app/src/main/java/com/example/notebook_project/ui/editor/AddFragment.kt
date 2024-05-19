package com.example.notebook_project.ui.editor

import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentAddBinding
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository
import com.example.notebook_project.db.repository.dataStore
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.db.viewmodel.NotebookViewModelFactory

import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import java.util.concurrent.Executors



class AddFragment : Fragment() {
    private lateinit var et_body: EditText
    private lateinit var et_title: EditText
    private var _binding: FragmentAddBinding? = null
    private val vb
        get() = _binding!!
    private lateinit var notebookViewModel : NotebookViewModel
    private var id : Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = vb.root
        val papaContext = container?.context

        notebookViewModel = ViewModelProvider(requireActivity(),
            NotebookViewModelFactory(
                NotebookRepository.getInstance(requireActivity()),
                UserPreferencesRepository(
                    requireActivity().dataStore,
                    requireActivity()
                )
            )
        )[NotebookViewModel::class.java]

        val fab_save = vb.fabAddSave
        fab_save.setOnClickListener{
            insertNewNotebook()
        }

        et_title = vb.etAddTitle
        et_body = vb.etAddBody
        val markwon = this.context?.let { Markwon
            .create(it)
        }
        val editor = markwon?.let { MarkwonEditor.create(it) }

        et_body.addTextChangedListener(editor?.let {
            MarkwonEditorTextWatcher
                .withPreRender(
                    it,
                    Executors.newCachedThreadPool(),
                    et_body
                )
        })

        return vb.root
    }

    private fun makeFileName(notebookName: String) : String {
        val spl = notebookName
            .lowercase()
            .removeSurrounding(" ")
            .replace(" ", "_") + ".md"
        return spl
    }

    private fun insertNewNotebook() : Boolean {
//        Toast.makeText(this.context, "not implemented :3", Toast.LENGTH_SHORT).show()
        val notebookTitle = et_title.text.toString()
        val errMessage = this
            .context?.resources?.getString(R.string.err_no_notebook_title)
        if (TextUtils.isEmpty(notebookTitle)){
            Toast.makeText(this.context, "$errMessage", Toast.LENGTH_LONG).show()
            return false
        }
        val notebookFilename = makeFileName(notebookTitle)
        val notebookBody = vb.etAddBody.text.toString()

        //new file and new record in db
        val currentDateTime = Calendar.getInstance().time
        val notebookObj =
            Notebook(
                uri = notebookFilename,
                notebook_name = notebookTitle,
                dateTimeOfCreation = currentDateTime,
                dateTimeLastEdited = currentDateTime
            )
        notebookViewModel.upsertNotebook(notebookObj)
        notebookViewModel.saveNotebook(notebookFilename, notebookBody)

        val goodMessage = this.context?.resources?.getString(R.string.success_add_notebook)
        Toast.makeText(this.context, "$goodMessage", Toast.LENGTH_LONG).show()

        val action = AddFragmentDirections.actionAddFragmentToNavPreview(notebookObj)
        findNavController().navigate(action)

        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
