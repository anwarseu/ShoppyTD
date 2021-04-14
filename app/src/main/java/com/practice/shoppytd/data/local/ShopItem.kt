package com.practice.shoppytd.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.practice.shoppytd.utils.Constants.Companion.SHOP_ITEM_TABLE

@Entity(tableName = SHOP_ITEM_TABLE)
data class ShopItem(
    var name: String,
    var amount: Int,
    var price: Float,
    var imageUrl: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
