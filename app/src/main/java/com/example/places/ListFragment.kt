package com.example.places

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.places.databinding.ListFragmentBinding

class ListFragment : Fragment(R.layout.list_fragment) {
    private var binding: ListFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listAdapter = ListAdapter(
            mutableListOf()
//            mutableListOf(
//                Place(name = "charbel", "beirut", "fdhfrj", "fjfbhe", "reiuthrei", "gfhi"),
//                Place(name = "charbel", "beirut", "fdhfrj", "fjfbhe", "reiuthrei", "gfhi"),
//                Place(name = "charbel", "beirut", "fdhfrj", "fjfbhe", "reiuthrei", "gfhi"),
//                Place(name = "charbel", "beirut", "fdhfrj", "fjfbhe", "reiuthrei", "gfhi")
//            )
        )

        binding?.placesRecyclerView?.adapter = listAdapter
        binding?.placesRecyclerView?.layoutManager = LinearLayoutManager(context)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}