package com.example.notebook_project.ui.editor

import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentEditBinding
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.NotebookRepository_Impl
import com.example.notebook_project.db.repository.UserPreferencesRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository_Impl
import com.example.notebook_project.db.repository.dataStore
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.db.viewmodel.NotebookViewModelFactory
import com.example.notebook_project.ui.preview.PreviewFragmentDirections
import com.example.notebook_project.util.EditTextFormatting
import com.example.notebook_project.util.makeFileName
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.core.MarkwonTheme
import io.noties.markwon.editor.MarkwonEditor
import io.noties.markwon.editor.MarkwonEditorTextWatcher
import java.util.Date



class EditFragment : Fragment() {


    private var _binding: FragmentEditBinding? = null
    private val vb get() = _binding!!
    private val args by navArgs<EditFragmentArgs>()
    private lateinit var NOTEBOOK_VIEW_MODEL: NotebookViewModel
    var current_nb : Notebook? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentEditBinding.inflate(inflater, container, false)
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

        vb.etEditTitle.setText(args.currentNotebook.notebook_name)

        val name : String = args.currentNotebook.notebook_name
        vb.etEditBody.setText( NOTEBOOK_VIEW_MODEL.readNotebookByName(name) )
//        val theme: AbstractMarkwonPlugin = object : AbstractMarkwonPlugin() {
//            override fun configureTheme(builder: MarkwonTheme.Builder) {
////                super.configureTheme(builder)
//                builder
//                    .codeTypeface(
//                        resources.getFont(R.font.space_mono)
//                    )
//                    .codeBlockTypeface(
//                        resources.getFont(
//                            R.font.space_mono
//                        )
//                    )
//                    .isLinkUnderlined(true)
//                    .headingTypeface(
//                        resources.getFont(
//                            R.font.cmu_serif
//                        )
//                    )
//            }
//        }
        val markwon = Markwon.builder(requireContext())
//            .usePlugin(theme)
            .build()
        val editor = MarkwonEditor.create(markwon)

        vb.etEditBody.addTextChangedListener(MarkwonEditorTextWatcher.withProcess(editor))
        vb.fabEditorSave.setOnClickListener{
            current_nb = updateNotebook()
        }
        vb.fabEditorSave.setOnLongClickListener{
            current_nb = updateNotebook()
            current_nb?.let {
                val action = EditFragmentDirections
                    .actionEditFragmentToNavPreview(it)
                findNavController().navigate(action)
            }
            true
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action = current_nb?.let {
                        EditFragmentDirections.actionEditFragmentToNavPreview(
                            it
                        )
                    }
                    action?.let { findNavController().navigate(it) }
                }
            }
            )

        //bar buttons
        val frm = EditTextFormatting()
        vb.tbEditBold.setOnClickListener {
            frm.formatBold(vb.etEditBody)
        }
        vb.tbEditItalic.setOnClickListener {
            frm.formatItalic(vb.etEditBody)
        }
        vb.tbEditUnderlined.setOnClickListener {
            frm.formatUnderlined(vb.etEditBody)
        }
        vb.tbEditCodeInline.setOnClickListener {
            frm.formatCodeInline(vb.etEditBody)
        }
        vb.tbEditCodeBlock.setOnClickListener {
            frm.formatCodeBlock(vb.etEditBody)
        }


        return root
    }

    private fun updateNotebook() : Notebook?{
        var uri = args.currentNotebook.uri
        val currentDateTime : Date = Calendar.getInstance().time
        val id = args.currentNotebook.id
        val old_name = args.currentNotebook.notebook_name
        val name = vb.etEditTitle.text.toString()
        val body = vb.etEditBody.text.toString()

        val errMessage = this
            .context?.resources?.getString(R.string.err_no_notebook_title)
        if (TextUtils.isEmpty(name)){
            Toast.makeText(this.context, "$errMessage", Toast.LENGTH_LONG).show()
            return null
        }

        if (old_name != name) {
            val new_uri = makeFileName(name)
            NOTEBOOK_VIEW_MODEL.renameNotebook(uri, new_uri)
            uri = new_uri
        }

        val date_created = args.currentNotebook.dateTimeOfCreation
        val date_edited = currentDateTime

        val notebook_updated = Notebook(
            id, uri, name, date_created, date_edited
        )
        NOTEBOOK_VIEW_MODEL.upsertNotebook(notebook_updated, body)
        val success_edit = this.context?.resources?.getString(R.string.success_edit_notebook)
        Toast.makeText(requireActivity(), success_edit, Toast.LENGTH_LONG).show()

        return notebook_updated
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_edit_appbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.appbar_menu_delete_button -> {
            NOTEBOOK_VIEW_MODEL.deleteNotebook(args.currentNotebook.notebook_name)
            val action = EditFragmentDirections.actionEditFragmentToNavHome()
            findNavController().navigate(action)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}