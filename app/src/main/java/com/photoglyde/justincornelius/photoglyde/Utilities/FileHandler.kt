package com.photoglyde.justincornelius.photoglyde.Utilities

import android.content.Context
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Helper
import com.photoglyde.justincornelius.photoglyde.Networking.PostUN
import java.io.IOException
import java.lang.Exception
import android.graphics.drawable.Drawable
import com.squareup.picasso.Picasso
import android.graphics.Bitmap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import android.R.attr.bitmap
import android.widget.Toast
import com.squareup.picasso.Target

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
        private val file: String?,

        private val uri_image:String?
    ) : Runnable {

        override fun run() {

            println("************ Running Background THread")

            val target: Target = object : Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    Toast.makeText(context, "Prapared", Toast.LENGTH_SHORT).show()
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }

                override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                    println("************ Bitmap")
                    Toast.makeText(context, "SUccess", Toast.LENGTH_SHORT).show()
                    if (bitmap != null){
                    saveme(context, file, bitmap)
                    }
                }
            }

            Picasso.get().load(uri_image).into(target)

        }


    }


    fun saveme(context: Context, file: String?, bitmap: Bitmap){

        try {
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


