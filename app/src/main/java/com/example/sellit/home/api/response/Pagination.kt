package com.example.sellit.home.api.response


import com.google.gson.annotations.SerializedName
import javax.annotation.Nullable

data class Pagination(
    @SerializedName("key")
    @Nullable
    val key: Any?
)