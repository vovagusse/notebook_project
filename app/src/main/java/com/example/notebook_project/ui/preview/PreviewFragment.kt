package com.example.notebook_project.ui.preview

import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentPreviewBinding
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository
import com.example.notebook_project.db.repository.dataStore
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.db.viewmodel.NotebookViewModelFactory



class PreviewFragment : Fragment() {

    val args by navArgs<PreviewFragmentArgs>()
    private var _binding: FragmentPreviewBinding? = null
    private val vb get() = _binding!!
    private lateinit var notebookViewModel: NotebookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        val root: View = vb.root
//        val appContext = requireActivity().application
        notebookViewModel = ViewModelProvider(requireActivity(),
            NotebookViewModelFactory(
                NotebookRepository.getInstance(requireActivity()),
                UserPreferencesRepository(
                    requireActivity().dataStore,
                    requireActivity()
                )
            )
        )[NotebookViewModel::class.java]

        vb.tvPreviewNotebookName.text = args.currentNotebook.notebook_name
        val date_created_txt = "${resources.getString(R.string.created_on)}: ${args.currentNotebook.dateTimeLastEdited}"
        val date_edited_txt = "${resources.getString(R.string.latest_changes)}: ${args.currentNotebook.dateTimeLastEdited}"
        vb.tvPreviewDateCreated.text = date_created_txt
        vb.tvPreviewDateEdited.text = date_edited_txt
        vb.tvPreviewNotebookBody.text = notebookViewModel.openNotebook(args.currentNotebook.uri)
        vb.tvPreviewNotebookBody.movementMethod = ScrollingMovementMethod()

        vb.fabEdit.setOnClickListener{
            val action = PreviewFragmentDirections.actionNavPreviewToEditFragment(args.currentNotebook)
            findNavController().navigate(action)
        }
//        FragmentPreviewBinding.inflate(R.menu.fragment_edit_appbar_menu)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_edit_appbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.appbar_menu_delete_button -> {
            notebookViewModel.deleteNotebookByName(args.currentNotebook.notebook_name)
            val action = PreviewFragmentDirections.actionNavPreviewToNavHome()
            findNavController().navigate(action)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

}