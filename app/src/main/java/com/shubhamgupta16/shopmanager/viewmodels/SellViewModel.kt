package com.shubhamgupta16.shopmanager.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shubhamgupta16.shopmanager.common.RecyclerItem
import com.shubhamgupta16.shopmanager.models.ProductModel
import com.shubhamgupta16.shopmanager.models.SellModel
import com.shubhamgupta16.shopmanager.repository.SellRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SellViewModel @Inject constructor(private val sellRepository: SellRepository) : ViewModel() {

    private val mSearchList = ArrayList<ProductModel>()
    private val mLiveSearchList = MediatorLiveData<Unit>()
    private val mCartList = ArrayList<SellModel>()
    private val mLiveCartList = MutableLiveData<RecyclerItem>()
    private val mLiveTotalPrice = MutableLiveData<Array<Double>>()

    val liveSearchList: LiveData<Unit>
        get() = mLiveSearchList
    val cartList:List<SellModel>
    get() = mCartList
    val searchList:List<ProductModel>
        get() = mSearchList
    val liveCartList: LiveData<RecyclerItem>
        get() = mLiveCartList
    val liveTotalPrice: LiveData<Array<Double>>
        get() = mLiveTotalPrice

    fun search(query: String?) {
        mSearchList.clear()
//        if long process, set value here to update the ui
        mLiveSearchList.addSource(sellRepository.searchProducts(query)) {
            mSearchList.addAll(it)
            mLiveSearchList.value = null
        }
    }

    fun addNewProduct(sellModel: SellModel) {
        mCartList.add(sellModel)
        mLiveCartList.value = RecyclerItem(RecyclerItem.CASE_INSERTED, cartList.lastIndex)
    }

    fun updateTotalPrice() {
        mLiveTotalPrice.value = arrayOf(cartList.sumOf { it.amount.toDouble() * it.quantity },
            cartList.sumOf { it.rate.toDouble() * it.quantity })
    }

    fun removeProduct(position: Int) {
        if (cartList.isEmpty()) return
        mCartList.removeAt(position)
        mLiveCartList.value = RecyclerItem(RecyclerItem.CASE_REMOVED, position)
    }
}