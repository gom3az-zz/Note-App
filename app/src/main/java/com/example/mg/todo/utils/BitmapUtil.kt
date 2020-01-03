package com.example.mg.todo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object BitmapUtil {
    var mCurrentPhotoPath: String? = null
    fun encodedImage(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val b = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun decodeImage(mString: String): ByteArray {
        return Base64.decode(mString.toByteArray(), Base64.DEFAULT)
    }

    @Throws(IOException::class)
    fun createTempImageFile(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HH:mm:ss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir /* directory */
        )
        mCurrentPhotoPath = file.absolutePath
        return file
    }

    fun encodeDrawable(d: Drawable): String {
        val bitmap = (d as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapdata = stream.toByteArray()
        return Base64.encodeToString(bitmapdata, Base64.DEFAULT)
    }

    fun resamplePic(imagePath: String?): Bitmap {
        return resize(BitmapFactory.decodeFile(imagePath), .15f)
    }

    fun resize(bm: Bitmap, factor: Float): Bitmap {
        val newWidth = bm.width
        val newHeight = bm.height
        val dstWidth = (newWidth * factor).toInt() // scale to 15% from original size
        val dstHeight = (newHeight * factor).toInt()
        return Bitmap.createScaledBitmap(
                bm, dstWidth, dstHeight, true)
    }
}