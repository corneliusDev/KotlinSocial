package com.photoglyde.justincornelius.photoglyde.Fragments
import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.graphics.BitmapFactory
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.service.autofill.UserData
import android.support.v4.app.*
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.obf.it
import com.mancj.materialsearchbar.MaterialSearchBar
import com.photoglyde.justincornelius.photoglyde.*
import com.photoglyde.justincornelius.photoglyde.Adapters.*
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Networking.DataDump
import com.photoglyde.justincornelius.photoglyde.Networking.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.Networking.NodeExist
import com.photoglyde.justincornelius.photoglyde.Networking.UnSplashService
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.VideoPlayback.VideoActivity
import com.photoglyde.justincornelius.photoglyde.dummy.DummyContent


import im.ene.toro.PlayerSelector
import im.ene.toro.ToroPlayer
import im.ene.toro.widget.Container
import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.fragment_video_record.*
import kotlinx.android.synthetic.main.full_view.view.*
import kotlinx.android.synthetic.main.horizontal_rows.view.*
import kotlinx.android.synthetic.main.test_include.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*
import kotlinx.android.synthetic.main.view_player.view.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.w3c.dom.Node
import java.util.*
import kotlin.collections.ArrayList


class ItemFragment : Fragment(), OnMapReadyCallback, MaterialSearchBar.OnSearchActionListener {

    // TODO: Customize parameters
    private var columnCount = 1
    lateinit private var adapterProfile: FeedAdapter
    lateinit private var adapterHorizotal: BindingHorizontal
    lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager
    private var listener: OnListFragmentInteractionListener? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    private var count = 1



    override fun onButtonClicked(buttonCode: Int) {
        println("==============text clicked")
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        Log.d("LOG_TAG",   " text changed ")
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        println("==============text confirmed $text")
      //  sendSearch(text.toString().trim())

        apiCall(text.toString())

    }

    private val onItemClickListenerVertical = object : FeedAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, data:CoreUnSplash) {

            var ref1:String? = ""
            var ref2:String? = ""
            var count:Int? = 0


            val transitionIntent = ExapandDetailActivity.newIntent(this@ItemFragment.requireContext(), position)

            println("======= shared type ${data.type}")

            when(data.type){

                GlobalVals.CATEGORY -> {
                    ref1 = PHOTOS
                    ref2 = data.categ_name
                    count = 2

                    transitionIntent.putExtra("image_uri", data.categ_image_uri)
                    transitionIntent.putExtra("type", data.type)
                    transitionIntent.putExtra("ref1", ref1)
                    transitionIntent.putExtra("ref2", ref2)
                    transitionIntent.putExtra("count", count)


                    // profile_list_explore.smoothScrollToPosition(position)



                    val placeImage = view.findViewById<ImageView>(R.id.placeImageH)


                    val statusBar = view.findViewById<View>(R.id.navigation)

                    val imagePair = Pair.create(view.placeImageH as View, "x")
                    val textPair = Pair.create(view.placeNameH as View, "categ_text")

                    //  val navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME)
                    val statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
                    //  val toolbarPair = Pair.create(toolbar as View, "tActionBar")

                    val pairs = mutableListOf(imagePair, textPair)

                    println("===========check values $view and ${ViewCompat.getTransitionName(view)}")
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ItemFragment.requireActivity(), *pairs.toTypedArray())

                    //  val new = ViewCompat.getTransitionName(view.placeImageH)


                    //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ItemFragment.requireActivity(), view, ViewCompat.getTransitionName(view.placeImageH)!!)

                    ActivityCompat.startActivity(view.context, transitionIntent, options.toBundle())

                }


                TYPE_PHOTO ->{

                    transitionIntent.putExtra("image_uri", data.urls?.regular)
                    transitionIntent.putExtra("type", data.type)

                    val placeImage = view.findViewById<ImageView>(R.id.placeImageH)


                    val statusBar = view.findViewById<View>(R.id.navigation)

                    val imagePair = Pair.create(view.placeImageH as View, "x")
                    val textPair = Pair.create(view.placeNameH as View, "categ_text")

                    //  val navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME)
                    val statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
                    //  val toolbarPair = Pair.create(toolbar as View, "tActionBar")

                    val pairs = mutableListOf(imagePair, textPair)

                    println("===========check values $view and ${ViewCompat.getTransitionName(view)}")
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ItemFragment.requireActivity(), *pairs.toTypedArray())

                    //  val new = ViewCompat.getTransitionName(view.placeImageH)


                    //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ItemFragment.requireActivity(), view, ViewCompat.getTransitionName(view.placeImageH)!!)

                    ActivityCompat.startActivity(view.context, transitionIntent, options.toBundle())

                }

                "" ->{

                    transitionIntent.putExtra("image_uri", data.urls?.regular)
                    transitionIntent.putExtra("type", data.type)

                    val placeImage = view.findViewById<ImageView>(R.id.placeImageH)


                    val statusBar = view.findViewById<View>(R.id.navigation)

                    val imagePair = Pair.create(view.placeImage as View, "x")
                   // val textPair = Pair.create(view.placeNameH as View, "categ_text")

                    //  val navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME)
                    val statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
                    //  val toolbarPair = Pair.create(toolbar as View, "tActionBar")

                    val pairs = mutableListOf(imagePair)

                   // println("===========check values $view and ${ViewCompat.getTransitionName(view)}")
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ItemFragment.requireActivity(), *pairs.toTypedArray())

                    //  val new = ViewCompat.getTransitionName(view.placeImageH)


                    //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ItemFragment.requireActivity(), view, ViewCompat.getTransitionName(view.placeImageH)!!)

                    ActivityCompat.startActivity(view.context, transitionIntent, options.toBundle())

                }


                TYPE_VIDEO ->{
                    Helper.show(this@ItemFragment.requireContext(), view.player, data)
                }



            }

