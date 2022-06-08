package com.example.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(
    private val lists : MutableList<Place>
) : RecyclerView.Adapter<ListViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,parent,false
        ))
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val current_city=lists[position]
        holder.setItem(current_city)


    }

    override fun getItemCount(): Int = lists.size
}