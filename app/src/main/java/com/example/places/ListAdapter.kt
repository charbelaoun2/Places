package com.example.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ListAdapter(
    var lists: List<Place>, private val listener :OnItemClickListener
) : RecyclerView.Adapter<ListViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(place :Place)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.place_details, parent, false
            )
        )
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentCity = lists[position]
        holder.setItem(currentCity)
        holder.itemView.setOnClickListener { listener.onItemClick(currentCity) }
    }

    override fun getItemCount(): Int = lists.size
}