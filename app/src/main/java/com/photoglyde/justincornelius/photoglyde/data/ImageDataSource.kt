

package com.photoglyde.justincornelius.photoglyde.data

import androidx.paging.PageKeyedDataSource
import com.google.firebase.database.*

import com.photoglyde.justincornelius.photoglyde.utilities.Helper


class ImageDataSource (reference: DatabaseReference) : PageKeyedDataSource<String, CoreData>() {

    private var api:DatabaseReference = reference

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, CoreData>) {

        api.orderByKey().limitToFirst(10).addListenerForSingleValueEvent(object : ValueEventListener {

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

        api.startAt(params.key).orderByKey().limitToFirst(10).addListenerForSingleValueEvent(object : ValueEventListener {

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


    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, CoreData>) {

        api.startAt(params.key).addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onCancelled(p0: DatabaseError) {

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