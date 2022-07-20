package com.example.places.saved_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.places.Place
import com.example.places.R

class ListSavedItemAdapter : RecyclerView.Adapter<ListSavedViewHolder>() {

    var itemListSaved = emptyList<Place>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSavedViewHolder {
        return ListSavedViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.place_details, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ListSavedViewHolder, position: Int) {
        val currentCity = itemListSaved[position]
        holder.setItem(currentCity)
    }

    override fun getItemCount(): Int = itemListSaved.size

    fun setData(place: List<Place>) {
        itemListSaved = place
        notifyDataSetChanged()
    }
}