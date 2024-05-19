package com.example.notebook_project.ui.editor

import android.content.Context
import android.os.Bundle
import androidx.datastore.preferences.preferencesDataStore
import androidx.preference.PreferenceFragmentCompat
import com.example.notebook_project.R
import com.example.notebook_project.db.viewmodel.NotebookViewModel


class SettingsFragment : PreferenceFragmentCompat(){
    private lateinit var viewModel: NotebookViewModel
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}

