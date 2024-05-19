package com.example.notebook_project.ui.editor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.notebook_project.R
import com.example.notebook_project.databinding.ContentSettingsBinding
import com.example.notebook_project.databinding.FragmentSettingsBinding
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository
import com.example.notebook_project.db.repository.dataStore
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.db.viewmodel.NotebookViewModelFactory
import com.example.notebook_project.util.convertStringToSortOrder
import com.example.notebook_project.util.convertStringToSortingParameter
import com.example.notebook_project.util.convertStringToStorageType
import com.example.notebook_project.util.convertStringToUserTheme
import java.io.File


class SettingsFragment : PreferenceFragmentCompat(){
    private lateinit var NOTEBOOK_VIEW_MODEL: NotebookViewModel
    private val keys = Keys()
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        NOTEBOOK_VIEW_MODEL = ViewModelProvider(requireActivity(),
            NotebookViewModelFactory(
                NotebookRepository.getInstance(requireActivity()),
                UserPreferencesRepository(
                    requireActivity().dataStore,
                )
            )
        )[NotebookViewModel::class.java]

        val theme = findPreference<ListPreference>(keys.app_theme)
        theme?.setOnPreferenceChangeListener { _, newValue ->
            Log.d("Preferences", "$newValue")
            convertStringToUserTheme(newValue.toString().uppercase()).apply {
                this?.let { NOTEBOOK_VIEW_MODEL.changeTheme(it) }
            }
            true }


        val storage = findPreference<ListPreference>(keys.storage_type)
        storage?.setOnPreferenceChangeListener {_, newValue ->
            Log.d("Preferences", "$newValue")
            convertStringToStorageType(newValue.toString().uppercase()).apply {
                this?.let { NOTEBOOK_VIEW_MODEL.changeStorageType(it) }
            }
            true}

        val sortOrder = findPreference<ListPreference>(keys.sort_order)
        sortOrder?.setOnPreferenceChangeListener {_, newValue ->
            Log.d("Preferences", "$newValue")
            convertStringToSortOrder(newValue.toString().uppercase()).apply {
                this?.let { NOTEBOOK_VIEW_MODEL.changeSortOrder(it) }
            }
            true}

        val sortParam = findPreference<ListPreference>(keys.sorting_parameter)
        sortParam?.setOnPreferenceChangeListener {_, newValue ->
            Log.d("Preferences", "$newValue")
            convertStringToSortingParameter(newValue.toString().uppercase()).apply {
                this?.let { NOTEBOOK_VIEW_MODEL.changeSortParam(it) }
            }
            true}

        val folder = findPreference<EditTextPreference>(keys.folder)
        folder?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply{
                val uri = Uri.parse(
                    Environment
                        .getExternalStorageDirectory()
                        .path +
                        File.separator + "folder" + File.separator)
                this.setDataAndType(uri, "resource/folder")
                this.addCategory(Intent.CATEGORY_OPENABLE)
                try{
                    startActivity(
                        Intent.createChooser(
                            this@apply,
                            resources.getText(R.string.select_file_message)
                        )
                    )
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(requireActivity(),
                        resources.getText(R.string.err_no_file_manager),
                        Toast.LENGTH_LONG).show()
                }
            }

            true
        }
        folder?.setOnBindEditTextListener {value ->
            Log.d("Preferences", "${value.text}") }
    }
}

//class SettingsFragment : Fragment(){
//    private lateinit var _binding : FragmentSettingsBinding
//    private var vb = _binding
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentSettingsBinding.inflate(inflater)
//
//        val a = vb.contentSettings.contentPreferenceScreen
//        val transaction = requireActivity().supportFragmentManager.beginTransaction()
//        val poop = SettingsFragmentSon()
//        transaction.add(a.id, poop)
//        transaction.commit()
//        return vb.root
//    }
//}