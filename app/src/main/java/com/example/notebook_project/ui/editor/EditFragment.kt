package com.example.notebook_project.ui.editor

import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentEditBinding
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository
import com.example.notebook_project.db.repository.dataStore
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.db.viewmodel.NotebookViewModelFactory
import com.example.notebook_project.util.makeFileName
import java.util.Date



class EditFragment : Fragment() {


    private var _binding: FragmentEditBinding? = null
    private val vb get() = _binding!!
    private val args by navArgs<EditFragmentArgs>()
    private lateinit var NOTEBOOK_VIEW_MODEL: NotebookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        val root: View = vb.root

        NOTEBOOK_VIEW_MODEL = ViewModelProvider(requireActivity(),
            NotebookViewModelFactory(
                NotebookRepository.getInstance(requireActivity()),
                UserPreferencesRepository(
                    requireActivity().dataStore,
                ),
                requireActivity().application
            )
        )[NotebookViewModel::class.java]

        vb.etEditTitle.setText(args.currentNotebook.notebook_name)

        val name : String = args.currentNotebook.notebook_name
        vb.etEditBody.setText( NOTEBOOK_VIEW_MODEL.readNotebookByName(name) )
        vb.fabEditorSave.setOnClickListener{
            updateNotebook()
        }
        vb.fabEditorSave.setOnLongClickListener{
            val notebook_updated = updateNotebook()
            notebook_updated?.let {
                val action = EditFragmentDirections
                    .actionEditFragmentToNavPreview(it)
                findNavController().navigate(action)
            }
            true
        }

        return root
    }

    private fun updateNotebook() : Notebook?{
        var uri = args.currentNotebook.uri
        val currentDateTime : Date = Calendar.getInstance().time
        val id = args.currentNotebook.id
        val old_name = args.currentNotebook.notebook_name
        val name = vb.etEditTitle.text.toString()
        val body = vb.etEditBody.text.toString()

        val errMessage = this
            .context?.resources?.getString(R.string.err_no_notebook_title)
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this.context, "$errMessage", Toast.LENGTH_LONG).show()
            return null
        }

        if (old_name != name) {
            val new_uri = makeFileName(name)
            NOTEBOOK_VIEW_MODEL.renameNotebook(uri, new_uri)
            uri = new_uri
        }

        val date_created = args.currentNotebook.dateTimeOfCreation
        val date_edited = currentDateTime

        val notebook_updated = Notebook(
            id, uri, name, date_created, date_edited
        )
        NOTEBOOK_VIEW_MODEL.upsertNotebook(notebook_updated, body)
        val success_edit = this.context?.resources?.getString(R.string.success_edit_notebook)
        Toast.makeText(requireActivity(), success_edit, Toast.LENGTH_LONG).show()

        return notebook_updated
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_edit_appbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.appbar_menu_delete_button -> {
            NOTEBOOK_VIEW_MODEL.deleteNotebook(args.currentNotebook.notebook_name)
            val action = EditFragmentDirections.actionEditFragmentToNavHome()
            findNavController().navigate(action)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}