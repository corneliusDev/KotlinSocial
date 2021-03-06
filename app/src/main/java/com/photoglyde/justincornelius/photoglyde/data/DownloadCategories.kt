package com.photoglyde.justincornelius.photoglyde.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.photoglyde.justincornelius.photoglyde.data.GlobalValues.listCateg


class DownloadCategories {

    interface DownloadCategories {
       fun onCallBack(bool:Boolean)
    }

    fun downloadCategs(finished: DownloadCategories){

        val ref = FirebaseDatabase.getInstance().getReference(GlobalValues.CATEGORY)
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
                finished.onCallBack(false)
            }

            override fun onDataChange(p0: DataSnapshot) {

               if (p0.exists()){

                   val categories  = ArrayList<CoreData?>()
                   val list = p0.children

                   list.forEach {
                       val categItem = it.getValue(CoreData::class.java)
                       if (categItem?.categ_image_uri != null) if (categItem.categ_image_uri.toString().isNotEmpty()) categories.add(categItem)
                   }

                   listCateg = categories
                   finished.onCallBack(true)

               }
            }
        })
    }

}