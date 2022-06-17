package com.example.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.places.databinding.PlaceDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PlaceDetailBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding : PlaceDetailsBinding

    companion object {
        val TAG: String = PlaceDetailBottomSheet::class.java.simpleName ?: ""
        const val PLACE_ARGUMENT = "PLACE_ARGUMENT"

        @JvmStatic
        fun newInstance(place: Place): PlaceDetailBottomSheet {
            val fragment = PlaceDetailBottomSheet()
            val args = Bundle()
            fragment.arguments=args
            args?.putParcelable(PLACE_ARGUMENT, place)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = PlaceDetailsBinding.inflate(inflater,container,false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val place = arguments?.getParcelable<Place>(PLACE_ARGUMENT)
        if (place != null) {
            binding.nameTextView.text = place.name
            binding.addressTextView.text = place.address
            binding.descriptionTextView.text = place.description
            binding.telTextView.text = place.tel
            Glide.with(this)
                .load(place.imageUrl)
                .into(binding.placeImageView)
        }
    }
}