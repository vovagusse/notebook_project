package com.example.notebook_project.ui.editor

import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentEditBinding
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import java.util.Date


class EditFragment : Fragment() {


    private var _binding: FragmentEditBinding? = null
    private val vb get() = _binding!!
    private val args by navArgs<EditFragmentArgs>()
    private lateinit var notebookViewModel: NotebookViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val root: View = vb.root

        notebookViewModel = ViewModelProvider(this)[NotebookViewModel::class.java]

        val name : String = args.currentNotebook.notebook_name
        vb.etEditTitle.setText(args.currentNotebook.notebook_name)
        vb.etEditBody.setText( notebookViewModel.getNotebookByName(name) )

        vb.fabEditorSave.setOnClickListener{
            val filename = args.currentNotebook.uri
            val body = vb.etEditBody.text.toString()
            notebookViewModel.saveFile(filename, body)
            updateNotebook(filename)
        }
        vb.fabEditorSave.setOnLongClickListener{
            val filename = args.currentNotebook.uri
            val body = vb.etEditBody.text.toString()
            notebookViewModel.saveFile(filename, body)
            val notebook_updated = updateNotebook(filename)
            notebook_updated?.let {
                val action = EditFragmentDirections.actionEditFragmentToNavPreview(it)
                findNavController().navigate(action)
            }
            true
        }

        return root
    }

    private fun updateNotebook(uri: String) : Notebook?{
        val currentDateTime : Date = Calendar.getInstance().time
        val id = args.currentNotebook.id
//        val uri = args.currentNotebook.uri
        val name = vb.etEditTitle.text.toString()

        val errMessage = this
            .context?.resources?.getString(R.string.err_no_notebook_title)
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this.context, "$errMessage", Toast.LENGTH_LONG).show()
            return null
        }
        val date_created = args.currentNotebook.dateTimeOfCreation
        val date_edited = currentDateTime.toString()

        val notebook_updated = Notebook(
            id, uri, name, date_created, date_edited
        )
        notebookViewModel.upsertNotebook(notebook_updated)
        val success_edit = this.context?.resources?.getString(R.string.success_edit_notebook)
        Toast.makeText(requireContext(), success_edit, Toast.LENGTH_LONG).show()

        return notebook_updated
    }
}