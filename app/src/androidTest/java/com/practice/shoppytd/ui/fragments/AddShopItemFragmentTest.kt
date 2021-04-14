package com.practice.shoppytd.ui.fragments

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import com.google.common.truth.Truth.assertThat
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.practice.shoppytd.R
import com.practice.shoppytd.data.local.ShopItem
import com.practice.shoppytd.getOrAwaitValue
import com.practice.shoppytd.launchFragmentInHiltContainer
import com.practice.shoppytd.repositories.FakeShopRepositoryAndroidTest
import com.practice.shoppytd.viewmodels.ShopViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.android.synthetic.main.fragment_add_shop_item.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShopItemFragmentTest{

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ShopFragmentFactory


    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickInsertIntoDb_shopItemInsertedIntoDb() {
        val testViewHolder = ShopViewModel(FakeShopRepositoryAndroidTest())
        launchFragmentInHiltContainer<AddShopItemFragment>(
            fragmentFactory = fragmentFactory
        ) {
            shopViewModel = testViewHolder
        }

        onView(withId(R.id.etShoppingItemName)).perform(replaceText("Shop Item"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("5.5"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        assertThat(testViewHolder.shopItems.getOrAwaitValue())
            .contains(ShopItem("Shop Item", 5, 5.5f, ""))
    }

    @Test
    fun pressBackButton_popBackStack() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltContainer<AddShopItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()

        verify(navController).popBackStack()
    }
}
