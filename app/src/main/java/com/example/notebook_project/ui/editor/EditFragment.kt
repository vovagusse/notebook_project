package com.example.notebook_project.ui.editor

import android.content.Context
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
import androidx.datastore.preferences.preferencesDataStore
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
import java.util.Date



class EditFragment : Fragment() {


    private var _binding: FragmentEditBinding? = null
    private val vb get() = _binding!!
    private val args by navArgs<EditFragmentArgs>()
    private lateinit var notebookViewModel: NotebookViewModel

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

        notebookViewModel = ViewModelProvider(requireActivity(),
            NotebookViewModelFactory(
                NotebookRepository.getInstance(requireActivity()),
                UserPreferencesRepository(
                    requireActivity().dataStore,
                )
            )
        )[NotebookViewModel::class.java]

        val name : String = args.currentNotebook.notebook_name
        vb.etEditTitle.setText(args.currentNotebook.notebook_name)
        vb.etEditBody.setText( notebookViewModel.openNotebook(name) )

        vb.fabEditorSave.setOnClickListener{
            val filename = args.currentNotebook.uri
            val body = vb.etEditBody.text.toString()
            notebookViewModel.saveNotebook(filename, body)
            updateNotebook(filename)
        }
        vb.fabEditorSave.setOnLongClickListener{
            val filename = args.currentNotebook.uri
            val body = vb.etEditBody.text.toString()
            notebookViewModel.saveNotebook(filename, body)
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
        val body = vb.etEditBody.text.toString()

        val errMessage = this
            .context?.resources?.getString(R.string.err_no_notebook_title)
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this.context, "$errMessage", Toast.LENGTH_LONG).show()
            return null
        }
        val date_created = args.currentNotebook.dateTimeOfCreation
        val date_edited = currentDateTime

        val notebook_updated = Notebook(
            id, uri, name, date_created, date_edited
        )
        notebookViewModel.upsertNotebook(notebook_updated)
        notebookViewModel.saveNotebook(uri, body)
        val success_edit = this.context?.resources?.getString(R.string.success_edit_notebook)
        Toast.makeText(requireContext(), success_edit, Toast.LENGTH_LONG).show()

        return notebook_updated
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_edit_appbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.appbar_menu_delete_button -> {
            notebookViewModel.deleteNotebookByName(args.currentNotebook.notebook_name)
            val action = EditFragmentDirections.actionEditFragmentToNavHome()
            findNavController().navigate(action)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}