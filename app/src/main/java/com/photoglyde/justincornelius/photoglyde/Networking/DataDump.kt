package com.photoglyde.justincornelius.photoglyde.Networking

import com.google.firebase.database.FirebaseDatabase
import com.photoglyde.justincornelius.photoglyde.Data.PHOTOS
import com.photoglyde.justincornelius.photoglyde.Data.UnSplashBegin

class DataDump {


    fun dumpNow(node:String?, info:UnSplashBegin){

        val ref = FirebaseDatabase.getInstance().getReference(PHOTOS).child(node.toString())

        if (info.results.isNotEmpty()) {
            for (i in info.results) {

                ref.push().setValue(i)

            }
        }


    }


}