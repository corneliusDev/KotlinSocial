package com.photoglyde.justincornelius.photoglyde.UI.fragment

import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.content.Context
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.view.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.photoglyde.justincornelius.photoglyde.data.*
import com.photoglyde.justincornelius.photoglyde.data.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.UI.adapter.OnItemClickListener
import com.photoglyde.justincornelius.photoglyde.utilities.Helper
import com.photoglyde.justincornelius.photoglyde.utilities.PlayerSelectorOption
import com.photoglyde.justincornelius.photoglyde.utilities.ScrollDownListener
import kotlinx.android.synthetic.main.fragment_explore.*
import com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter as FeedAdapter1






class MainFeed : androidx.fragment.app.Fragment(){

    private var adapterFeed: com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter? = null
    private var staggeredLayoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager? = null
    private lateinit var api:DatabaseReference
    var listenerExplore: MainFeed.OnFragmentInteractionListenerExplore? = null

    private val onItemClickListenerVertical = object : OnItemClickListener {

        override fun onItemClick(view: View, position: Int, data:CoreData) {

            if (data.type != TYPE_VIDEO) ActivityCompat.startActivity(view.context, Helper.deliverIntent(data, this@MainFeed.requireContext(), null, null, 0),
                Helper.deliverOptions(view, this@MainFeed.requireActivity())?.toBundle())
        }
    }

    override fun onPause() {
        super.onPause()

        GlobalValues.cameFromCateg = false
        if (profile_list_explore != null) GlobalValues.recyclerState2 = profile_list_explore.layoutManager?.onSaveInstanceState()

    }

    override fun onResume() {
        super.onResume()

        setRoutes()
        staggeredLayoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(1, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)
        if (GlobalValues.recyclerState2 != null && adapterFeed != null) restoreInstance() else initializeList()


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onDetach() {
        super.onDetach()
        listenerExplore = null
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listenerExplore = if (context is OnFragmentInteractionListenerExplore) context else { throw RuntimeException("$context must implement OnVideoWatchListener") }
    }

    interface OnFragmentInteractionListenerExplore {
        fun onFragmentInteractionExplore(fragment: androidx.fragment.app.Fragment, animateDirection:String)
    }

    companion object {
        val TAG: String = MainFeed::class.java.simpleName
        @JvmStatic
        fun newInstance() : MainFeed {
            return MainFeed()
        }
    }

    private fun restoreInstance(){


        profile_list_explore?.apply {
            layoutManager = staggeredLayoutManager
            layoutManager?.onRestoreInstanceState(GlobalValues.recyclerState2)
            adapter = adapterFeed
        }

//        adapterFeed.setOnItemClickListener(onItemClickListenerVertical)
//        adapterFeed.SetOnLock(longListener)

        ScrollDownListener().show(this@MainFeed.requireContext(), profile_list_explore, object : ScrollDownListener.HideShow{
                override fun onCallback(animate: String) {
                    listenerExplore?.onFragmentInteractionExplore(this@MainFeed, animate)
                }
            })

        val selector = PlayerSelectorOption
        profile_list_explore?.playerSelector = selector

    }

    private fun initializeList() {

        adapterFeed = com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter()
        staggeredLayoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(1,androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)
        profile_list_explore?.layoutManager = staggeredLayoutManager
        profile_list_explore.adapter = adapterFeed
        if (GlobalValues.recyclerState2 != null) staggeredLayoutManager?.onRestoreInstanceState(GlobalValues.recyclerState2)
        adapterFeed!!.setOnItemClickListener(onItemClickListenerVertical)
     //   adapterFeed!!.SetOnLock(longListener)

        //blanked out for no wifi work session
        val config = PagedList.Config.Builder().setPageSize(30).setEnablePlaceholders(false).build()

        val liveData = initializedPagedListBuilder(config).build()
        liveData.observe(this, Observer<PagedList<CoreData>> { pagedList -> adapterFeed!!.submitList(pagedList) })

        ScrollDownListener().show(this@MainFeed.requireContext(), profile_list_explore, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                listenerExplore?.onFragmentInteractionExplore(this@MainFeed, animate)
            }
        })

    }




    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, CoreData> {

        val dataSourceFactory = object : DataSource.Factory<String, CoreData>() {
            override fun create(): DataSource<String, CoreData> {
                api = FirebaseDatabase.getInstance().getReference(FEED).child(EXPLORE)
                return ImageDataSource(api)
            }
        }
        return LivePagedListBuilder<String, CoreData>(dataSourceFactory, config)
    }

    private fun setRoutes(){
        GlobalValues.whatsNew = false
        GlobalValues.cameFromCateg = true
        GlobalValues.cameFromCateg = false
    }






    }




