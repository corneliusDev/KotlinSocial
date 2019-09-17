package com.photoglyde.justincornelius.photoglyde.UI.fragment

import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.UI.adapter.OnItemClickListener
import com.photoglyde.justincornelius.photoglyde.utilities.ScrollDownListener
import com.photoglyde.justincornelius.photoglyde.data.*
import com.photoglyde.justincornelius.photoglyde.data.ImageDataSource
import kotlinx.android.synthetic.main.fragment_blank_fragment2.*

class BlankFragment2 : androidx.fragment.app.Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var staggeredLayoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager
    private lateinit var adapter : FeedAdapter


    override fun onResume() {
        super.onResume()

        GlobalValues.videoWatch = true
        if (GlobalValues.videoWatchState != null) {
            restoreInstance()
        }else{
            initializeList(VIDEOS, FEED, 1)
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank_fragment2, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is OnFragmentInteractionListener) {
//            listener = context
//        } else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
//    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        val TAG: String = BlankFragment2::class.java.simpleName
        @JvmStatic
        fun newInstance() : BlankFragment2 {
            return BlankFragment2()
        }
    }


    private fun initializeList(current_ref1:String?, current_ref2: String?, nodeCount:Int?) {

        staggeredLayoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(
            1,
            androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
        )
        home_page_video_feed?.layoutManager = staggeredLayoutManager
        adapter = FeedAdapter()
        home_page_video_feed.adapter = adapter
        adapter.setOnItemClickListener(onItemClickListenerVertical)
//blanked out for no wifi work session
        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()
        GlobalValues.test = GlobalValues.test + 1



        val liveData = initializedPagedListBuilder(config, current_ref1, current_ref2, nodeCount).build()


        liveData.observe(this, Observer<PagedList<CoreData>> { pagedList ->


            adapter.submitList(pagedList)


        })

        ScrollDownListener()
            .show(this@BlankFragment2.requireContext(), home_page_video_feed, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
              //  listener?.onFragmentInteractionExplore(this@BlankFragment2, animate)
            }
        })

    }

    private fun restoreInstance(){

        staggeredLayoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(
            1,
            androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
        )
        home_page_video_feed?.layoutManager = staggeredLayoutManager
        home_page_video_feed.layoutManager?.onRestoreInstanceState(GlobalValues.videoWatchState)
        home_page_video_feed?.adapter = adapter
        adapter.setOnItemClickListener(onItemClickListenerVertical)

    }

    private fun initializedPagedListBuilder(config: PagedList.Config, current_ref1:String?, current_ref2:String?, nodeCount: Int?):
            LivePagedListBuilder<String, CoreData> {

        val dataSourceFactory = object : DataSource.Factory<String, CoreData>() {
            override fun create(): DataSource<String, CoreData> {

                return ImageDataSource(
                    current_ref1,
                    current_ref2,
                    nodeCount
                )
            }
        }
        return LivePagedListBuilder<String, CoreData>(dataSourceFactory, config)
    }
}
