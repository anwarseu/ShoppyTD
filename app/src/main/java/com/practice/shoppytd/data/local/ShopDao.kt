package com.practice.shoppytd.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.practice.shoppytd.utils.Constants.Companion.SHOP_ITEM_TABLE

@Dao
interface ShopDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShopItem(shopItem: ShopItem)

    @Delete
    suspend fun deleteShopItem(shopItem: ShopItem)

    @Query("SELECT * FROM $SHOP_ITEM_TABLE")
    fun observeAllShopItems(): LiveData<List<ShopItem>>

    @Query("SELECT SUM(price * amount) FROM $SHOP_ITEM_TABLE")
    fun observeTotalPrice(): LiveData<Float>
}