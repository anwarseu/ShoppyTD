package com.practice.shoppytd.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.practice.shoppytd.utils.Status
import com.practice.shoppytd.MainCoroutineRule
import com.practice.shoppytd.getOrAwaitValueTest
import com.practice.shoppytd.repositories.FakeShopRepositoryTest
import com.practice.shoppytd.utils.Constants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShopViewModelTest{

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ShopViewModel

    @Before
    fun setup(){
       viewModel = ShopViewModel(FakeShopRepositoryTest())
    }

    @Test
    fun `insert shopping item with empty field, returns error`() {
        viewModel.insertShopItem("name", "", "3.0")

        val value = viewModel.insertShopItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name, returns error`() {
        val string = buildString {
            for(i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShopItem(string, "5", "3.0")

        val value = viewModel.insertShopItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price, returns error`() {
        val string = buildString {
            for(i in 1..Constants.MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShopItem("name", "5", string)

        val value = viewModel.insertShopItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, returns error`() {
        viewModel.insertShopItem("name", "9999999999999999999", "3.0")

        val value = viewModel.insertShopItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, returns success`() {
        viewModel.insertShopItem("name", "5", "3.0")

        val value = viewModel.insertShopItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}