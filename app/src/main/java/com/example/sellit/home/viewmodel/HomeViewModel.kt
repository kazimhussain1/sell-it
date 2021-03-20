package com.example.sellit.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sellit.home.api.response.ClassifiedItem
import com.example.sellit.home.api.response.ClassifiedResponse
import com.example.sellit.home.contracts.HomeContracts
import com.example.sellit.home.repository.HomeRepo

class HomeViewModel : ViewModel(), HomeContracts.ViewModel {

    private var pageNo: Int = 0
    private val homeRepo: HomeRepo = HomeRepo(this)

    private val mProgressBarVisible: MutableLiveData<Boolean> = MutableLiveData()
    private val mClassifiedItemResult: MutableLiveData<List<ClassifiedItem>> = MutableLiveData()
    private val mError: MutableLiveData<String> = MutableLiveData()

    val progressBarVisible: LiveData<Boolean>
        get() = mProgressBarVisible

    val classifiedItemResult: LiveData<List<ClassifiedItem>>
        get() = mClassifiedItemResult

    val error: LiveData<String>
        get() = mError


    fun getClassified() {
        pageNo = 1
        homeRepo.getImages()

        mProgressBarVisible.value = true
    }

    override fun onClassifiedPaginatedError(error: String) {
        mError.value = error
        mProgressBarVisible.value = false
    }

    override fun onClassifiedPaginatedSuccess(response: ClassifiedResponse) {
        val oldData = mClassifiedItemResult.value?.toMutableList()
        if (oldData != null) {
            oldData.addAll(response.classifiedItems)
            mClassifiedItemResult.value = oldData
        }
        else
            mClassifiedItemResult.value = response.classifiedItems

        mProgressBarVisible.value = false
    }

    override fun onClassifiedError(error: String) {

        mError.value = error
        mProgressBarVisible.value = false
    }

    override fun onClassifiedSuccess(response: ClassifiedResponse) {

        mClassifiedItemResult.value = response.classifiedItems
        mProgressBarVisible.value = false

    }




}