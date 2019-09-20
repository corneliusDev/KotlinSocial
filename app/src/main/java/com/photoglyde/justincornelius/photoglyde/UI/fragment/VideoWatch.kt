package com.photoglyde.justincornelius.photoglyde.UI.fragment

import android.content.Context
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.UI.adapter.OnItemClickListener
import com.photoglyde.justincornelius.photoglyde.utilities.ScrollDownListener
import com.photoglyde.justincornelius.photoglyde.data.*
import com.photoglyde.justincornelius.photoglyde.data.ImageDataSource
import kotlinx.android.synthetic.main.fragment_blank_fragment2.*

class VideoWatch : androidx.fragment.app.Fragment() {

    private var listener: OnVideoWatchListener? = null
    private lateinit var staggeredLayoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager
    private lateinit var adapter : FeedAdapter
    private lateinit var api: DatabaseReference

    override fun onResume() {
        super.onResume()

        GlobalValues.videoWatch = true
        GlobalValues.whatsNew = false
        if (GlobalValues.videoWatchState != null) {
            restoreInstance()
        }else{
            initializeList()
        }
    }

    override fun onStop() {
        GlobalValues.videoWatch = false
        super.onStop()
    }

    override fun onPause() {
        GlobalValues.videoWatch = false
        if (home_page_video_feed != null) GlobalValues.videoWatchState = home_page_video_feed.layoutManager?.onSaveInstanceState()
        super.onPause()
    }

    private val onItemClickListenerVertical = object : OnItemClickListener {
        override fun onItemClick(view: View, position: Int, data:CoreData) {


        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        GlobalValues.videoWatch = true

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_fragment2, container, false)
    }


    @Throws(RuntimeException::class)
    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = if (context is OnVideoWatchListener) context else { throw RuntimeException("$context must implement OnVideoWatchListener") }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnVideoWatchListener {
        fun onVideoWatchInteraction(animate:String)
    }

    companion object {
        val TAG: String = VideoWatch::class.java.simpleName
        @JvmStatic
        fun newInstance() : VideoWatch {
            return VideoWatch()
        }
    }


    private fun initializeList() {

        staggeredLayoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(1, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)
        home_page_video_feed?.layoutManager = staggeredLayoutManager
        adapter = FeedAdapter()
        home_page_video_feed.adapter = adapter
        adapter.setOnItemClickListener(onItemClickListenerVertical)
        //blanked out for no wifi work session
        val config = PagedList.Config.Builder().setPageSize(30).setEnablePlaceholders(false).build()


        val liveData = initializedPagedListBuilder(config).build()

        liveData.observe(this, Observer<PagedList<CoreData>> { pagedList ->
            adapter.submitList(pagedList)
        })

        ScrollDownListener().show(this@VideoWatch.requireContext(), home_page_video_feed, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                listener?.onVideoWatchInteraction(animate)
            }
        })

    }

    private fun restoreInstance(){
        staggeredLayoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(1, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)
        home_page_video_feed?.layoutManager = staggeredLayoutManager
        home_page_video_feed.layoutManager?.onRestoreInstanceState(GlobalValues.videoWatchState)
        home_page_video_feed?.adapter = adapter
        adapter.setOnItemClickListener(onItemClickListenerVertical)
    }

    private fun initializedPagedListBuilder(config: PagedList.Config): LivePagedListBuilder<String, CoreData> {
        val dataSourceFactory = object : DataSource.Factory<String, CoreData>() {
            override fun create(): DataSource<String, CoreData> {
                api = FirebaseDatabase.getInstance().getReference(VIDEOS)
                return ImageDataSource(api)
            }
        }
        return LivePagedListBuilder<String, CoreData>(dataSourceFactory, config)
    }
}
