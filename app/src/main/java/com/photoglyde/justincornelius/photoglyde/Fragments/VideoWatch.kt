package com.photoglyde.justincornelius.photoglyde.Fragments

import android.arch.lifecycle.Observer
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.photoglyde.justincornelius.photoglyde.R
import android.support.v7.widget.StaggeredGridLayoutManager
import com.photoglyde.justincornelius.photoglyde.Adapters.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.Adapters.ProfileAdapter
import com.photoglyde.justincornelius.photoglyde.Adapters.ScrollDownListener
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Networking.ImageDataSource
import kotlinx.android.synthetic.main.fragment_blank_fragment2.*
import kotlinx.android.synthetic.main.toolbar.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BlankFragment2.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BlankFragment2.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class BlankFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    private lateinit var adapterProfile:ProfileAdapter
    private lateinit var adapter : FeedAdapter


    override fun onResume() {
        super.onResume()

        GlobalVals.videoWatch = true
        if (GlobalVals.videoWatchState != null) {
            staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            home_page_video_feed?.layoutManager = staggeredLayoutManager
            home_page_video_feed.layoutManager?.onRestoreInstanceState(GlobalVals.videoWatchState)
            home_page_video_feed?.adapter = adapter
            adapter.setOnItemClickListener(onItemClickListenerVertical)
        }else{
            initializeList(VIDEOS, FEED, 1)
        }
    }

    override fun onStop() {
        GlobalVals.videoWatch = false
        super.onStop()
    }

    override fun onPause() {
        GlobalVals.videoWatch = false
        if (home_page_video_feed != null) GlobalVals.videoWatchState = home_page_video_feed.layoutManager?.onSaveInstanceState()
        super.onPause()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private val onItemClickListenerVertical = object : FeedAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, data:CoreUnSplash) {










            println("===========on video click")


        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)?.searchBar?.visibility = View.INVISIBLE
        GlobalVals.videoWatch = true


       // setUpAdapter()
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment2.
         */
        val TAG: String = BlankFragment2::class.java.simpleName
        @JvmStatic
        fun newInstance() : BlankFragment2 {
            val fragment = BlankFragment2()


            return fragment


        }
    }

    fun setUpAdapter(){

        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        home_page_video_feed.layoutManager = staggeredLayoutManager
        home_page_video_feed.isNestedScrollingEnabled = true
        adapterProfile = ProfileAdapter(
            this@BlankFragment2.requireContext(), GlobalVals.upSplash,
            arrayListOf(6,6,6,6,6,6)
        )
        home_page_video_feed.adapter = adapterProfile
    }

    private fun initializeList(current_ref1:String?, current_ref2: String?, nodeCount:Int?) {

        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        home_page_video_feed?.layoutManager = staggeredLayoutManager
        adapter = FeedAdapter()
        home_page_video_feed.adapter = adapter
        println("======this is null ${GlobalVals.recyclerState2}")
        if (GlobalVals.recyclerState2 != null) {
            home_page_video_feed.layoutManager?.onRestoreInstanceState(GlobalVals.recyclerState2)
        }

        adapter.setOnItemClickListener(onItemClickListenerVertical)

        val layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)






//blanked out for no wifi work session
        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()
        GlobalVals.test = GlobalVals.test + 1



        val liveData = initializedPagedListBuilder(config, current_ref1, current_ref2, nodeCount).build()

        3
        liveData.observe(this, Observer<PagedList<CoreUnSplash>> { pagedList ->


            adapter.submitList(pagedList)


        })

        ScrollDownListener().show(this@BlankFragment2.requireContext(), home_page_video_feed, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                println("=======we have call back $animate and $listenerExplore")
                listenerExplore?.onFragmentInteractionExplore(this@BlankFragment2, animate)
            }
        })

    }

    private fun initializedPagedListBuilder(config: PagedList.Config, current_ref1:String?, current_ref2:String?, nodeCount: Int?):
            LivePagedListBuilder<String, CoreUnSplash> {

        val dataSourceFactory = object : DataSource.Factory<String, CoreUnSplash>() {
            override fun create(): DataSource<String, CoreUnSplash> {

                return ImageDataSource(current_ref1, current_ref2, nodeCount)
            }
        }
        return LivePagedListBuilder<String, CoreUnSplash>(dataSourceFactory, config)
    }
}
