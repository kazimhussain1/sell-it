package com.example.sellit.detail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sellit.home.api.response.ClassifiedItem

class DetailViewModel : ViewModel() {

    private val mImageData: MutableLiveData<ClassifiedItem> = MutableLiveData()
    private val mPosition: MutableLiveData<Int> = MutableLiveData()


    val imageData: LiveData<ClassifiedItem>
        get() = mImageData

    val position: LiveData<Int>
        get() = mPosition


    fun setProductData(data: ClassifiedItem?) {
        if (data != null)
            mImageData.value = data
    }

    fun setPosition(position: Int) {
        mPosition.value = position
    }

    fun setPositionIfNull(position: Int) {

        if (mPosition.value == null)
            mPosition.value = position
    }

}