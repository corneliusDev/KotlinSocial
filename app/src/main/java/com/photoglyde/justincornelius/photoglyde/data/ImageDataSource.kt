

package com.photoglyde.justincornelius.photoglyde.data

import androidx.paging.PageKeyedDataSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.photoglyde.justincornelius.photoglyde.utilities.Helper


class ImageDataSource (child1:String?, child2:String?, nodeCount:Int?) : PageKeyedDataSource<String, CoreData>() {


    private var mDataBaseReference = FirebaseDatabase.getInstance().getReference(child1.toString())



    init {

        when(nodeCount){

            1 -> mDataBaseReference = FirebaseDatabase.getInstance().getReference(child1.toString())

            2 -> mDataBaseReference = FirebaseDatabase.getInstance().getReference(child1.toString()).child(child2.toString())

        }

    }



    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, CoreData>) {
        mDataBaseReference.orderByKey().limitToFirst(10).addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if (p0.exists()) {

                    val listing = p0

                    var redditPosts = listing.children.map {
                        it.getValue(CoreData::class.java)

                    }

                    if (GlobalValues.whatsNew && !GlobalValues.cameFromExa){
                        redditPosts = Helper.main(redditPosts)
                    }else if (GlobalValues.videoWatch){
                        redditPosts = Helper.main(redditPosts)
                    }else if (!GlobalValues.cameFromExa){
                        redditPosts = Helper.main(redditPosts)
                    }

                    callback.onResult(redditPosts, listing.children.first().key, listing.children.last().key)

                }
            }
        })
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, CoreData>) {

        mDataBaseReference.startAt(params.key).orderByKey().limitToFirst(10).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {

                    val listing = p0

                    var redditPosts = listing.children.map {
                        it.getValue(CoreData::class.java)
                    }


                    if (GlobalValues.whatsNew && !GlobalValues.cameFromExa){
                        redditPosts = Helper.main(redditPosts)
                    }else if (GlobalValues.videoWatch){
                        redditPosts = Helper.main(redditPosts)
                    }else if (!GlobalValues.cameFromExa){
                        //  redditPosts = Helper.main(redditPosts)
                    }

                    if (params.key == listing.children.last().key){
                        callback.onResult(null ?: listOf(), listing.children.last().key)
                    }else{
                        callback.onResult(redditPosts, listing.children.last().key)
                    }




                }
            }
        })
    }


    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, CoreData>) {
        mDataBaseReference.startAt(params.key)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
//
                            val listing = p0

                            val redditPosts = listing.children.map { it.getValue(CoreData::class.java) }

                            callback.onResult(redditPosts, listing.children.first().key)




                        }
                    }
                })
    }
}