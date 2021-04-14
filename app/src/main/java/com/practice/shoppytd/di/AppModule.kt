package com.practice.shoppytd.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.practice.shoppytd.R
import com.practice.shoppytd.data.local.ShopDao
import com.practice.shoppytd.data.local.ShopDatabase
import com.practice.shoppytd.data.remote.PixabayAPI
import com.practice.shoppytd.repositories.DefaultShopRepository
import com.practice.shoppytd.repositories.ShopRepository
import com.practice.shoppytd.utils.Constants.Companion.BASE_URL
import com.practice.shoppytd.utils.Constants.Companion.SHOP_DATABASE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShopDatabase(
            @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShopDatabase::class.java, SHOP_DATABASE).build()

    @Singleton
    @Provides
    fun provideDefaultShopRepository(
            dao: ShopDao,
            api: PixabayAPI
    ) = DefaultShopRepository(dao, api) as ShopRepository

    @Singleton
    @Provides
    fun provideGlideInstance(
            @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)
    )

    @Singleton
    @Provides
    fun provideShopDao(
            database: ShopDatabase
    ) = database.shopDao()

    @Singleton
    @Provides
    fun providePixabayAPI(): PixabayAPI {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(PixabayAPI::class.java)
    }
}