/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.photoglyde.justincornelius.photoglyde.Data

import androidx.paging.PageKeyedDataSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.photoglyde.justincornelius.photoglyde.utilities.Helper


class ImageDataSource (child1:String?, child2:String?, nodeCount:Int?) : PageKeyedDataSource<String, CoreUnSplash>() {


    private var mDataBaseReference = FirebaseDatabase.getInstance().getReference(child1.toString())



    init {

        when(nodeCount){

            1 -> mDataBaseReference = FirebaseDatabase.getInstance().getReference(child1.toString())

            2 -> mDataBaseReference = FirebaseDatabase.getInstance().getReference(child1.toString()).child(child2.toString())

        }

    }



    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, CoreUnSplash>) {
        mDataBaseReference.orderByKey().limitToFirst(10).addListenerForSingleValueEvent(
    object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {

            if (p0.exists()) {

                val listing = p0

                var redditPosts = listing.children.map {
                    it.getValue(CoreUnSplash::class.java)

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
    
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, CoreUnSplash>) {

        mDataBaseReference.startAt(params.key).orderByKey().limitToFirst(10)
                .addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {

                                    val listing = p0

                                    var redditPosts = listing.children.map {
                                        it.getValue(CoreUnSplash::class.java)
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
            callback: LoadCallback<String, CoreUnSplash>) {
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

                                    val redditPosts = listing.children.map { it.getValue(CoreUnSplash::class.java) }

                                    callback.onResult(redditPosts, listing.children.first().key)




                                }
                            }
                        })
    }
}