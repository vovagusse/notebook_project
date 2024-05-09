package com.example.notebook_project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notebook_project.databinding.FragmentHomeBinding
import com.example.notebook_project.db.NotebookTuple
import com.example.notebook_project.ui.home.adapter.NotebookRecyclerViewAdapter

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
        val rw = vb.rvNotebooks
        val my_adapter = papaContext?.let{
            NotebookRecyclerViewAdapter(mutableListOf<NotebookTuple>(), it)
        }
        rw.adapter = my_adapter
        rw.layoutManager = GridLayoutManager(papaContext, 2)
        homeViewModel.notebooks.observe(viewLifecycleOwner) {
            _binding!!.rvNotebooks.adapter?.notifyDataSetChanged()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}