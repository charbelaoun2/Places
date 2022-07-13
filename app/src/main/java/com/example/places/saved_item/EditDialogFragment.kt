package com.example.places.saved_item

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.places.*
import com.example.places.viewmodels.PlacesViewModel

class EditDialogFragment : DialogFragment() {
    private val viewModel by activityViewModels<PlacesViewModel>()

    companion object {
        val TAG: String = FilterDialogFragment::class.java.simpleName ?: ""
        const val PLACE_ARGUMENT = "PLACE_ARGUMENT"

        @JvmStatic
        fun newInstance(place: Place): EditDialogFragment {
            val fragment = EditDialogFragment()
            val args = Bundle()
            fragment.arguments = args
            args.putParcelable(PlaceDetailBottomSheet.PLACE_ARGUMENT, place)
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.edit_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners(view)
    }

    private fun setupClickListeners(view: View) {
        view.findViewById<Button>(R.id.negative_button).setOnClickListener {
            dismiss()
        }
        view.findViewById<Button>(R.id.positive_button).setOnClickListener {
            val nameEdited = view.findViewById<EditText>(R.id.name_edited)
            val addressEdited = view.findViewById<EditText>(R.id.address_edited)
            val place = arguments?.getParcelable<Place>(PLACE_ARGUMENT)
            if (nameEdited.text.toString().trim().isBlank() && addressEdited.text.toString()
                    .isBlank()
            ) {
                Toast.makeText(
                    context,
                    "You should Enter the Edited name and address",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (nameEdited.text.toString().trim()
                    .isNotBlank() && addressEdited.text.toString().isBlank()
            ) {
                place?.name = nameEdited.text.toString()
                viewModel.editedText.value = place
            } else if (nameEdited.text.toString().trim().isBlank() && addressEdited.text.toString()
                    .isNotBlank()
            ) {
                place?.address = addressEdited.text.toString()
                viewModel.editedText.value = place
            } else {
                place?.name = nameEdited.text.toString()
                place?.address = addressEdited.text.toString()
                viewModel.editedText.value = place
            }
            dismiss()

        }
    }
}