//
//            staggeredLayoutManager.spanCount = 1
//
//
//            var height = data.height
//            val width = data.width
//            val ratio = height?.div(width!!)
//            val finalHeight = GlobalVals.widthWindow?.times(ratio!!)?.toInt()
//
//            val resize1 = view.layoutParams as ViewGroup.LayoutParams
//            val resize2 = view.player.layoutParams
//           // resize.width = GlobalVals.widthWindow
//
//
//            resize1.width = GlobalVals.widthWindow
//            resize1.height = finalHeight!!
//
//
//            resize2.width = GlobalVals.widthWindow
//            resize2.height = finalHeight!!
//
//
//
//
//
//            staggeredLayoutManager.scrollToPositionWithOffset(position, 0)

            val new = profile_list2.layoutParams
          //  new.height = finalHeight




           // staggeredLayoutManager.removeViewAt(position + 1)




        }
    }


    override fun onPause() {

        GlobalVals.fromExplore = false

        GlobalVals.recyclerStateNews = profile_list2.layoutManager?.onSaveInstanceState()

        GlobalVals.recyclerStateCategExplore = profile_list.layoutManager?.onSaveInstanceState()

        super.onPause()

    }


    override fun onResume() {

        GlobalVals.fromExplore = true

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







            println("======this is null o nresume ${GlobalVals.recyclerStateNews}")
            if (GlobalVals.recyclerStateNews != null) {
                println("=======we are being restored item frag")
                profile_list2.layoutManager?.onRestoreInstanceState(GlobalVals.recyclerStateNews)
                profile_list2?.adapter = adapterProfile
                adapterProfile.SetOnLock(listener3)

                ScrollDownListener().show(this@ItemFragment.requireContext(), profile_list2, object : ScrollDownListener.HideShow{
                    override fun onCallback(animate: String) {
                        println("=======we have item call back $animate and $listenerExplore")
                        listener?.onListFragmentInteraction(animate)
                    }
                })

                val selector = object : PlayerSelector {

                    override fun reverse(): PlayerSelector {

                        return this.reverse()
                    }

                    override fun select(
                        container: Container,
                        items: MutableList<ToroPlayer>
                    ): List<ToroPlayer> {
                        var count = items.size
                        //    println("=====player count $count and ${items[0]}")
                        var toSelect:List<ToroPlayer>
                        if (count < 1) {
                            println("==========player select1")
                            toSelect = Collections.emptyList();
                        }else{

                            val firstOrder = items.get(0).getPlayerOrder()

                            val span = staggeredLayoutManager.spanCount
//                    count = Math.min(count, span / span)
                            count = 2
                            toSelect =  ArrayList<ToroPlayer>()


                            println("==========player select2 ${staggeredLayoutManager.spanCount} and $count and $firstOrder")
                            for (i in 0 until count) {
                                if (i < items.size) toSelect.add(items[i])
                            }



                        }

                        return toSelect
                    }
                }

                //val container = view?.findViewById<Container>(R.id.profile_list2)
                profile_list2?.playerSelector = selector

            }else{

                initializeList(FEED,"",1)


//                val api = UnSplashService.createService()
//
//
//                api.getPosts("photos", "9").enqueue(object : retrofit2.Callback<ArrayList<CoreUnSplash>> {
//                    override fun onFailure(call: retrofit2.Call<ArrayList<CoreUnSplash>>, t: Throwable) {
//                        println("=======this is unsplash error ${t.message} and ${t.localizedMessage} and ${t.localizedMessage} and ${t}")
//                    }
//
//                    override fun onResponse(
//                        call: retrofit2.Call<ArrayList<CoreUnSplash>>,
//                        response: retrofit2.Response<ArrayList<CoreUnSplash>>
//                    ) {
//
//                        println("=======this is unsplash${response.body()?.size}  ${response.code()} and ${response.raw()}\" ${response.headers()}\"")
//
//                        val splashData = response.body()
//                       // GlobalVals.upSplash = splashData!!
//
//                        GlobalVals.apiCount++
//
//                        GlobalVals.upSplash = splashData!!
//
//                       // FirebaseDatabase.getInstance().getReference("Photos").child("random").child("page${GlobalVals.apiCount}").setValue(splashData)
//
//                        for (i in splashData) {
//                            FirebaseDatabase.getInstance().getReference("Feed").push().setValue(i)
//                        }
//
//                        adapterSetUpVertical(splashData)
//
//
//                    }
//                })
            }
        }



        super.onResume()
    }

    private val listener3 = object : FeedAdapter.OnItemLockClickListener {
        // , View.OnTouchListener {

        override fun onItemLongClick(view: View, position: Int, core:CoreUnSplash?) {
            println("========== long click $position")
            // view.setOnTouchListener(new)
            Data.sendIndexToDetail = position

            ViewPropertyObjectAnimator.animate(view.placeImage).scaleY(1.0f).scaleX(1.0f)
                .setDuration(200).start()
            if (view.placeImage != null) {
                ImagePreview().show(this@ItemFragment.requireContext(), view.placeImage, object : ImagePreview.ExpandActivity{


                    override fun onCallback(source: View) {
                        println("=======we have swiped up $position")

                    }
                })
            }else{

                Helper.show(this@ItemFragment.requireContext(), view.player, core)
            }

         //   out.bringToFront()


            // zoomImageFromThumb(view, GlobalVals.picassoUnit[position])

//            expanded_image_holder_explore.bringToFront()
//            expanded_linear_explore.bringToFront()
//            expanded_image_explore.bringToFront()
        }


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

        val toolbarItemFrag = activity?.findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)?.searchBar

        toolbarItemFrag?.visibility = View.VISIBLE

       // setHorizontal()

        toolbarItemFrag?.setOnSearchActionListener(this)

        toolbarItemFrag?.addTextChangeListener(object : TextWatcher{

            override fun afterTextChanged(p0: Editable?) {

                println("==========after text changed $p0")
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println("=========== before text changed $p0")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                println("============on text changed $p0")
            }
        })





        GlobalVals.fromExplore = true
       // initializeList()

       // GlobalVals.loadPicasso = true

       // setUpDummyUser()


