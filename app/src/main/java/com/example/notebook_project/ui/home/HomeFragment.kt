package com.example.notebook_project.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.notebook_project.databinding.FragmentHomeBinding
import com.example.notebook_project.util.notebookTemplate
import com.example.notebook_project.ui.home.adapter.NotebookRecyclerViewAdapter
import com.example.notebook_project.ui.home.rwdecoration.GridSpacingItemDecoration

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var homeViewModel: HomeViewModel? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val vb get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
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
        homeViewModel?.notebooks?.observe(viewLifecycleOwner) {
            rw.adapter?.notifyItemRangeChanged(
                0,
                rw.adapter?.itemCount?:0)
        }
        homeViewModel?.spans?.observe(viewLifecycleOwner){
            val newDecor = GridSpacingItemDecoration(it, 16, true)
            rw.addItemDecoration(newDecor)
            rw.adapter?.notifyItemRangeChanged(
                0,
                rw.adapter?.itemCount?:0)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            homeViewModel?.changeSpansTo(3)
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            homeViewModel?.changeSpansTo(2)
        }
    }
}