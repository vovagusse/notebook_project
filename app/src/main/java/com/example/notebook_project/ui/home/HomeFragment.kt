package com.example.notebook_project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentHomeBinding
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.NotebookRepository_Impl
import com.example.notebook_project.db.repository.UserPreferencesRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository_Impl
import com.example.notebook_project.db.repository.dataStore
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.db.viewmodel.NotebookViewModelFactory
import com.example.notebook_project.ui.home.adapter.RecyclerViewInterface
import com.example.notebook_project.ui.home.adapter.NotebookRecyclerViewAdapter
import com.example.notebook_project.ui.home.rwdecoration.GridSpacingItemDecoration




class HomeFragment : Fragment(), RecyclerViewInterface {


    private var _binding: FragmentHomeBinding? = null
    private val vb get() = _binding!!
    private lateinit var NOTEBOOK_VIEW_MODEL: NotebookViewModel
    private lateinit var rw: RecyclerView

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

        NOTEBOOK_VIEW_MODEL = ViewModelProvider(requireActivity(),
            NotebookViewModelFactory(
                NotebookRepository_Impl.getInstance(requireActivity()),
                UserPreferencesRepository_Impl(
                    requireActivity().dataStore,
                ),
                requireActivity().application
            )
        )[NotebookViewModel::class.java]
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        val papaContext = container?.context

        val adapter = papaContext?.let {
            NotebookRecyclerViewAdapter(it, this)
        }
//        notebookViewModel.getAllNotebooks()
//        notebookViewModel.print()
        NOTEBOOK_VIEW_MODEL.notebookUiModel.observe(viewLifecycleOwner) {
            adapter?.setListContent(it.notebooks)
        }
        rw = vb.rvNotebooks
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

    override fun onContextButtonDelete(position: Int) {
        val found = NOTEBOOK_VIEW_MODEL.getNotebookByPosition(position)
        found?.notebook_name?.let {
            NOTEBOOK_VIEW_MODEL.deleteNotebook(it)
        }
    }

    override fun readNotebookBody(position: Int) : String{
        val found = NOTEBOOK_VIEW_MODEL.getNotebookByPosition(position)
        if (found != null){
            return NOTEBOOK_VIEW_MODEL.readNotebookByUri(found.uri)
        }
        return ""
    }


}