package com.example.notebook_project.ui.preview

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentEditBinding
import com.example.notebook_project.databinding.FragmentPreviewBinding
import com.example.notebook_project.db.viewmodel.NotebookViewModel

class PreviewFragment : Fragment() {

    val args by navArgs<PreviewFragmentArgs>()
    private var _binding: FragmentPreviewBinding? = null
    private val vb get() = _binding!!
    private lateinit var notebookViewModel: NotebookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        val root: View = vb.root
        notebookViewModel = ViewModelProvider(this)[NotebookViewModel::class.java]

        vb.tvPreviewNotebookName.setText(args.currentNotebook.notebook_name)
        vb.tvPreviewDateCreated.setText(args.currentNotebook.dateTimeOfCreation)
        vb.tvPreviewDateEdited.setText(args.currentNotebook.dateTimeLastEdited)
        vb.tvPreviewNotebookBody.setText( notebookViewModel.getNotebookByName(args.currentNotebook.notebook_name) )

        return root
    }
}