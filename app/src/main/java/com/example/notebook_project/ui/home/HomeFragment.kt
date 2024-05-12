package com.example.notebook_project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.notebook_project.databinding.FragmentHomeBinding
import com.example.notebook_project.db.NotebookTuple
import com.example.notebook_project.notebookTemplate
import com.example.notebook_project.ui.home.adapter.NotebookRecyclerViewAdapter
import com.example.notebook_project.ui.home.rwdecoration.GridSpacingItemDecoration
import com.example.notebook_project.db.dao.NotebookDao as NotebookDao

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val vb get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = vb.root

//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        val papaContext = container?.context
        var my_objects = notebookTemplate()
        my_objects.addAll(notebookTemplate())
        my_objects.addAll(notebookTemplate())
        my_objects.addAll(notebookTemplate())
        val rw = vb.rvNotebooks
        val my_adapter = papaContext?.let{
//            NotebookRecyclerViewAdapter(mutableListOf<NotebookTuple>(), it)
            NotebookRecyclerViewAdapter(my_objects, it)
        }
        rw.adapter = my_adapter
        rw.layoutManager = GridLayoutManager(papaContext, 2)
        homeViewModel.notebooks.observe(viewLifecycleOwner) {
            _binding!!.rvNotebooks.adapter?.notifyDataSetChanged()
        }
        rw.addItemDecoration(GridSpacingItemDecoration(2, 16, false))
//        rw.addItemDecoration()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}