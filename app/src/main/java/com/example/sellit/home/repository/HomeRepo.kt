package com.example.sellit.home.repository

import com.example.sellit.common.http.ApiClient
import com.example.sellit.common.http.RetrofitCallback
import com.example.sellit.home.api.HomeApi
import com.example.sellit.home.api.response.ClassifiedResponse
import com.example.sellit.home.contracts.HomeContracts

class HomeRepo(private val viewModel: HomeContracts.ViewModel) {

    private val homeApi: HomeApi = ApiClient.getInstance().createService(HomeApi::class.java)


    fun getImages() {

        homeApi.classified.enqueue(
            RetrofitCallback<ClassifiedResponse>(
                resultCallback = {
                    viewModel.onClassifiedSuccess(it)
                },
                errorCallback = {
                    viewModel.onClassifiedError(it)
                })
        )
    }



    fun getImagesPaginated(pageNo: Int) {
        homeApi.getClassifiedPaginated(pageNo).enqueue(
            RetrofitCallback<ClassifiedResponse>(
                resultCallback = {
                    viewModel.onClassifiedPaginatedSuccess(it)
                },
                errorCallback = {
                    viewModel.onClassifiedPaginatedError(it)
                })
        )
    }

}