package com.example.sellit.home.api.response


import com.google.gson.annotations.SerializedName

data class ClassifiedResponse(
    @SerializedName("pagination")
    val pagination: Pagination,
    @SerializedName("results")
    val classifiedItems: List<ClassifiedItem>
)