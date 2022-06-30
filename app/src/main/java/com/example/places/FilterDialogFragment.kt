package com.example.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.places.viewmodels.PlacesViewModel

class FilterDialogFragment : DialogFragment() {
    private val viewModel by activityViewModels<PlacesViewModel>()
    companion object {
        val TAG: String = FilterDialogFragment::class.java.simpleName ?: ""

        fun newInstance(): FilterDialogFragment {
            return FilterDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_filter, container, false)
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
            val minPriceInput = view.findViewById<EditText>(R.id.min_price_id).text.toString()
            val maxPriceInput = view.findViewById<EditText>(R.id.max_price_id).text.toString()
            val limitInput = view.findViewById<EditText>(R.id.limit_id).text.toString()
            val openAtInput = view.findViewById<EditText>(R.id.open_at_id).text.toString()
            val radioGroupSort = view.findViewById<RadioGroup>(R.id.filter_sort)
            val radioGroupOpenNow = view.findViewById<RadioGroup>(R.id.filter_open_now)

            if (radioGroupSort.checkedRadioButtonId != -1 && radioGroupOpenNow.checkedRadioButtonId == -1) {
                val radioButtonSort =
                    radioGroupSort.findViewById<View>(radioGroupSort.checkedRadioButtonId) as RadioButton
                viewModel.getPlaces(
                    minPriceInput.toIntOrNull(),
                    maxPriceInput.toIntOrNull(),
                    limitInput.toIntOrNull(),
                    openAtInput,
                    null,
                    radioButtonSort.text.toString()
                )
            } else if (radioGroupSort.checkedRadioButtonId != -1 && radioGroupOpenNow.checkedRadioButtonId != -1) {
                val radioButtonSort =
                    radioGroupSort.findViewById<View>(radioGroupSort.checkedRadioButtonId) as RadioButton
                val radioButtonOpenNow =
                    radioGroupOpenNow.findViewById<View>(radioGroupOpenNow.checkedRadioButtonId) as RadioButton
                viewModel.getPlaces(
                    minPriceInput.toIntOrNull(),
                    maxPriceInput.toIntOrNull(),
                    limitInput.toIntOrNull(),
                    openAtInput,
                    radioButtonOpenNow.text.toString().toBoolean(),
                    radioButtonSort.text.toString()
                )
            } else if (radioGroupSort.checkedRadioButtonId == -1 && radioGroupOpenNow.checkedRadioButtonId != -1) {
                val radioButtonOpenNow: RadioButton =
                    radioGroupOpenNow.findViewById<View>(radioGroupOpenNow.checkedRadioButtonId) as RadioButton
                viewModel.getPlaces(
                    minPriceInput.toIntOrNull(),
                    maxPriceInput.toIntOrNull(),
                    limitInput.toIntOrNull(),
                    openAtInput,
                    radioButtonOpenNow.text.toString().toBoolean(),
                    null
                )
            } else viewModel.getPlaces(
                minPriceInput.toIntOrNull(),
                maxPriceInput.toIntOrNull(),
                limitInput.toIntOrNull(),
                openAtInput,
                null,
                null
            )
            dismiss()
        }
    }
}