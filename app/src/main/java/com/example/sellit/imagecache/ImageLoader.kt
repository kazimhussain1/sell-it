package com.example.sellit.imagecache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import android.widget.ImageView
import com.example.sellit.common.ContextService
import kotlinx.coroutines.*
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class ImageLoader private constructor() {

    private val diskCachePath = ContextService.getInstance().context.cacheDir.path +
            separator + "cached_images" + separator
    private val memoryCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        memoryCache = LruCache(cacheSize)
    }

    companion object {
        private var instance: ImageLoader? = null

        fun getInstance(): ImageLoader {
            if (instance == null)
                instance = ImageLoader()
            return instance!!
        }
    }


    fun loadImage(url: String, name: String, imageView: ImageView, callback: ((success:Boolean)->Unit)? = null) {

        if (imageView.tag != null) {
            val job = imageView.tag as Job
            job.cancel()
            imageView.tag = null
        }
        imageView.setImageBitmap(null)
        if (memoryCache[name] != null) {
            imageView.setImageBitmap(memoryCache[name])
            callback?.invoke(true)
        } else {
            imageView.tag = GlobalScope.launch(Dispatchers.Main) {
                var bitmap: Bitmap? = null
                withContext(Dispatchers.IO) {

                    val dir = File(diskCachePath)
                    if (!dir.exists())
                        dir.mkdirs()

                    val filePath = diskCachePath + name
                    val file = File(filePath)
                    if (file.exists()) {
                        bitmap = BitmapFactory.decodeFile(filePath)
                    } else {

                        try {
                            val url = URL(url)
                            bitmap = BitmapFactory.decodeStream(url.openStream())

                        } catch (e: IOException) {
                            e.printStackTrace()
                            callback?.invoke(false)
                        }
                    }
                }

                if (bitmap != null) {
                    memoryCache.put(name, bitmap)
                    imageView.setImageBitmap(bitmap)
                    callback?.invoke(true)

                    withContext(Dispatchers.IO) {
                        try {
                            val file = File(diskCachePath + name)
                            file.createNewFile()
                            FileOutputStream(file).use { out ->
                                bitmap!!.compress(
                                    Bitmap.CompressFormat.PNG,
                                    100,
                                    out
                                ) // bmp is your Bitmap instance
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            callback?.invoke(false)
                        }
                    }
                }


            }
        }


    }


}