package com.example.sellit.utilities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.sellit.BuildConfig
import com.example.sellit.R
import com.example.sellit.common.ContextService
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class Utilities {

    companion object {


        fun showToast(context: Context, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun showToast(message: String, isShort: Boolean = false) {
            Toast.makeText(
                ContextService.getInstance().context,
                message,
                if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
            ).show()
        }


        fun showSnackBar(
            view: View,
            message: String?,
            isLong: Boolean
        ) {

            val s = Snackbar.make(
                view,
                message!!,
                if (isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
            )
            s.view
                .setBackgroundColor(ContextCompat.getColor(view.context, R.color.purple_500))
            s.show()
        }


        fun loadImageWithGlide(imageView: ImageView, imageUrl: String) {
            Glide.with(ContextService.getInstance().context)
                .load(imageUrl)
                .into(imageView)
        }

        fun loadImageWithGlideCallback(
            imageView: ImageView,
            imageUrl: String,
            callback: (success: Boolean) -> Unit
        ) {
            Glide.with(ContextService.getInstance().context)
                .load(imageUrl)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        callback(false)

                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        callback(true)

                        return false
                    }
                })
                .into(imageView)
        }


//        fun saveImage(image: Bitmap): String? {
//            var savedImagePath: String? = null
//            val imageFileName = "JPEG_" + "FILE_NAME" + ".jpg"
//            val storageDir = File(
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                    .toString()
//            )
//            var success = true
//            if (!storageDir.exists()) {
//                success = storageDir.mkdirs()
//            }
//            if (success) {
//                val imageFile = File(storageDir, imageFileName)
//                savedImagePath = imageFile.absolutePath
//                try {
//                    val fOut: OutputStream = FileOutputStream(imageFile)
//                    image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
//                    fOut.close()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//
//                // Add the image to the system gallery
//                galleryAddPic(savedImagePath)
//                //Toast.makeText(this, "IMAGE SAVED", Toast.LENGTH_LONG).show() // to make this working, need to manage coroutine, as this execution is something off the main thread
//            }
//            return savedImagePath
//        }
//
//        private fun galleryAddPic(imagePath: String?) {
//            imagePath?.let { path ->
//                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//                val f = File(path)
//                val contentUri: Uri = Uri.fromFile(f)
//                mediaScanIntent.data = contentUri
//                ContextService.getInstance().context.sendBroadcast(mediaScanIntent)
//            }
//        }


        fun shareImage(bitmap: Bitmap, context: Context){

            try {
                val cachePath = File(context.cacheDir, "images")
                cachePath.mkdirs() // don't forget to make the directory
                val stream =
                    FileOutputStream("$cachePath/image.png") // overwrites this image every time
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()

                val imagePath = File(context.cacheDir, "images")
                val newFile = File(imagePath, "image.png")
                val contentUri =
                    FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".fileprovider", newFile)

                if (contentUri != null) {
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                    shareIntent.setDataAndType(contentUri, context.contentResolver.getType(contentUri))
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                    context.startActivity(Intent.createChooser(shareIntent, "Choose an app"))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
            if (android.os.Build.VERSION.SDK_INT >= 29) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
                values.put(MediaStore.Images.Media.IS_PENDING, true)
                // RELATIVE_PATH and IS_PENDING are introduced in API 29.

                val uri: Uri? = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
                if (uri != null) {
                    saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)
                }
            } else {
                val directory = File(
                    Environment.getExternalStorageDirectory().toString() + separator + folderName
                )
                // getExternalStorageDirectory is deprecated in API 29

                if (!directory.exists()) {
                    directory.mkdirs()
                }
                val fileName = System.currentTimeMillis().toString() + ".png"
                val file = File(directory, fileName)
                saveImageToStream(bitmap, FileOutputStream(file))
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
            }
        }

        private fun contentValues() : ContentValues {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
//            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            return values
        }

        private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
            if (outputStream != null) {
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

}