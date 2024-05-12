package com.example.notebook_project.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notebook_project.R
import com.example.notebook_project.db.NotebookTuple

class NotebookRecyclerViewAdapter (
    var notebooks: MutableList<NotebookTuple>,
    var context: Context
) : RecyclerView.Adapter<NotebookRecyclerViewAdapter.NotebookViewHolder>() {
    class NotebookViewHolder(iv: View) : RecyclerView.ViewHolder(iv){
        var tv_date_created: TextView
        var tv_name: TextView
        var tv_date_edited: TextView
        var tv_description: TextView
        init{
            tv_name = iv.findViewById(R.id.tv_item_name)
            tv_date_edited = iv.findViewById(R.id.tv_item_date_edited)
            tv_description = iv.findViewById(R.id.tv_item_description)
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

        holder.tv_description.text = "${gs(R.string.item_description)}: ${nb.description}"
        holder.tv_date_edited.text = "${gs(R.string.latest_changes)}: ${nb.dateTimeLastEdited}"
        holder.tv_date_created.text = "${gs(R.string.created_on)}: ${nb.dateTimeLastEdited}"
    }

    private fun gs(id: Int): String{
        return this.context.resources.getString(id)
    }
}