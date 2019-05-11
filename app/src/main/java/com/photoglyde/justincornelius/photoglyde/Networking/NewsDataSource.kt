package com.photoglyde.justincornelius.photoglyde.Networking

import android.arch.paging.PageKeyedDataSource
import android.arch.paging.PositionalDataSource.LoadRangeCallback
import android.support.annotation.NonNull
import android.arch.paging.PositionalDataSource.LoadRangeParams
import android.arch.paging.PositionalDataSource.computeInitialLoadSize
import android.arch.paging.PositionalDataSource.computeInitialLoadPosition
import android.arch.paging.PositionalDataSource
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals


class NewsDataSource : PageKeyedDataSource<String, NYCTimesDataResponse>() {

    private val api = NYTimeService.createService()

    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, NYCTimesDataResponse>) {



    api.getPosts("top-headlines", "us","24e30100299e4a86913d4e5dd975c015").enqueue(object : retrofit2.Callback<NYCTimesResponse>{

        override fun onFailure(call: retrofit2.Call<NYCTimesResponse>, t: Throwable) {

            println("====we have failed ${t.message}")
        }

        override fun onResponse(
            call: retrofit2.Call<NYCTimesResponse>,
            response: retrofit2.Response<NYCTimesResponse>

        ) {

            println("=====here are the results ${response} and ${response.message()}")
            val listing = response.body()?.articles

            listing?.map {

                println("=====find the null $it")
            }



            val items = listing?.map {it
                it


            } as MutableList?


            items?.map {
                GlobalVals.savedNews.add(it)
            }



            listing?.forEach {
                println("=====my id ${it.title}")
            }

            callback.onResult(items ?: listOf(),"","")
           // println("========body data ${listing.first().title} and ${listing?.size}")
        }
    })


    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, NYCTimesDataResponse>) {
        api.getPosts("2b0818c86c3046c99ff2e04cd5fc7458").enqueue(object : retrofit2.Callback<NYCTimesResponse>{

            override fun onFailure(call: retrofit2.Call<NYCTimesResponse>, t: Throwable) {

                println("====we have failed ${t.message}")
            }

            override fun onResponse(
                call: retrofit2.Call<NYCTimesResponse>,
                response: retrofit2.Response<NYCTimesResponse>
            ) {
                val listing = response.body()?.articles
                val items = listing?.map {
                    it
                } as MutableList?

                callback.onResult(items ?: listOf(),listing?.last()?.title.toString())
            }
        })
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, NYCTimesDataResponse>) {
        api.getPosts("2b0818c86c3046c99ff2e04cd5fc7458").enqueue(object : retrofit2.Callback<NYCTimesResponse>{

            override fun onFailure(call: retrofit2.Call<NYCTimesResponse>, t: Throwable) {

                println("====we have failed ${t.message}")
            }

            override fun onResponse(
                call: retrofit2.Call<NYCTimesResponse>,
                response: retrofit2.Response<NYCTimesResponse>
            ) {
                val listing = response.body()?.articles
                val items = listing?.map {
                    it
                } as MutableList?

                callback.onResult(items ?: listOf(),listing?.first()?.title.toString())
            }
        })
    }
}

//internal class ItemDataSource : PositionalDataSource<Item>() {
//    private fun computeCount(): Int {
//        // actual count code here
//    }
//
//    private fun loadRangeInternal(startPosition: Int, loadCount: Int): List<Item> {
//        // actual load code here
//    }
//
//    override fun loadInitial(
//        params: PositionalDataSource.LoadInitialParams,
//        callback: PositionalDataSource.LoadInitialCallback<Item>
//    ) {
//        val totalCount = computeCount()
//        val position = computeInitialLoadPosition(params, totalCount)
//        val loadSize = computeInitialLoadSize(params, position, totalCount)
//        callback.onResult(loadRangeInternal(position, loadSize), position, totalCount)
//    }
//
//    override fun loadRange(
//        params: LoadRangeParams,
//        callback: LoadRangeCallback<Item>
//    ) {
//        callback.onResult(loadRangeInternal(params.startPosition, params.loadSize))
//    }
//}