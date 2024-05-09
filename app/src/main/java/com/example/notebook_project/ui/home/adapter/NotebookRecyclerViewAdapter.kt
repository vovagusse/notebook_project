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
        var tv_name: TextView
        var tv_index_in_list: TextView
        var tv_date: TextView
        var tv_description: TextView
        init{
            tv_name = iv.findViewById(R.id.tv_name)
            tv_index_in_list = iv.findViewById(R.id.tv_index_in_list)
            tv_date = iv.findViewById(R.id.tv_date)
            tv_description = iv.findViewById(R.id.tv_description)
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
        holder.tv_index_in_list.text = pos.toString()
        holder.tv_description.text = nb.description
        holder.tv_date.text = "Последние изменения: $nb.dateTimeLastEdited"
    }
}