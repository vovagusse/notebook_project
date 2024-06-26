package com.example.notebook_project.ui.editor

import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentAddBinding
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.NotebookRepository_Impl
import com.example.notebook_project.db.repository.UserPreferencesRepository_Impl
import com.example.notebook_project.db.repository.dataStore
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.db.viewmodel.NotebookViewModelFactory
import com.example.notebook_project.util.EditTextFormatting
import com.example.notebook_project.util.makeFileName
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
//        val root: View = vb.root
//        val papaContext = container?.context

        notebookViewModel = ViewModelProvider(requireActivity(),
            NotebookViewModelFactory(
                NotebookRepository_Impl.getInstance(requireActivity()),
                UserPreferencesRepository_Impl(
                    requireActivity().dataStore,
                ),
                requireActivity().application
            )
        )[NotebookViewModel::class.java]


        var current_nb : Notebook?
        vb.fabAddSave.setOnClickListener{
            insertNewNotebook()
        }
        vb.fabAddSave.setOnLongClickListener{
            insertNewNotebook()
            true
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

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    goHome()
                }
            }
            )

        //bar buttons
        val frm = EditTextFormatting()
        vb.tbAddBold.setOnClickListener {
            frm.formatBold(vb.etAddBody)
        }
        vb.tbAddItalic.setOnClickListener {
            frm.formatItalic(vb.etAddBody)
        }
        vb.tbAddUnderlined.setOnClickListener {
            frm.formatUnderlined(vb.etAddBody)
        }
        vb.tbAddCodeInline.setOnClickListener {
            frm.formatCodeInline(vb.etAddBody)
        }
        vb.tbAddCodeBlock.setOnClickListener {
            frm.formatCodeBlock(vb.etAddBody)
        }
        return vb.root
    }

    private fun insertNewNotebook() : Boolean {
//        Toast.makeText(this.context, "not implemented :3", Toast.LENGTH_SHORT).show()
        val notebookTitle = et_title.text.toString().removeSurrounding(" ")

        val errMessage = this
            .context?.resources?.getString(R.string.err_no_notebook_title)
        val alreadyExists = resources.getString(R.string.err_already_exists)
        if (TextUtils.isEmpty(notebookTitle)){
            Toast.makeText(this.context, "$errMessage", Toast.LENGTH_LONG).show()
            return false
        }
        val search = notebookViewModel.getNotebookByName(notebookTitle)
        if (search!=null){
            Toast.makeText(this.context, "$alreadyExists", Toast.LENGTH_LONG).show()
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
        notebookViewModel.upsertNotebook(notebookObj, notebookBody)

        val goodMessage = this.context?.resources?.getString(R.string.success_add_notebook)
        Toast.makeText(this.context, "$goodMessage", Toast.LENGTH_LONG).show()

        val action = AddFragmentDirections.actionAddFragmentToNavPreview(notebookObj)
        findNavController().navigate(action)
        return true
    }

    private fun goHome(){
        val action = AddFragmentDirections.actionAddFragmentToNavHome()
        action.let { findNavController().navigate(it) }
    }
}
