package com.photoglyde.justincornelius.photoglyde.Networking

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.photoglyde.justincornelius.photoglyde.Data.PHOTOS

class NodeExist {


    interface SendResult {
        fun onCallback(bool:Boolean)
    }


    fun ifNodeExist(node:String?, callBack:SendResult) {

        var boolReturn = false

        val ref = FirebaseDatabase.getInstance().getReference(PHOTOS)
        val bool = ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){

                    println("=======datasnapshot $p0")

                    p0.children.forEach {

                        println("======== on going search ${it.key} and $node and ${p0.key.toString() == node}")
                        if (it.key.toString() == node){
                            println("====== we true")
                            boolReturn = true
                            callBack.onCallback(boolReturn)
                            return
                        }
                    }

                }
            }
        })




    }


}