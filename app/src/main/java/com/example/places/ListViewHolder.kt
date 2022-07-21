package com.example.places

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvName = itemView.findViewById<TextView>(R.id.name_text_view)
    private val tvAddress = itemView.findViewById<TextView>(R.id.address_text_view)
    private val ivPlace = itemView.findViewById<ImageView>(R.id.place_image_view)
    private val tvEmail = itemView.findViewById<TextView>(R.id.email_address_text_view)
    private val tvDescription = itemView.findViewById<TextView>(R.id.description_text_view)
    private val tvTel = itemView.findViewById<TextView>(R.id.tel_text_view)

    fun setItem(place: Place) {
        tvName.text = place.name ?: "N/A"
        tvEmail.text = place.email ?: "N/A"
        tvAddress.text = place.address ?: "N/A"
        tvEmail.text = place.email ?: "N/A"

        Glide.with(itemView.context)
            .load(place.imageUrl)
            .placeholder(R.drawable.image_placeholder)
            .into(ivPlace)

        if (place.description != null) {
            tvDescription.visibility = View.VISIBLE
            tvDescription.text = place.description
        } else {
            tvDescription.visibility = View.GONE
        }
        if (place.tel != null) {
            tvDescription.visibility = View.VISIBLE
            tvTel.text = place.tel
        } else {
            tvTel.visibility = View.GONE
        }
    }
}
