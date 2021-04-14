package com.practice.shoppytd.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.shoppytd.data.remote.responses.ImageResponse
import com.practice.shoppytd.data.local.ShopItem
import com.practice.shoppytd.repositories.ShopRepository
import com.practice.shoppytd.utils.Constants
import com.practice.shoppytd.utils.Event
import com.practice.shoppytd.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
        private val repository: ShopRepository
) : ViewModel() {

    val shopItems = repository.observeAllShopItem()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<NetworkResult<ImageResponse>>>()
    val images: LiveData<Event<NetworkResult<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    private val _insertShopItemStatus = MutableLiveData<Event<NetworkResult<ShopItem>>>()
    val insertShopItemStatus: LiveData<Event<NetworkResult<ShopItem>>> = _insertShopItemStatus

    fun setCurImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    fun deleteShopItem(shopItem: ShopItem) = viewModelScope.launch {
        repository.deleteShopItem(shopItem)
    }

    fun insertShopItemIntoDb(shopItem: ShopItem) = viewModelScope.launch {
        repository.insertShopItem(shopItem)
    }

    fun insertShopItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShopItemStatus.postValue(Event(NetworkResult.error("The fields must not be empty", null)))
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShopItemStatus.postValue(Event(NetworkResult.error("The name of the item" +
                    "must not exceed ${Constants.MAX_NAME_LENGTH} characters", null)))
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShopItemStatus.postValue(Event(NetworkResult.error("The price of the item" +
                    "must not exceed ${Constants.MAX_PRICE_LENGTH} characters", null)))
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _insertShopItemStatus.postValue(Event(NetworkResult.error("Please enter a valid amount", null)))
            return
        }
        val shoppingItem = ShopItem(name, amount, priceString.toFloat(), _curImageUrl.value ?: "")
        insertShopItemIntoDb(shoppingItem)
        setCurImageUrl("")
        _insertShopItemStatus.postValue(Event(NetworkResult.success(shoppingItem)))
    }

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }
        _images.value = Event(NetworkResult.loading(null))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.value = Event(response)
        }
    }
}