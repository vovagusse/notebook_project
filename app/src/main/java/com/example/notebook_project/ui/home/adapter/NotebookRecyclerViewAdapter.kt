package com.example.notebook_project.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook_project.R
import com.example.notebook_project.db.entities.Notebook
import com.example.notebook_project.ui.home.HomeFragmentDirections

class NotebookRecyclerViewAdapter (
    var context: Context,
) : RecyclerView.Adapter<NotebookRecyclerViewAdapter.NotebookViewHolder>() {

    interface OptionsMenuClickListener {
        fun onOptionsMenuClicked(pos: Int)
    }
    private var notebooks : List<Notebook> = ArrayList(0)
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class NotebookViewHolder(iv: View) : RecyclerView.ViewHolder(iv){
        var tv_date_created: TextView
        var tv_name: TextView
        var tv_date_edited: TextView
        var tv_body: TextView
        val container : ConstraintLayout
        val context_menu_button : ImageButton
        init{
            context_menu_button = iv.findViewById(R.id.ib_item_context_menu_options)
            container = iv.findViewById(R.id.item_notebook_constraintLayout)
            tv_name = iv.findViewById(R.id.tv_item_notebook_name)
            tv_body = iv.findViewById(R.id.tv_item_notebook_body)
            tv_date_edited = iv.findViewById(R.id.tv_item_date_edited)
            tv_date_created = iv.findViewById(R.id.tv_item_date_created)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotebookViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_notebook, parent, false)
        return NotebookViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.notebooks.size
    }

    override fun onBindViewHolder(holder: NotebookViewHolder, pos: Int) {
        val nb = notebooks[pos]
        holder.tv_name.text = nb.notebook_name
        var body = ""
        val max_length = 200
        if (body.length > 200){
            body = body.substring(0, max_length-3) + "..."
        }
        holder.tv_body.text = "${body}"
        holder.tv_date_edited.text = "${gs(R.string.latest_changes)}: ${nb.dateTimeLastEdited}"
        holder.tv_date_created.text = "${gs(R.string.created_on)}: ${nb.dateTimeLastEdited}"

        holder.container.setOnClickListener{
            val action = HomeFragmentDirections.actionNavHomeToPreviewFragment(notebooks[pos])
            holder.itemView.findNavController().navigate(action)
        }

        holder.context_menu_button.setOnClickListener{

        }
    }

    private fun gs(id: Int): String{
        return this.context.resources.getString(id)
    }

    fun setData(notebook: List<Notebook>?) {
        notebook?.let { notebooks = it }
        notifyDataSetChanged()
    }


}