package com.practice.shoppytd.repositories

import androidx.lifecycle.LiveData
import com.practice.shoppytd.data.remote.responses.ImageResponse
import com.practice.shoppytd.data.local.ShopItem
import com.practice.shoppytd.utils.NetworkResult

interface ShopRepository {

    suspend fun insertShopItem(shopItem: ShopItem)

    suspend fun deleteShopItem(shopItem: ShopItem)

    fun observeAllShopItem(): LiveData<List<ShopItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): NetworkResult<ImageResponse>
}