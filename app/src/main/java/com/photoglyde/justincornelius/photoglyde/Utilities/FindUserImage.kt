package com.photoglyde.justincornelius.photoglyde.Utilities

import android.content.Context
import android.util.Log
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.Data.ImageClass
import java.lang.Exception


class FindUserImage(
    /**
     * The JPEG image
     */
    private val context: Context
) : Runnable {

    override fun run() {
        println("======== About to search")


            context.fileList().forEach {
                println("=========Looking File: " + it.toString())
                if (it.toString().contains(PREFIX_FILE)){
                    var image = ImageClass()
                    println("=========Found File: " + it.toString())
                    image.file = context.filesDir.path + it.toString()
                    GlobalVals.currentUser?.userImages?.add(image)
                }

            }


    }


}