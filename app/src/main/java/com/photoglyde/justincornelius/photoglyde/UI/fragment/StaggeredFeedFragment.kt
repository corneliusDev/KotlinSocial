package com.photoglyde.justincornelius.photoglyde.UI.fragment
import android.R.attr.*
import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Dialog
import androidx.lifecycle.Observer
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.content.Context
import android.os.Bundle
import androidx.core.app.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.photoglyde.justincornelius.photoglyde.data.*
import com.photoglyde.justincornelius.photoglyde.data.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.UI.adapter.BindingHorizontal
import com.photoglyde.justincornelius.photoglyde.UI.adapter.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.UI.adapter.OnItemClickListener


import kotlinx.android.synthetic.main.staggered_feed_fragment.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*
import android.hardware.SensorManager
import com.google.android.exoplayer2.ui.PlayerView
import com.photoglyde.justincornelius.photoglyde.UI.custom.ImagePreviewerUtils
import com.photoglyde.justincornelius.photoglyde.utilities.*
import android.content.pm.ActivityInfo
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView.ScaleType
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.*

import kotlinx.android.synthetic.main.view_player.*


class StaggeredFeedFragment : androidx.fragment.app.Fragment() {


    private var columnCount = 1
    private lateinit var adapterProfile: FeedAdapter
    private  var staggeredLayoutManager: StaggeredGridLayoutManager? = null
    private var listener: StaggeredFeedFragmentListener? = null
    private var mOrientationEventListener:OrientationEventListener? = null
    private var current_orient = PORTRAIT
    private val LEFT_ANGLE = 90f
    private val RIGHT_ANGLE = 270f
    private val PORTRAIT_ANGLE = 360f
    private lateinit var api: DatabaseReference


    private val onItemClickListenerVertical = object : OnItemClickListener {

        override fun onItemClick(view: View, position: Int, data:CoreData) {

            println("====================== CLICKED")

            var ref1:String? = ""
            var ref2:String? = ""

            when(data.type){

                GlobalValues.CATEGORY -> {
                    ref1 = PHOTOS
                    ref2 = data.categ_name
                    val increment = 2
                    ActivityCompat.startActivity(view.context,
                        Helper.deliverIntent(data, this@StaggeredFeedFragment.requireContext(), ref1, ref2, increment),
                        Helper.deliverOptions(
                            view,
                            this@StaggeredFeedFragment.requireActivity()
                        )?.toBundle()
                    )
                }

                TYPE_PHOTO ->{

                    ActivityCompat.startActivity(view.context, Helper.deliverIntent(data, this@StaggeredFeedFragment.requireContext(), ref1, ref2, Load_NONE),
                        Helper.deliverOptions(
                            view,
                            this@StaggeredFeedFragment.requireActivity()
                        )?.toBundle()
                    )

                }

                "" ->{

                    ActivityCompat.startActivity(view.context, Helper.deliverIntent(data, this@StaggeredFeedFragment.requireContext(), ref1, ref2, Load_NONE),
                        Helper.deliverOptions(
                            view,
                            this@StaggeredFeedFragment.requireActivity()
                        )?.toBundle()
                    )

                }

                COLLECTION ->{

                    ActivityCompat.startActivity(view.context, Helper.deliverIntent(data, this@StaggeredFeedFragment.requireContext(), null, null, Load_NONE),
                        Helper.deliverOptions(
                            view,
                            this@StaggeredFeedFragment.requireActivity()
                        )?.toBundle()
                    )

                }

                TYPE_VIDEO ->{
                    show(this@StaggeredFeedFragment.requireContext(), view.player, data)
                }
            }
        }
    }


    override fun onPause() {

        GlobalValues.recyclerStateNews = staggered_list.layoutManager?.onSaveInstanceState()

        mOrientationEventListener?.disable()


        super.onPause()

    }


