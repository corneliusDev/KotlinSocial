package com.photoglyde.justincornelius.photoglyde.Utilities

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.IOException
import java.lang.Exception
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.net.HttpURLConnection


val PREFIX_FILE = "user_image"
val UNDER = "_"

class FileHandler (
    /**
     * The JPEG image
     */
    private val context: Context,
    /**
     * The file we save the image into.
     */
    private val file: String,

    private val uri_image_string:String?

) : Thread(Runnable {

    try {
        //Your code goes here

        val bitmap = uri_image_string?.let { getBitmapFromURL(it) }
//
        if (bitmap != null) saveme(context, file, bitmap)

    } catch (e: Exception) {
        e.printStackTrace()
    }

})

    fun getBitmapFromURL(src: String): Bitmap? {
        try {
            val url = java.net.URL(src)
            val connection = url
                .openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input = connection.getInputStream()
            return BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

    }

    fun saveme(context: Context, file: String?, bitmap: Bitmap){

        try {

            println("SAVING FILE")
            context.openFileOutput(PREFIX_FILE + UNDER + file, Context.MODE_PRIVATE).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, it)
                it.flush()
                it.close()
            }

        } catch (e: Exception) {
            println("======== we are attempting to catch any exceptions")
            Log.e("", e.toString())
        } finally {
            context.fileList().forEach {
                println("*******" + it)

            }
            println("======== we are attempting finally")
        }
    }








