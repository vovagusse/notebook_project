package com.example.notebook_project.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentHomeBinding
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.viewmodels.NotebookViewModel
import com.example.notebook_project.ui.home.adapter.NotebookRecyclerViewAdapter
import com.example.notebook_project.ui.home.rwdecoration.GridSpacingItemDecoration

class HomeFragment : Fragment(), SelectListener{

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

        notebookViewModel = ViewModelProvider(this)[NotebookViewModel::class.java]
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        val papaContext = container?.context

        val adapter = papaContext?.let { NotebookRecyclerViewAdapter(emptyList<Notebook>(), it, this) }
        notebookViewModel._notebooks.observe(viewLifecycleOwner, Observer {notebook ->
            adapter?.setData(notebook)
        })

        val rw = vb.rvNotebooks
        rw.adapter = adapter
        rw.layoutManager = GridLayoutManager(papaContext, 2)
        rw.addItemDecoration(GridSpacingItemDecoration(2, 16, true))

        val fab_createNewNotebook = vb.fabCreateNewNotebook
        fab_createNewNotebook.setOnClickListener{
            findNavController().navigate(R.id.action_nav_home_to_editorFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
//            homeViewModel?.changeSpansTo(3)
//        }
//        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            homeViewModel?.changeSpansTo(2)
//        }
    }

    override fun onItemClicked(notebook: Notebook) {
        findNavController().navigate(R.id.action_nav_home_to_previewFragment)
    }
}