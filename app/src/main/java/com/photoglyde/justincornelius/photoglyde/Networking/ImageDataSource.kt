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

package com.photoglyde.justincornelius.photoglyde.Networking

import android.arch.paging.PageKeyedDataSource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.photoglyde.justincornelius.photoglyde.Data.CoreUnSplash
import com.photoglyde.justincornelius.photoglyde.Data.FEED
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.Data.GrabImageData
import com.photoglyde.justincornelius.photoglyde.Helper


class ImageDataSource (child1:String?, child2:String?, nodeCount:Int?) : PageKeyedDataSource<String, CoreUnSplash>() {


    private var mDataBaseReference = FirebaseDatabase.getInstance().getReference(child1.toString())



init {

    when(nodeCount){

        1 -> mDataBaseReference = FirebaseDatabase.getInstance().getReference(child1.toString())

        2 -> mDataBaseReference = FirebaseDatabase.getInstance().getReference(child1.toString()).child(child2.toString())

    }


}








    override fun loadInitial(

            params: LoadInitialParams<String>,
            callback: LoadInitialCallback<String, CoreUnSplash>) {


        mDataBaseReference.orderByKey().limitToFirst(10).addListenerForSingleValueEvent(
    object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
           println("========operation is cancelled")
        }

        override fun onDataChange(p0: DataSnapshot) {
            if (p0.exists()) {



                val listing = p0

                println()




                var redditPosts = listing.children.map {
                    it.getValue(CoreUnSplash::class.java)

                }



                                                    println("here is my list check1 ${redditPosts.size}")



                                    if (GlobalVals.whatsNew && !GlobalVals.cameFromExa){
                                        redditPosts = Helper.main(redditPosts)
                                    }else if (GlobalVals.videoWatch){
                                        redditPosts = Helper.main(redditPosts)
                                    }else if (!GlobalVals.cameFromExa){
                                        redditPosts = Helper.main(redditPosts)
                                    }


                                    println("here is my list check2 ${redditPosts.size}")
//
//                                    redditPosts.forEach {
//
//                                        GlobalVals.imageClassUser.add(it)
//
//                                    }




                callback.onResult(redditPosts, listing.children.first().key, listing.children.last().key)

println("================check data  ${p0.children.first().key} and last ${p0.children.last().key}")
                println("======list initial ${p0.childrenCount}")
            }
        }
    })
}
    
    override fun loadAfter(
            params: LoadParams<String>,
            callback: LoadCallback<String, CoreUnSplash>) {
        println("=====on load after ${params.key}")
        mDataBaseReference.startAt(params.key).orderByKey().limitToFirst(10)
                .addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {


                                    println("======list2after ${p0.childrenCount}")

//
                                    val listing = p0





                                   // print("here is my list ${GlobalVals.imageClassUser.size}")
                                    var redditPosts = listing.children.map {
                                        it.getValue(CoreUnSplash::class.java)
                                    }


                                    if (GlobalVals.whatsNew && !GlobalVals.cameFromExa){
                                        redditPosts = Helper.main(redditPosts)
                                    }else if (GlobalVals.videoWatch){
                                        redditPosts = Helper.main(redditPosts)
                                    }else if (!GlobalVals.cameFromExa){
                                      //  redditPosts = Helper.main(redditPosts)
                                    }

//                                    print("here is my list check1 ${redditPosts.size}")
//
//
//                                    redditPosts = Helper.main(redditPosts)
//
//                                    print("here is my list check2 ${redditPosts.size}")

//                                    redditPosts.forEach {
//
//                                        GlobalVals.imageClassUser.add(it)
//
//                                    }

                                  //  println("================grabbing list ${GlobalVals.imageClassUser.size} and ${GlobalVals.imageClassUser.first()}")

                                    println("=====values first and last ${listing.children.first().key} and ${listing.children.last().key} and ${p0.key}")



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
        println("=====on load before ${params.key}")
        mDataBaseReference.startAt(params.key)
                .addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {

                                    println("===========testing values1 ---- ${p0.childrenCount}")

                                    println("===========testing values2 ---- ${p0.children.forEach {
                                        //println("==============inside 4 ${it.key} and ${it.value}")
                                    }}")

                                    println("===========testing values3 ---- ${p0.key}")


                                    println("======list2 ${p0.value}")

//
                                    val listing = p0
                                    val new = listing.children.map { println("=======check setter ${it.value}") }

                                    print("here is my list ${new.size}")
                                    val redditPosts = listing.children.map { it.getValue(CoreUnSplash::class.java) }

                                    println("=====values first and last ${listing.children.first().key} and ${listing.children.last().key}")
                                        callback.onResult(redditPosts, listing.children.first().key)




                                }
                            }
                        })
    }
}