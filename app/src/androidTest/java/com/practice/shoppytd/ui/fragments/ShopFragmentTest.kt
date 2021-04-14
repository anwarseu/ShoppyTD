package com.practice.shoppytd.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.practice.shoppytd.R
import com.practice.shoppytd.data.local.ShopItem
import com.practice.shoppytd.launchFragmentInHiltContainer
import com.practice.shoppytd.viewmodels.ShopViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.google.common.truth.Truth.assertThat
import com.practice.shoppytd.adapters.ShopItemAdapter
import com.practice.shoppytd.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ShopFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactoryTest: ShopFragmentFactoryTest

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun swipeShopItem_deleteItemInDb() {
        val shopItem = ShopItem("TEST", 1, 1f, "TEST", 1)
        var testViewModel: ShopViewModel? = null

        launchFragmentInHiltContainer<ShopFragment>(
            fragmentFactory = fragmentFactoryTest
        ) {
            testViewModel =  shopViewModel
            shopViewModel?.insertShopItemIntoDb(shopItem)
        }

        onView(withId(R.id.rvShoppingItems)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ShopItemAdapter.ShopItemViewHolder>(
                0,
                swipeLeft()
            )
        )

        assertThat(testViewModel?.shopItems?.getOrAwaitValue()).isEmpty()

    }

    @Test
    fun clickAddShoppingItemButton_navigateToAddShoppingItemFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<ShopFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.fabAddShoppingItem)).perform(click())

        verify(navController).navigate(
            ShopFragmentDirections.actionShopFragmentToAddShopItemFragment()
        )
    }
}