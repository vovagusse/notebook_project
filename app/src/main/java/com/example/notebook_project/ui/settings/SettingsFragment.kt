package com.example.notebook_project.ui.editor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.notebook_project.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val vb
        get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Use the ViewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = vb.root
        val papaContext = container?.context
        return vb.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
