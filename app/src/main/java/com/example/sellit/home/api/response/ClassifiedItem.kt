package com.example.sellit.home.api.response


import android.annotation.SuppressLint
import android.os.Parcelable
import com.example.sellit.R
import com.example.sellit.common.ContextService
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class ClassifiedItem(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("image_ids")
    val imageIds: List<String>,
    @SerializedName("image_urls")
    val imageUrls: List<String>,
    @SerializedName("image_urls_thumbnails")
    val imageUrlsThumbnails: List<String>,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("uid")
    val uid: String
) : Parcelable {

    companion object {
        @SuppressLint("SimpleDateFormat")
        val fromDate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        @SuppressLint("SimpleDateFormat")
        val toDate: SimpleDateFormat = SimpleDateFormat("dd MMM yyyy")
    }

    fun getPostedOn(): String = ContextService.getInstance().context.getString(
        R.string.posted_on,
        toDate.format(fromDate.parse(this.createdAt)!!)
    )
}