//        if (GlobalVals.exploreLoad == 0) {
//            GlobalVals.exploreLoad++
//            val api = UnSplashService.createService()
//
//
//            api.getPosts("photos", "30").enqueue(object : retrofit2.Callback<List<CoreUnSplash>> {
//                override fun onFailure(call: retrofit2.Call<List<CoreUnSplash>>, t: Throwable) {
//                    println("=======this is unsplash error ${t.message} and ${t.localizedMessage} and ${t.localizedMessage} and ${t}")
//                }
//
//                override fun onResponse(
//                    call: retrofit2.Call<List<CoreUnSplash>>,
//                    response: retrofit2.Response<List<CoreUnSplash>>
//                ) {
//
//                    println("=======this is unsplash${response.body()?.size}  ${response.code()} and ${response.raw()}\" ${response.message()}\"")
//
//                    val splashData = response.body()
//
//                    adapterSetUpVertical(splashData!!)
//
//
//                }
//            })
//        }





    //    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@ItemFragment.requireActivity())


    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        println("============maps are hit")
        mMap = googleMap


        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
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

        val TAG: String = ItemFragment::class.java.simpleName

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance() : ItemFragment {
            val fragment = ItemFragment()
            val args = Bundle()

            return fragment


        }
    }


    fun setHorizontal(){
        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        profile_list.layoutManager = staggeredLayoutManager
        adapterHorizotal = BindingHorizontal(this@ItemFragment.requireContext(), 6)
        profile_list.isNestedScrollingEnabled = true
        profile_list.adapter = adapterHorizotal
    }


    fun adapterSetUpVertical(Data:List<CoreUnSplash>) {







        ScrollDownListener().show(this@ItemFragment.requireContext(), profile_list2, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                println("=======we have item call back $animate and $listenerExplore")
                listener?.onListFragmentInteraction(animate)
            }
        })



        val selector = object : PlayerSelector {

            override fun reverse(): PlayerSelector {

                return this.reverse()
            }

            override fun select(
                container: Container,
                items: MutableList<ToroPlayer>
            ): List<ToroPlayer> {
                var count = items.size
            //    println("=====player count $count and ${items[0]}")
                var toSelect:List<ToroPlayer>
                if (count < 1) {
                    println("==========player select1")
                    toSelect = Collections.emptyList();
                }else{

                    val firstOrder = items.get(0).getPlayerOrder()

                    val span = staggeredLayoutManager.spanCount
//                    count = Math.min(count, span / span)
                    count = 2
                    toSelect =  ArrayList<ToroPlayer>()


                    println("==========player select2 ${staggeredLayoutManager.spanCount} and $count and $firstOrder")
                    for (i in 0 until count) {
                        if (i < items.size) toSelect.add(items[i])
                    }



                }

                return toSelect
            }
        }

        //val container = view?.findViewById<Container>(R.id.profile_list2)
        profile_list2?.playerSelector = selector




        //adapterProfile.setOnItemClickListener(onItemClickListenerVertical)

    }

