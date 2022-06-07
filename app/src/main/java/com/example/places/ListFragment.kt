package com.example.places

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.places.databinding.ListFragmentBinding
import retrofit2.HttpException
import java.io.IOException

const val TAG = "Main Activity"

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
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getPlaces("41.8781,-87.6298", 9999)

            } catch (e: IOException) {
                Log.e(TAG, "IOException, you may not have internet connection")
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException, unexpected response ")
                return@launchWhenCreated
            }

            val list = response.body()?.results?.map {
                Place(
                    it.name,
                    it.location.address, "url", it.email, it.description, it.tel
                )
            }

            if (response.isSuccessful && list != null) {
                setupAdapter(list)

            }
        }


    }

    fun setupAdapter(placesList: List<Place>) {
        val listAdapter = ListAdapter(
            placesList

        )
        binding?.placesRecyclerView?.adapter = listAdapter
        binding?.placesRecyclerView?.layoutManager = LinearLayoutManager(context)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}