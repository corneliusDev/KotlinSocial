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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.mancj.materialsearchbar.MaterialSearchBar
import com.photoglyde.justincornelius.photoglyde.*
import com.photoglyde.justincornelius.photoglyde.Adapters.*
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Networking.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.Networking.NodeExist
import com.photoglyde.justincornelius.photoglyde.Networking.UnSplashService
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.Utilities.PlayerSelectorOption


import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.fragment_item_list.*
import kotlinx.android.synthetic.main.horizontal_rows.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.android.synthetic.main.view_holder_exoplayer_basic.view.*


class WhatsNew : Fragment(), MaterialSearchBar.OnSearchActionListener {

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


            val transitionIntent = ExapandDetailActivity.newIntent(this@WhatsNew.requireContext(), position)

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
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@WhatsNew.requireActivity(), *pairs.toTypedArray())

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
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@WhatsNew.requireActivity(), *pairs.toTypedArray())

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
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@WhatsNew.requireActivity(), *pairs.toTypedArray())

                    //  val new = ViewCompat.getTransitionName(view.placeImageH)


                    //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ItemFragment.requireActivity(), view, ViewCompat.getTransitionName(view.placeImageH)!!)

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

                ScrollDownListener().show(this@WhatsNew.requireContext(), profile_list2, object : ScrollDownListener.HideShow{
                    override fun onCallback(animate: String) {
                        println("=======we have item call back $animate and $listenerExplore")
                        listener?.onListFragmentInteraction(animate)
                    }
                })

                val selector = PlayerSelectorOption

                //val container = view?.findViewById<Container>(R.id.profile_list2)
                profile_list2?.playerSelector = selector

            }else{

                initializeList("Feed","test",2)


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
                ImagePreview().show(this@WhatsNew.requireContext(), view.placeImage, object : ImagePreview.ExpandActivity{


                    override fun onCallback(source: View) {
                        println("=======we have swiped up $position")

                    }
                })
            }else{

                Helper.show(this@WhatsNew.requireContext(), view.player, core)
            }

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


    fun setHorizontal(){
        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        profile_list.layoutManager = staggeredLayoutManager
        adapterHorizotal = BindingHorizontal(this@WhatsNew.requireContext(), 6)
        profile_list.isNestedScrollingEnabled = true
        profile_list.adapter = adapterHorizotal
    }


    fun adapterSetUpVertical(Data:List<CoreUnSplash>) {







        ScrollDownListener().show(this@WhatsNew.requireContext(), profile_list2, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                println("=======we have item call back $animate and $listenerExplore")
                listener?.onListFragmentInteraction(animate)
            }
        })



        val selector = PlayerSelectorOption
        profile_list2?.playerSelector = selector

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

        ScrollDownListener().show(this@WhatsNew.requireContext(), profile_list2, object : ScrollDownListener.HideShow{
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
