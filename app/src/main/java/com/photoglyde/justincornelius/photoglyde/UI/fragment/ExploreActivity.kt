package com.photoglyde.justincornelius.photoglyde.UI.fragment

import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.content.Context
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.view.*
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.photoglyde.justincornelius.photoglyde.data.*
import com.photoglyde.justincornelius.photoglyde.UI.custom.ImagePreview
import com.photoglyde.justincornelius.photoglyde.data.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.UI.adapter.OnItemClickListener
import com.photoglyde.justincornelius.photoglyde.UI.adapter.OnItemLockClickListener
import com.photoglyde.justincornelius.photoglyde.utilities.Helper
import com.photoglyde.justincornelius.photoglyde.utilities.PlayerSelectorOption
import com.photoglyde.justincornelius.photoglyde.utilities.ScrollDownListener
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.full_view.view.*
import java.util.*
import com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter as FeedAdapter1






class ExploreActivity : androidx.fragment.app.Fragment(){

    private lateinit var adapterFeed: com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter
    private var staggeredLayoutManager: androidx.recyclerview.widget.StaggeredGridLayoutManager? = null
    private lateinit var api:DatabaseReference
    var listenerExplore: ExploreActivity.OnFragmentInteractionListenerExplore? = null
    private var longListener = object : OnItemLockClickListener {

        override fun onItemLongClick(view: View, position: Int, core:CoreData?) {

           GlobalValues.currentCore = core

           ImagePreview().show(this@ExploreActivity.requireContext(), view.placeImageHere, core, object : ImagePreview.ExpandActivity{

                override fun onCallback(action:String) {

                when(action){

                    MAP_OPEN ->{
                        listenerExplore?.onFragmentInteractionExplore(this@ExploreActivity, MAP_OPEN)
                    }

                    EXPANDED_IMAGE ->{
                        ActivityCompat.startActivity(view.context, Helper.deliverIntent(core, this@ExploreActivity.requireContext(), null, null, Load_NONE),
                            Helper.deliverOptions(view, this@ExploreActivity.requireActivity())?.toBundle())
                    }
                }
            }
        })
            expanded_image_holder.bringToFront()
            expanded_linear.bringToFront()
            expanded_image.bringToFront()
        }

    }

    private val onItemClickListenerVertical = object : OnItemClickListener {

        override fun onItemClick(view: View, position: Int, data:CoreData) {

            if (data.type != TYPE_VIDEO) ActivityCompat.startActivity(view.context, Helper.deliverIntent(data, this@ExploreActivity.requireContext(), null, null, 0),
                Helper.deliverOptions(view, this@ExploreActivity.requireActivity())?.toBundle())
        }
    }

    override fun onPause() {
        super.onPause()

        GlobalValues.cameFromExa = false
        if (profile_list_explore != null) GlobalValues.recyclerState2 = profile_list_explore.layoutManager?.onSaveInstanceState()

    }

    override fun onResume() {
        super.onResume()
        GlobalValues.whatsNew = false
        GlobalValues.cameFromExa = true
        GlobalValues.cameFromExa = false
        //staggeredLayoutManager?: androidx.recyclerview.widget.StaggeredGridLayoutManager(1, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)


            staggeredLayoutManager = androidx.recyclerview.widget.StaggeredGridLayoutManager(1, androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL)


        if (GlobalValues.recyclerState2 != null) restoreInstance() else initializeList()


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
        val TAG: String = ExploreActivity::class.java.simpleName
        @JvmStatic
        fun newInstance() : ExploreActivity {
            return ExploreActivity()
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

        ScrollDownListener().show(this@ExploreActivity.requireContext(), profile_list_explore, object : ScrollDownListener.HideShow{
                override fun onCallback(animate: String) {
                    listenerExplore?.onFragmentInteractionExplore(this@ExploreActivity, animate)
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
        adapterFeed.setOnItemClickListener(onItemClickListenerVertical)
        adapterFeed.SetOnLock(longListener)

        //blanked out for no wifi work session
        val config = PagedList.Config.Builder().setPageSize(30).setEnablePlaceholders(false).build()

        val liveData = initializedPagedListBuilder(config).build()
        liveData.observe(this, Observer<PagedList<CoreData>> { pagedList -> adapterFeed.submitList(pagedList) })

        ScrollDownListener().show(this@ExploreActivity.requireContext(), profile_list_explore, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                listenerExplore?.onFragmentInteractionExplore(this@ExploreActivity, animate)
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






    }




