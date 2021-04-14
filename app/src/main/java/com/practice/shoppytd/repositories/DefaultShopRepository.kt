package com.practice.shoppytd.repositories

import androidx.lifecycle.LiveData
import com.practice.shoppytd.data.local.ShopDao
import com.practice.shoppytd.data.remote.PixabayAPI
import com.practice.shoppytd.data.remote.responses.ImageResponse
import com.practice.shoppytd.data.local.ShopItem
import com.practice.shoppytd.utils.Constants.Companion.API_KEY
import com.practice.shoppytd.utils.NetworkResult
import javax.inject.Inject

class DefaultShopRepository @Inject constructor(
        private val shopDao: ShopDao,
        private val pixabayAPI: PixabayAPI
): ShopRepository {
    override suspend fun insertShopItem(shopItem: ShopItem) {
        shopDao.insertShopItem(shopItem)
    }

    override suspend fun deleteShopItem(shopItem: ShopItem) {
       shopDao.deleteShopItem(shopItem)
    }

    override fun observeAllShopItem(): LiveData<List<ShopItem>> {
        return shopDao.observeAllShopItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shopDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): NetworkResult<ImageResponse> {
        return try {
            val response = pixabayAPI.searchForImage(imageQuery, API_KEY)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let NetworkResult.success(it)
                } ?: return NetworkResult.error("An unknown error occurred", null)
            }else{
                return NetworkResult.error("An unknown error occurred", null)
            }
        }catch (e: Exception){
            return NetworkResult.error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}