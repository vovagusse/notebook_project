package com.example.notebook_project.ui.editor

import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentEditorBinding
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.entities.NotebookMetadata
import com.example.notebook_project.db.viewmodels.NotebookViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import java.util.Date
import java.util.concurrent.Executors


class EditorFragment : Fragment() {
    private lateinit var et_body: EditText
    private lateinit var et_title: EditText
    private var _binding: FragmentEditorBinding? = null
    private val vb
        get() = _binding!!
    private lateinit var notebookViewModel : NotebookViewModel
    private var id : Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorBinding.inflate(inflater, container, false)
        val root: View = vb.root
        val papaContext = container?.context

        notebookViewModel = ViewModelProvider(this)[NotebookViewModel::class.java]

        //                contentEditor is an <include> tag id lol
        val fab_save = vb.contentEditor.fabEditorSave
        fab_save.setOnClickListener{
            insertNewNotebook()
        }

        et_title = vb.contentEditor.etEditorTitle
        et_body = vb.contentEditor.etEditorBody
        val markwon = this.context?.let { Markwon.create(it) }
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

        //new file and new record in db
        val currentDateTime : Date = Calendar.getInstance().time
        val notebookObj = Notebook(
            uri = notebookFilename,
            metadata = NotebookMetadata(
                notebook_name = notebookTitle,
                dateTimeOfCreation = currentDateTime.toString(),
                dateTimeLastEdited = currentDateTime.toString()
            )
        )
        notebookViewModel.addNotebook(notebookObj)

        val goodMessage = this.context?.resources?.getString(R.string.success_add_notebook)
        Toast.makeText(this.context, "$goodMessage", Toast.LENGTH_LONG).show()
        return true
}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
