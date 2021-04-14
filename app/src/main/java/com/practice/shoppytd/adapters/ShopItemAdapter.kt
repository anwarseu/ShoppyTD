package com.practice.shoppytd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.practice.shoppytd.R
import com.practice.shoppytd.data.local.ShopItem
import kotlinx.android.synthetic.main.item_shop.view.*
import javax.inject.Inject

class ShopItemAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ShopItemAdapter.ShopItemViewHolder>() {

    class ShopItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback = object : DiffUtil.ItemCallback<ShopItem>() {
        override fun areItemsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShopItem, newItem: ShopItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shopItems: List<ShopItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        return ShopItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_shop,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return shopItems.size
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val shoppingItem = shopItems[position]
        holder.itemView.apply {
            glide.load(shoppingItem.imageUrl).into(ivShoppingImage)

            tvName.text = shoppingItem.name
            val amountText = "${shoppingItem.amount}x"
            tvShoppingItemAmount.text = amountText
            val priceText = "${shoppingItem.price}€"
            tvShoppingItemPrice.text = priceText
        }
    }
}