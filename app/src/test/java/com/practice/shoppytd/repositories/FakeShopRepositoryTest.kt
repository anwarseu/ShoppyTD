package com.practice.shoppytd.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practice.shoppytd.data.local.ShopItem
import com.practice.shoppytd.data.remote.responses.ImageResponse
import com.practice.shoppytd.utils.NetworkResult

class FakeShopRepositoryTest: ShopRepository {

    private val shopItems = mutableListOf<ShopItem>()

    private val observableShopItems = MutableLiveData<List<ShopItem>>(shopItems)
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableShopItems.postValue(shopItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    private fun getTotalPrice(): Float {
        return shopItems.sumByDouble { it.price.toDouble() }.toFloat()
    }

    override suspend fun insertShopItem(shopItem: ShopItem) {
        shopItems.add(shopItem)
        refreshLiveData()
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
        shopItems.remove(shopItem)
        refreshLiveData()
    }

    override fun observeAllShopItem(): LiveData<List<ShopItem>> {
       return observableShopItems
    }


    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImage(imageQuery: String): NetworkResult<ImageResponse> {
        return if(shouldReturnNetworkError) {
            NetworkResult.error("Error", null)
        } else {
            NetworkResult.success(ImageResponse(listOf(), 0, 0))
        }
    }
}