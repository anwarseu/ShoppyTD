package com.practice.shoppytd.ui.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.practice.shoppytd.adapters.ImageAdapter
import com.practice.shoppytd.adapters.ShopItemAdapter
import com.practice.shoppytd.repositories.FakeShopRepositoryAndroidTest
import com.practice.shoppytd.viewmodels.ShopViewModel
import javax.inject.Inject

class ShopFragmentFactoryTest @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager,
    private val shopItemAdapter: ShopItemAdapter
): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShopItemFragment::class.java.name -> AddShopItemFragment(glide)
            ShopFragment::class.java.name -> ShopFragment(
                shopItemAdapter,
                ShopViewModel(FakeShopRepositoryAndroidTest())
            )
            else -> super.instantiate(classLoader, className)
        }
    }
}