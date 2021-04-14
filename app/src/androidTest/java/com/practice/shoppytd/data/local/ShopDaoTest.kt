package com.practice.shoppytd.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.filters.SmallTest
import com.practice.shoppytd.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import com.practice.shoppytd.launchFragmentInHiltContainer
import com.practice.shoppytd.ui.fragments.ShopFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ShopDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ShopDatabase
    private lateinit var dao: ShopDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.shopDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testLaunchFragmentInHiltContainer() {
        launchFragmentInHiltContainer<ShopFragment> {

        }
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shopItem = ShopItem("name", 1, 1f, "url", id = 1)
        dao.insertShopItem(shopItem)

        val allShoppingItems = dao.observeAllShopItems().getOrAwaitValue()
        assertThat(allShoppingItems).contains(shopItem)

    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shopItem = ShopItem("name", 1, 1f, "url", id = 1)
        dao.insertShopItem(shopItem)
        dao.deleteShopItem(shopItem)

        val allShoppingItems = dao.observeAllShopItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shopItem)
    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shopItem1 = ShopItem("name", 2, 10f, "url", id = 1)
        val shopItem2 = ShopItem("name", 4, 5.5f, "url", id = 2)
        val shopItem3 = ShopItem("name", 0, 100f, "url", id = 3)
        dao.insertShopItem(shopItem1)
        dao.insertShopItem(shopItem2)
        dao.insertShopItem(shopItem3)

        val totalPriceSum = dao.observeTotalPrice().getOrAwaitValue()

        assertThat(totalPriceSum).isEqualTo(2 * 10f + 4 * 5.5f)
    }
}