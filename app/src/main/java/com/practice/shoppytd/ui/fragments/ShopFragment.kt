package com.practice.shoppytd.ui.fragments

import android.app.ProgressDialog.show
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.practice.shoppytd.R
import com.practice.shoppytd.adapters.ShopItemAdapter
import com.practice.shoppytd.viewmodels.ShopViewModel
import kotlinx.android.synthetic.main.fragment_shop.*
import javax.inject.Inject

class ShopFragment @Inject constructor(
   val shopItemAdapter: ShopItemAdapter,
   var shopViewModel: ShopViewModel? = null
): Fragment(R.layout.fragment_shop) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shopViewModel = shopViewModel ?: ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)
        subscribeToObservers()
        setupRecyclerView()

        fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(
                    ShopFragmentDirections.actionShopFragmentToAddShopItemFragment()
            )
        }
    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = shopItemAdapter.shopItems[pos]
            shopViewModel?.deleteShopItem(item)
            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    shopViewModel?.insertShopItemIntoDb(item)
                }
                show()
            }

        }
    }

    private fun subscribeToObservers() {
        shopViewModel?.shopItems?.observe(viewLifecycleOwner, Observer {
            shopItemAdapter.shopItems = it
        })
        shopViewModel?.totalPrice?.observe(viewLifecycleOwner, Observer {
            val price = it ?: 0f
            val priceText = "Total Price: $price€"
            tvShoppingItemPrice.text = priceText
        })
    }

    private fun setupRecyclerView() {
        rvShoppingItems.apply {
            adapter = shopItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }

}