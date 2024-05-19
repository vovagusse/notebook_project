package com.example.notebook_project.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentHomeBinding
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository
import com.example.notebook_project.db.repository.dataStore
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.db.viewmodel.NotebookViewModelFactory
import com.example.notebook_project.ui.home.adapter.NotebookRecyclerViewAdapter
import com.example.notebook_project.ui.home.rwdecoration.GridSpacingItemDecoration




class HomeFragment : Fragment(){


    private var _binding: FragmentHomeBinding? = null
    private val vb get() = _binding!!
    private lateinit var notebookViewModel: NotebookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = vb.root

        notebookViewModel = ViewModelProvider(requireActivity(),
            NotebookViewModelFactory(
                NotebookRepository.getInstance(requireActivity()),
                UserPreferencesRepository(
                    requireActivity().dataStore,
                    requireActivity()
                )
            )
        )[NotebookViewModel::class.java]
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        val papaContext = container?.context

        val adapter = papaContext?.let {
            NotebookRecyclerViewAdapter(it)
        }
//        notebookViewModel.getAllNotebooks()
//        notebookViewModel.print()
        notebookViewModel.notebookUiModel.observe(viewLifecycleOwner) {
            adapter?.setListContent(it.notebooks)
        }
        val rw = vb.rvNotebooks
        rw.setAdapter(adapter)
        rw.layoutManager = GridLayoutManager(papaContext, 2)
        rw.addItemDecoration(GridSpacingItemDecoration(2, 16, true))

        val fab_createNewNotebook = vb.fabCreateNewNotebook
        fab_createNewNotebook.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_to_addFragment)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}