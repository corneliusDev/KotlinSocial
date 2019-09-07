package com.photoglyde.justincornelius.photoglyde.Fragments
import android.arch.lifecycle.Observer
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.support.v4.app.*
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.photoglyde.justincornelius.photoglyde.*
import com.photoglyde.justincornelius.photoglyde.Adapters.*
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.HoldImageViewer.ImagePreview
import com.photoglyde.justincornelius.photoglyde.Networking.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.Utilities.PlayerSelectorOption
import com.photoglyde.justincornelius.photoglyde.Utilities.ScrollDownListener


import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.horizontal_rows.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*


class WhatsNew : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 1
    private lateinit var adapterProfile: FeedAdapter
    private lateinit var adapterHorizotal: BindingHorizontal
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    private var count = 1

    private val onItemClickListenerVertical = object : FeedAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, data:CoreUnSplash) {

            var ref1:String? = ""
            var ref2:String? = ""
            var count:Int? = 0


            val transitionIntent = ExapandDetailActivity.newIntent(this@WhatsNew.requireContext(), position)



            when(data.type){

                GlobalVals.CATEGORY -> {
                    ref1 = PHOTOS
                    ref2 = data.categ_name
                    count = 2


                    println("======= shared type ${data.type} and " + ref2)
                    transitionIntent.putExtra("image_uri", data.categ_image_uri)
                    transitionIntent.putExtra("type", data.type)
                    transitionIntent.putExtra("ref1", ref1)
                    transitionIntent.putExtra("ref2", ref2)
                    transitionIntent.putExtra("count", count)
                    transitionIntent.putExtra("cameFrom", "HOME")
                    transitionIntent.putExtra("height", data.height)
                    transitionIntent.putExtra("width", data.width)
                    transitionIntent.putExtra("image_tag", data.categ_name)

                    val imagePair = Pair.create(view.placeImageH as View, "x")
                    val textPair = Pair.create(view.placeNameH as View, "categ_text")

                    val pairs = mutableListOf(imagePair, textPair)
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@WhatsNew.requireActivity(), *pairs.toTypedArray())

                    ActivityCompat.startActivity(view.context, transitionIntent, options.toBundle())

                }


                TYPE_PHOTO ->{

                    transitionIntent.putExtra("image_uri", data.urls?.regular)
                    transitionIntent.putExtra("type", data.type)
                    transitionIntent.putExtra("height", data.height)
                    transitionIntent.putExtra("width", data.width)

                    val imagePair = Pair.create(view.placeImageH as View, "x")
                    val textPair = Pair.create(view.placeNameH as View, "categ_text")

                    val pairs = mutableListOf(imagePair, textPair)

                    println("===========check values $view and ${ViewCompat.getTransitionName(view)}")
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@WhatsNew.requireActivity(), *pairs.toTypedArray())
                    ActivityCompat.startActivity(view.context, transitionIntent, options.toBundle())

                }

                "" ->{

                    transitionIntent.putExtra("image_uri", data.urls?.regular)
                    transitionIntent.putExtra("type", data.type)

                    transitionIntent.putExtra("height", data.height)
                    transitionIntent.putExtra("width", data.width)

                    val imagePair = Pair.create(view.placeImage as View, "x")

                    val pairs = mutableListOf(imagePair)

                   // println("===========check values $view and ${ViewCompat.getTransitionName(view)}")
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@WhatsNew.requireActivity(), *pairs.toTypedArray())

                    ActivityCompat.startActivity(view.context, transitionIntent, options.toBundle())

                }


                TYPE_VIDEO ->{
                    Helper.show(this@WhatsNew.requireContext(), view.player, data)
                }



            }


        }
    }


    override fun onPause() {

        GlobalVals.whatsNew = false

        GlobalVals.recyclerStateNews = profile_list2.layoutManager?.onSaveInstanceState()

        GlobalVals.recyclerStateCategExplore = profile_list.layoutManager?.onSaveInstanceState()

        super.onPause()

    }


    override fun onResume() {

        GlobalVals.whatsNew = true

        if (GlobalVals.recyclerStateCategExplore != null){

            staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
            profile_list.layoutManager = staggeredLayoutManager
            profile_list.layoutManager!!.onRestoreInstanceState(GlobalVals.recyclerStateCategExplore)
           // profile_list.isNestedScrollingEnabled = true
            profile_list.adapter = adapterHorizotal

        }else{
          //  setHorizontal()
        }


        if (GlobalVals.loadPicassoFromExplore) {
            GlobalVals.loadPicasso = true
            staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            // val layout = view?.findViewById<RecyclerView>(R.id.list)
            profile_list2?.layoutManager = staggeredLayoutManager

            if (GlobalVals.recyclerStateNews != null) {
                println("=======we are being restored item frag")
                profile_list2.layoutManager?.onRestoreInstanceState(GlobalVals.recyclerStateNews)
                profile_list2?.adapter = adapterProfile
                adapterProfile.SetOnLock(listener3)

                ScrollDownListener()
                    .show(this@WhatsNew.requireContext(), profile_list2, object : ScrollDownListener.HideShow{
                    override fun onCallback(animate: String) {
                        println("=======we have item call back $animate and $listenerExplore")
                        listener?.onListFragmentInteraction(animate)
                    }
                })

                val selector = PlayerSelectorOption

                //val container = view?.findViewById<Container>(R.id.profile_list2)
                profile_list2?.playerSelector = selector

            }else{

                initializeList("Feed","Random",2)

            }
        }



        super.onResume()
    }

    private val listener3 = object : FeedAdapter.OnItemLockClickListener {

        override fun onItemLongClick(view: View, position: Int, core:CoreUnSplash?) {
            println("========== long click $position")
            // view.setOnTouchListener(new)
            GlobalVals.sendIndexToDetail = position

            ViewPropertyObjectAnimator.animate(view.placeImage).scaleY(1.0f).scaleX(1.0f)
                .setDuration(200).start()
            if (view.placeImage != null) {
                ImagePreview()
                    .show(this@WhatsNew.requireContext(), view.placeImage, object : ImagePreview.ExpandActivity{


                    override fun onCallback(action:String) {
                        println("=======we have swiped up $position")

                    }
                })
            }else{

                Helper.show(this@WhatsNew.requireContext(), view.player, core)
            }

        }


    }

    override fun onStart() {
        super.onStart()
        GlobalVals.whatsNew = true
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


        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnRageComicSelected.")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        GlobalVals.whatsNew = true

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnListFragmentInteractionListener {

        fun onListFragmentInteraction(animate:String)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        val TAG: String = WhatsNew::class.java.simpleName

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance() : WhatsNew {
            val fragment = WhatsNew()
            val args = Bundle()

            return fragment


        }
    }



    private fun initializeList(ref1:String?, ref2:String?, count:Int) {

        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        profile_list2?.layoutManager = staggeredLayoutManager

        adapterProfile = FeedAdapter()
        profile_list2.adapter = adapterProfile

        adapterProfile.SetOnLock(listener3)

        profile_list2.isNestedScrollingEnabled = true

        println("======this is null ${GlobalVals.recyclerState2}")

        adapterProfile.setOnItemClickListener(onItemClickListenerVertical)

//blanked out for no wifi work session
        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build()
        GlobalVals.test = GlobalVals.test + 1

        val liveData = initializedPagedListBuilder(config, ref1, ref2, count)
            .build()

        liveData.observe(this, Observer<PagedList<CoreUnSplash>> { pagedList ->

            adapterProfile.submitList(pagedList)


        })

        val selector = PlayerSelectorOption

        profile_list2?.playerSelector = selector

        ScrollDownListener()
            .show(this@WhatsNew.requireContext(), profile_list2, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                println("=======we have item call back $animate and $listenerExplore")
                listener?.onListFragmentInteraction(animate)
            }
        })

    }

    private fun initializedPagedListBuilder(config: PagedList.Config, ref1:String?, ref2:String?, count:Int):
            LivePagedListBuilder<String, CoreUnSplash> {

        val dataSourceFactory = object : DataSource.Factory<String, CoreUnSplash>() {
            override fun create(): DataSource<String, CoreUnSplash> {
                return ImageDataSource(ref1, ref2, count)
            }
        }
        return LivePagedListBuilder<String, CoreUnSplash>(dataSourceFactory, config)
    }








}
