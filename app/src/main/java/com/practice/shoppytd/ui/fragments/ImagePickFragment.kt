package com.practice.shoppytd.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practice.shoppytd.R
import com.practice.shoppytd.adapters.ImageAdapter
import com.practice.shoppytd.utils.Constants.Companion.GRID_SPAN_COUNT
import com.practice.shoppytd.viewmodels.ShopViewModel
import kotlinx.android.synthetic.main.fragment_image_pick.*
import javax.inject.Inject

class ImagePickFragment @Inject constructor(
        val imageAdapter: ImageAdapter
): Fragment(R.layout.fragment_image_pick) {

    lateinit var shopViewModel: ShopViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopViewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)

        setupRecyclerView()

        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            shopViewModel.setCurImageUrl(it)
        }
    }

    private fun setupRecyclerView() {
        rvImages.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT)
        }
    }
}