//    fun setUpDummyUser(){
//
//        val description = "Aspiring journalist with a dream to sing and change lives"
//        val URI = "https://thedailyhustleonline.com/wp-content/uploads/2017/10/circular-profile-image.png"
//
//        val myImages = ImageClass(
//            Uri.parse(URI), 100101, LatLng((84).toDouble(),(84).toDouble()), ArrayList(),""
//        )
//
//        val imageArray = ArrayList<ImageClass>()
//        imageArray.add(myImages)
//        imageArray.add(myImages)
//        imageArray.add(myImages)
//        imageArray.add(myImages)
//
//        var dummyUser = UserInfo(
//            "Judy Lien", description, Uri.parse(URI), null, imageArray, ArrayList(), ArrayList()
//        )
//
//
//
//    }

    private fun initializeList(ref1:String?, ref2:String?, count:Int) {


        // if (GlobalVals.recyclerState2 == null) {
//        println("======this is null")
        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)




        // val layout = view?.findViewById<RecyclerView>(R.id.list)
        profile_list2?.layoutManager = staggeredLayoutManager


        adapterProfile = FeedAdapter()
        profile_list2.adapter = adapterProfile


        adapterProfile.SetOnLock(listener3)

        profile_list2.isNestedScrollingEnabled = true





        println("======this is null ${GlobalVals.recyclerState2}")
//        if (GlobalVals.recyclerState2 != null) {
//            profile_list2.layoutManager?.onRestoreInstanceState(GlobalVals.recyclerState2)
//        }




        adapterProfile.setOnItemClickListener(onItemClickListenerVertical)




        //  adapter.setOnItemItemPositionGetter(listener2)











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

        val selector = object : PlayerSelector {

            override fun reverse(): PlayerSelector {

                return this.reverse()
            }

            override fun select(
                container: Container,
                items: MutableList<ToroPlayer>
            ): List<ToroPlayer> {
                var count = items.size
                //    println("=====player count $count and ${items[0]}")
                var toSelect:List<ToroPlayer>
                if (count < 1) {
                    println("==========player select1")
                    toSelect = Collections.emptyList();
                }else{

                    val firstOrder = items.get(0).getPlayerOrder()

                    val span = staggeredLayoutManager.spanCount
//                    count = Math.min(count, span / span)
                    count = 2
                    toSelect =  ArrayList<ToroPlayer>()


                    println("==========player select2 ${staggeredLayoutManager.spanCount} and $count and $firstOrder")
                    for (i in 0 until count) {
                        if (i < items.size) toSelect.add(items[i])
                    }



                }

                return toSelect
            }
        }

        profile_list2?.playerSelector = selector

        ScrollDownListener().show(this@ItemFragment.requireContext(), profile_list2, object : ScrollDownListener.HideShow{
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


    fun sendSearch(node:String?){
        NodeExist().ifNodeExist(node, object : NodeExist.SendResult{
            override fun onCallback(bool: Boolean) {
                println("======= result from search $bool")


            }
        })




    }

    fun apiCall(search:String?){

                val api = UnSplashService.createService()

                    api.getPosts("photos",search, count.toString(), "29").enqueue(object : retrofit2.Callback<UnSplashBegin> {
                override fun onFailure(call: retrofit2.Call<UnSplashBegin>, t: Throwable) {
                    println("=======this is unsplash error ${t.message} and ${t.localizedMessage} and ${t.localizedMessage} and ${t}")
                }

                override fun onResponse(
                    call: retrofit2.Call<UnSplashBegin>,
                    response: retrofit2.Response<UnSplashBegin>
                ) {

                    count++
                    println("=======this is unsplash${response.body()} ${response.body()?.results}  ${response.code()} and ${response.raw()}\" ${response.message()}\"")

                    val splashData = response.body()

                  // DataDump().dumpNow(search, splashData!!)




                }
            })
    }




}
