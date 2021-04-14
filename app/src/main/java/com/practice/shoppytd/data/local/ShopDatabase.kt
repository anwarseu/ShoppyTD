package com.practice.shoppytd.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
        entities = [ShopItem::class],
        version = 1
)
abstract class ShopDatabase : RoomDatabase(){
    abstract fun shopDao(): ShopDao
}