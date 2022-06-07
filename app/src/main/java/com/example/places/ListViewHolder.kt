package com.example.places

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

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
        tvDescription.text = place.description
        tvTel.text = place.tel
        tvDescription.visibility = View.GONE
        tvTel.visibility = View.GONE

    }
}
