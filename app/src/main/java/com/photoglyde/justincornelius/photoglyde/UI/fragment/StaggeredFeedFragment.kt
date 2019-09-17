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
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.*

import kotlinx.android.synthetic.main.view_player.*


class StaggeredFeedFragment : androidx.fragment.app.Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private lateinit var adapterProfile: FeedAdapter
    private lateinit var adapterHorizotal: BindingHorizontal
    private  var staggeredLayoutManager: StaggeredGridLayoutManager? = null
    private var listener: StaggeredFeedFragmentListener? = null
    private var mOrientationEventListener:OrientationEventListener? = null
    private var current_orient = PORTRAIT
    private val LEFT_ANGLE = 90f
    private val RIGHT_ANGLE = 270f
    private val PORTRAIT_ANGLE = 360f
    private var CURRENT_ANGLE = 0F


    private val onItemClickListenerVertical = object : OnItemClickListener {


        override fun onItemClick(view: View, position: Int, data:CoreData) {

            println("====================== CLICKED")

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

        GlobalValues.whatsNew = false

        GlobalValues.recyclerStateNews = staggered_list.layoutManager?.onSaveInstanceState()

        mOrientationEventListener?.disable()


        super.onPause()

    }


    override fun onResume() {

        staggeredLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )

        if (GlobalValues.recyclerStateNews != null) restoreInstance() else initializeList(FEED, RANDOM,2)
        GlobalValues.whatsNew = true
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

           // source.player.release()

            if (mOrientationEventListener != null) mOrientationEventListener!!.disable()


        }
        var count = 0
        dialog.cancel_video.setOnClickListener {
           // dialog.dismiss()


            if(count == 0){
                imageView.rotation = 90.0f
                count++

            }else if (count == 1){
                imageView.rotation = 270.0f
            }

            println("cancel count: " + count)




         //   dialog.expand_text.rotation = 90.0f
        }

        mOrientationEventListener = object : OrientationEventListener(
            this.requireContext(), SensorManager.SENSOR_DELAY_NORMAL
        ) {

            override fun onOrientationChanged(orientation: Int) {

                println("@@@@@@@@@@@@@@@@@@@@@ CHANGING ORIENTASTION: " + isPortrait(orientation) + " and value: " + orientation)

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
