package com.example.notebook_project.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.notebook_project.databinding.FragmentEditorBinding
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.viewmodels.NotebookViewModel
import io.noties.markwon.Markwon
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import java.util.concurrent.Executors


class EditorFragment : Fragment() {
    private lateinit var et_body: EditText
    private lateinit var et_title: EditText
    private var _binding: FragmentEditorBinding? = null
    private val vb
        get() = _binding!!
    private lateinit var notebookViewModel : NotebookViewModel


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
            insertDataToDatabase()
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

    private fun insertDataToDatabase() {
//        TODO("Not yet implemented")
        Toast.makeText(this.context, "not implemented :3", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
