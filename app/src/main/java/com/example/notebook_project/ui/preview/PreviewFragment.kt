package com.example.notebook_project.ui.preview

import android.os.Bundle
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notebook_project.R
import com.example.notebook_project.databinding.FragmentPreviewBinding
import com.example.notebook_project.db.repository.NotebookRepository
import com.example.notebook_project.db.repository.NotebookRepository_Impl
import com.example.notebook_project.db.repository.UserPreferencesRepository
import com.example.notebook_project.db.repository.UserPreferencesRepository_Impl
import com.example.notebook_project.db.repository.dataStore
import com.example.notebook_project.db.viewmodel.NotebookViewModel
import com.example.notebook_project.db.viewmodel.NotebookViewModelFactory
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonPlugin
import io.noties.markwon.core.MarkwonTheme


class PreviewFragment : Fragment() {

    val args by navArgs<PreviewFragmentArgs>()
    private var _binding: FragmentPreviewBinding? = null
    private val vb get() = _binding!!
    private lateinit var notebookViewModel: NotebookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        val root: View = vb.root
//        val appContext = requireActivity().application
        notebookViewModel = ViewModelProvider(requireActivity(),
            NotebookViewModelFactory(
                NotebookRepository_Impl.getInstance(requireActivity()),
                UserPreferencesRepository_Impl(
                    requireActivity().dataStore,
                ),
                requireActivity().application
            )
        )[NotebookViewModel::class.java]

        val tv_preview_body = vb.tvPreviewNotebookBody
        tv_preview_body.movementMethod = ScrollingMovementMethod()
        vb.tvPreviewNotebookName.text = args.currentNotebook.notebook_name
        val date_created_txt = "${resources.getString(R.string.created_on)}: ${args.currentNotebook.dateTimeLastEdited}"
        val date_edited_txt = "${resources.getString(R.string.latest_changes)}: ${args.currentNotebook.dateTimeLastEdited}"
        vb.tvPreviewDateCreated.text = date_created_txt
        vb.tvPreviewDateEdited.text = date_edited_txt
        val body = notebookViewModel.readNotebookByUri(args.currentNotebook.uri)

//        val mono_font = resources.getFont(R.font.space_mono)
//        val serif_font = resources.getFont(R.font.cmu_serif)

//        val theme: AbstractMarkwonPlugin = object : AbstractMarkwonPlugin() {
//            override fun beforeSetText(textView: TextView, markdown: Spanned) {
//                super.beforeSetText(textView, markdown)
//                textView.linksClickable = true
//            }
//            override fun configureTheme(builder: MarkwonTheme.Builder) {
//                super.configureTheme(builder)
//                builder
////                    .codeTypeface(mono_font)
////                    .codeBlockTypeface(mono_font)
//                    .isLinkUnderlined(true)
////                    .headingTypeface(serif_font)
//            }
//        }
//        val markwon: Markwon? = try {
//            this.context?.let {
//                Markwon.builder(it)
//                    .usePlugin(theme)
//                    .build()
//            }
//
//        } catch (e: Exception) {
//            this.context?.let{
//                Markwon.create(it)
//            }
//        }

        val markwon = Markwon.create(requireActivity())
        markwon.setMarkdown(tv_preview_body, body)



        vb.fabEdit.setOnClickListener{
            val action = PreviewFragmentDirections.actionNavPreviewToEditFragment(args.currentNotebook)
            findNavController().navigate(action)
        }
//        FragmentPreviewBinding.inflate(R.menu.fragment_edit_appbar_menu)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object
                : OnBackPressedCallback(true)
                {
                    override fun handleOnBackPressed() {
                        val action = PreviewFragmentDirections.actionNavPreviewToNavHome()
                        findNavController().navigate(action)
                    }
                }
            )

        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_edit_appbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.appbar_menu_delete_button -> {
            notebookViewModel.deleteNotebook(args.currentNotebook.notebook_name)
            val action = PreviewFragmentDirections.actionNavPreviewToNavHome()
            findNavController().navigate(action)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}