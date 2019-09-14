package com.photoglyde.justincornelius.photoglyde.UI.fragment
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.content.Context
import android.os.Bundle
import androidx.core.app.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Data.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.UI.adapter.BindingHorizontal
import com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.UI.adapter.SimplePlayerViewHolder
import com.photoglyde.justincornelius.photoglyde.utilities.FeedAdapterListener
import com.photoglyde.justincornelius.photoglyde.utilities.Helper
import com.photoglyde.justincornelius.photoglyde.utilities.PlayerSelectorOption
import com.photoglyde.justincornelius.photoglyde.utilities.ScrollDownListener


import kotlinx.android.synthetic.main.staggered_feed_fragment.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*


class StaggeredFeedFragment : androidx.fragment.app.Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private lateinit var adapterProfile: FeedAdapter
    private lateinit var adapterHorizotal: BindingHorizontal
    private  var staggeredLayoutManager: StaggeredGridLayoutManager? = null
    private var listener: StaggeredFeedFragmentListener? = null


    private val onItemClickListenerVertical = object : FeedAdapter.OnItemClickListener {


        override fun onItemClick(view: View, position: Int, data:CoreData) {

            var ref1:String? = ""
            var ref2:String? = ""



            when(data?.type){

                GlobalValues.CATEGORY -> {
                    ref1 = PHOTOS
                    ref2 = data.categ_name
                    val increment = 2
                    ActivityCompat.startActivity(view.context,
                        Helper.deliverIntent(data, this@StaggeredFeedFragment.requireContext(), ref1, ref2, increment),
                        Helper.deliverOptions(
                            view,
                            this@StaggeredFeedFragment.requireActivity()
                        ).toBundle())

                }


                TYPE_PHOTO ->{

                    ActivityCompat.startActivity(view.context, Helper.deliverIntent(data, this@StaggeredFeedFragment.requireContext(), ref1, ref2, Load_NONE), Helper.deliverOptions(
                        view,
                        this@StaggeredFeedFragment.requireActivity()
                    ).toBundle())

                }

                "" ->{

                    ActivityCompat.startActivity(view.context, Helper.deliverIntent(data, this@StaggeredFeedFragment.requireContext(), ref1, ref2, Load_NONE), Helper.deliverOptions(
                        view,
                        this@StaggeredFeedFragment.requireActivity()
                    ).toBundle())

                }


                TYPE_VIDEO ->{
                    Helper.show(this@StaggeredFeedFragment.requireContext(), view.player, data)
                }



            }


        }
    }


    override fun onPause() {

        GlobalValues.whatsNew = false

        GlobalValues.recyclerStateNews = staggered_list.layoutManager?.onSaveInstanceState()

        super.onPause()

    }


    override fun onResume() {

        staggeredLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )

        if (GlobalValues.recyclerStateNews != null) restoreInstance() else initializeList(FEED, RANDOM,2)

        super.onResume()

    }

    private val feedAdapterListener = FeedAdapterListener

    override fun onStart() {
        super.onStart()
        GlobalValues.whatsNew = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.staggered_feed_fragment, container, false)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is StaggeredFeedFragmentListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnRageComicSelected.")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        GlobalValues.whatsNew = true

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface StaggeredFeedFragmentListener {
        fun staggeredFeedFragmentInteractor(animate:String)
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"
        val TAG: String = StaggeredFeedFragment::class.java.simpleName
        @JvmStatic
        fun newInstance() : StaggeredFeedFragment {
            val fragment = StaggeredFeedFragment()
            return fragment

        }
    }



    private fun initializeList(ref1:String?, ref2:String?, count:Int) {

        adapterProfile = FeedAdapter()
        staggered_list?.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter =  adapterProfile
            isNestedScrollingEnabled = true
            playerSelector = PlayerSelectorOption
        }

        adapterProfile.SetOnLock(feedAdapterListener)
        adapterProfile.setOnItemClickListener(onItemClickListenerVertical)

//blanked out for no wifi work session
        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()


        GlobalValues.test = GlobalValues.test + 1

        val liveData = initializedPagedListBuilder(config, ref1, ref2, count)
            .build()

        liveData.observe(this, Observer<PagedList<CoreData>> { pagedList ->
            adapterProfile.submitList(pagedList)
        })


        ScrollDownListener().show(this@StaggeredFeedFragment.requireContext(), staggered_list, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                listener?.staggeredFeedFragmentInteractor(animate)
            }
        })

    }

    private fun restoreInstance(){

        staggered_list?.apply {
            layoutManager = staggeredLayoutManager
            layoutManager?.onRestoreInstanceState(GlobalValues.recyclerStateNews)
            adapter = adapterProfile
        }
        adapterProfile.SetOnLock(feedAdapterListener)

        ScrollDownListener()
            .show(this@StaggeredFeedFragment.requireContext(), staggered_list, object : ScrollDownListener.HideShow{
                override fun onCallback(animate: String) {
                    listener?.staggeredFeedFragmentInteractor(animate)
                }
            })

        val selector = PlayerSelectorOption
        staggered_list?.playerSelector = selector

    }

    private fun initializedPagedListBuilder(config: PagedList.Config, ref1:String?, ref2:String?, count:Int):
            LivePagedListBuilder<String, CoreData> {

        val dataSourceFactory = object : DataSource.Factory<String, CoreData>() {
            override fun create(): DataSource<String, CoreData> {
                return ImageDataSource(ref1, ref2, count)
            }
        }
        return LivePagedListBuilder<String, CoreData>(dataSourceFactory, config)
    }








}