    override fun onResume() {


        GlobalValues.whatsNew = true
        GlobalValues.cameFromExa = false
        GlobalValues.cameFromExa = false

        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        if (GlobalValues.recyclerStateNews != null) restoreInstance() else initializeList()
        super.onResume()

    }

    private val feedAdapterListener = FeedAdapterListener

    override fun onStart() {
        super.onStart()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.staggered_feed_fragment, container, false)
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = if (context is StaggeredFeedFragmentListener) context else { throw RuntimeException("$context must implement OnVideoWatchListener") }
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



    private fun initializeList() {

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
        val config = PagedList.Config.Builder().setPageSize(30).setEnablePlaceholders(false).build()


        val liveData = initializedPagedListBuilder(config).build()

        liveData.observe(this, Observer<PagedList<CoreData>> { pagedList -> adapterProfile.submitList(pagedList) })


        ScrollDownListener().show(this@StaggeredFeedFragment.requireContext(), staggered_list, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                listener?.staggeredFeedFragmentInteractor(animate)
            }
        })

    }

    private fun restoreInstance(){

        staggered_list?.apply { layoutManager = staggeredLayoutManager; layoutManager?.onRestoreInstanceState(GlobalValues.recyclerStateNews); adapter = adapterProfile }
        adapterProfile.SetOnLock(feedAdapterListener)
        ScrollDownListener().show(this@StaggeredFeedFragment.requireContext(), staggered_list, object : ScrollDownListener.HideShow{
                override fun onCallback(animate: String) {
                    listener?.staggeredFeedFragmentInteractor(animate)
                }
            })

        val selector = PlayerSelectorOption
        staggered_list?.playerSelector = selector

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

    @SuppressLint("ClickableViewAccessibility", "InflateParams")
    fun show(context: Context, source: PlayerView, core:CoreData?) {
        val background = ImagePreviewerUtils().getBlurredScreenDrawable(context, source.rootView)


        val dialogView = LayoutInflater.from(context).inflate(R.layout.view_player, null)
        val imageView = dialogView.findViewById(R.id.previewer_player) as PlayerView
        val playerLayout = imageView.layoutParams as ViewGroup.LayoutParams


        imageView.player = source.player

        var resize = imageView.layoutParams
        val width = GlobalValues.widthWindow
        resize.width = width
        //add null check
        val ratio = core?.height?.div(core.width!!)
        val finalHeight = width.times(ratio!!)

        resize.height = finalHeight.toInt()

        val dialog = Dialog(context, R.style.ImagePreviewerTheme)
        dialog.window?.setBackgroundDrawable(background)
        dialog.setContentView(dialogView)
        dialog.show()


        dialogView.setOnClickListener {
            dialog.dismiss()
            if (mOrientationEventListener != null) mOrientationEventListener!!.disable()
        }

        mOrientationEventListener = object : OrientationEventListener(this.requireContext(), SensorManager.SENSOR_DELAY_NORMAL) {

            override fun onOrientationChanged(orientation: Int) {

                rotateVideo(isPortrait(orientation), imageView)

            }
        }

        mOrientationEventListener!!.enable()
    }


    private fun isPortrait(orientation: Int): String {
        return when {

            orientation >= 60 && orientation <= 120 ->  RIGHT_TILT

            orientation >= 200 && orientation <= 300 -> LEFT_TILT

            else -> PORTRAIT
        }
    }

    private fun rotateVideo(rotation:String, playerView: PlayerView){

        if (rotation == RIGHT_TILT && current_orient != rotation){
                println("ROTATE: " + RIGHT_TILT)
                playerView.rotation = RIGHT_ANGLE
        }else if (rotation == LEFT_TILT && current_orient != rotation){
                println("ROTATE: " + LEFT_TILT)
                playerView.rotation = LEFT_ANGLE
        }else {
                println("ROTATE: " + PORTRAIT)
                playerView.rotation = PORTRAIT_ANGLE
        }

    }










}
