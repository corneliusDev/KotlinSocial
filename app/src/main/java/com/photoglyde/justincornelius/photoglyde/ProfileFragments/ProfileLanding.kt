package com.photoglyde.justincornelius.photoglyde.ProfileFragments

import android.arch.lifecycle.Observer
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.TabLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_test.*
import android.widget.*
import com.photoglyde.justincornelius.photoglyde.Adapters.*
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Networking.NYCTimesDataResponse
import com.photoglyde.justincornelius.photoglyde.Networking.NewsDataSource
import com.photoglyde.justincornelius.photoglyde.R


import com.photoglyde.justincornelius.photoglyde.Web.NewsWebView


import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.button_profile.*
import kotlinx.android.synthetic.main.news_lists.*
import kotlinx.android.synthetic.main.toolbar.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileLanding : Fragment() {
    private lateinit var adapterVertical: BindingVertical
    private lateinit var adapterHorizontal: BindingHorizontal
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    lateinit var mListener: OnFragmentInteractionListenerNews
    private  var adapterM = NewsAdapter()
    private lateinit var adapterProfile: BindingVertical
    private var scrollDown = true
    private val request_code = 101
    var restoreContent: Parcelable? = null
    var restoreStateSaved: Parcelable? = null
    var restoreStateCollection: Parcelable? = null



    private val onItemClickListenerVertical = object : NewsAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {




//            val i = Intent(this@BlankFragment.requireContext(), NewsWebView::class.java)
//            i.putExtra("URL", GlobalVals.savedNews[position].url)
//            startActivityForResult(i, request_code)
//            // 1
            val transitionIntent = NewsWebView.newIntent(
                this@ProfileLanding.requireContext(),
                position
            )
            transitionIntent.putExtra("URL", GlobalVals.savedNews[position].url)
            transitionIntent.putExtra("URL_IMAGE", GlobalVals.savedNews[position].urlToImage)
            transitionIntent.putExtra("TITLE", GlobalVals.savedNews[position].title)

            GlobalVals.currentCreator = Picasso.get().load(GlobalVals.savedNews[position].urlToImage)
            val placeImage = view.findViewById<ImageView>(R.id.imageHere)


            // 2
            val text = view.findViewById<TextView>(R.id.title)
            val statusBar = view.findViewById<View>(android.R.id.statusBarBackground)

            val imagePair = Pair.create(placeImage as View, "toWeb")
          //  val holderPair = Pair.create(placeNameHolder as View, "tNameHolder")

            // 3
            val navPair = Pair.create(text as View, "textWeb")

            // 4
            val pairs = mutableListOf(imagePair, navPair)
//            if (navigationBar != null && navPair != null) {
//                pairs += navPair
//            }
            Data.sendIndexToDetail = position
             5
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ProfileLanding.requireActivity(),imagePair,navPair)
            //val new = ActivityOptionsCompat.makeSceneTransitionAnimation(this@BlankFragment.requireActivity(), pairs.toTypedArray())



            ActivityCompat.startActivity(this@ProfileLanding.requireContext(), transitionIntent, options.toBundle())

        }
    }

    private val onItemClickListenerHorizontal = object : BindingHorizontal.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {

          //  mListener.onFragmentInteraction()

        }
    }
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var recyclerTabLayout: TabLayout
    private var listener: OnFragmentInteractionListenerNews? = null
    var adapterPOS = findNavigationPositionById(R.id.button_profile_1)

    override fun onResume() {
        super.onResume()

        if (returnSavedInstance(adapterPOS.id) != null) {
            staggeredLayoutManager = StaggeredGridLayoutManager(getSpan(adapterPOS.id), StaggeredGridLayoutManager.VERTICAL)
            home_page.layoutManager = staggeredLayoutManager
            home_page.layoutManager?.onRestoreInstanceState(returnSavedInstance(adapterPOS.id))
            home_page.adapter = returnAdapter(adapterPOS.id)
        }

    }

    override fun onPause() {
        super.onPause()
        println("===========on pause saving ${adapterPOS} amd ${adapterPOS.id}")
        savedInstance(adapterPOS.id)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListenerNews) {
            listener = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnRageComicSelected.")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)?.searchBar?.visibility = View.INVISIBLE

        moveBorder(adapterPOS.id)

        println("USER PHOTO: " + GlobalVals.currentUser?.userImages)


//
        button_profile_1?.setOnClickListener {

            adapterPOS = findNavigationPositionById(R.id.button_profile_1)
            setUpAdapter(adapterPOS.id, 3)
            moveBorder(adapterPOS.id)

        }

        button_profile_2?.setOnClickListener {
            adapterPOS = findNavigationPositionById(R.id.button_profile_2)
            println("========= button profile 2 $adapterPOS")
            setUpAdapter(adapterPOS.id,1)
            moveBorder(adapterPOS.id)
        }

        button_profile_3?.setOnClickListener {
            adapterPOS = findNavigationPositionById(R.id.button_profile_3)
            setUpAdapter(adapterPOS.id,1)
            moveBorder(adapterPOS.id)
        }






    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_test, container, false)
    }



    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    private fun initializeList() {




        // if (GlobalVals.recyclerState2 == null) {
//        println("======this is null")
        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        staggeredLayoutManager.spanCount =1
        listVertical3?.layoutManager = staggeredLayoutManager
        listVertical3.isNestedScrollingEnabled = true
        listVertical3?.adapter = adapterM

        adapterM.setOnItemClickListener(onItemClickListenerVertical)

        val config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(true)
            .build()
        GlobalVals.test = GlobalVals.test + 1



        val liveData = initializedPagedListBuilder(config).build()

        3
        liveData.observe(this, Observer<PagedList<NYCTimesDataResponse>> { pagedList ->
            adapterM.submitList(pagedList)
            println("========we have live data")
            if (GlobalVals.recyclerStateNews != null) {
                listVertical3.layoutManager?.onRestoreInstanceState(GlobalVals.recyclerStateNews)
            }


        })

    }

    interface OnFragmentInteractionListenerNews {

        fun onFragmentInteractionNews(fragment:Fragment, animateDirection:String)
    }


    companion object {

        val TAG: String = ProfileLanding::class.java.simpleName

        fun newInstance() : ProfileLanding {
            val fragment = ProfileLanding()
            val args = Bundle()

            return fragment


        }
    }
    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<String, NYCTimesDataResponse> {

        val dataSourceFactory = object : DataSource.Factory<String, NYCTimesDataResponse>() {
            override fun create(): DataSource<String, NYCTimesDataResponse> {
                return NewsDataSource()
            }
        }
        return LivePagedListBuilder<String, NYCTimesDataResponse>(dataSourceFactory, config)
    }



    fun setUpAdapter(id: Int, span:Int){
        println("======applied from setup ${returnAdapter(id)} and ${findNavigationPositionById(id)}")
        staggeredLayoutManager = StaggeredGridLayoutManager(span, StaggeredGridLayoutManager.VERTICAL)
        home_page.layoutManager = staggeredLayoutManager
        adapterProfile = returnAdapter(id)
        home_page.isNestedScrollingEnabled = true
        home_page.adapter = adapterProfile
    }



    enum class AdapterSwitch(val position: Int, val id: Int) {
        MYCONTENT(0, R.id.button_profile_1),
        COLLECTIONS(1, R.id.button_profile_2),
        SAVEDCONTENT(2, R.id.button_profile_3),

    }

    fun getSpan(id: Int): Int = when (id) {
        AdapterSwitch.MYCONTENT.id -> 3
        AdapterSwitch.COLLECTIONS.id -> 1
        AdapterSwitch.SAVEDCONTENT.id -> 1
        else -> 1
    }


    fun findNavigationPositionById(id: Int): AdapterSwitch = when (id) {
        AdapterSwitch.MYCONTENT.id -> AdapterSwitch.MYCONTENT
        AdapterSwitch.COLLECTIONS.id -> AdapterSwitch.COLLECTIONS
        AdapterSwitch.SAVEDCONTENT.id -> AdapterSwitch.SAVEDCONTENT
        else -> AdapterSwitch.MYCONTENT
    }


    fun returnAdapter(id: Int): BindingVertical = when (id) {
        AdapterSwitch.MYCONTENT.id -> BindingVertical(this@ProfileLanding.requireContext(), GlobalVals.currentUser!!.userImages!!, 0)
        AdapterSwitch.COLLECTIONS.id -> BindingVertical(this@ProfileLanding.requireContext(), GlobalVals.currentUser!!.userImages!!, 1)
        AdapterSwitch.SAVEDCONTENT.id -> BindingVertical(this@ProfileLanding.requireContext(), GlobalVals.currentUser!!.userImages!!, 2)
        else -> BindingVertical(this@ProfileLanding.requireContext(), GlobalVals.currentUser!!.userImages!!, 1)
    }




    fun returnSavedInstance(id: Int): Parcelable? = when (id) {
        AdapterSwitch.MYCONTENT.id -> restoreContent
        AdapterSwitch.COLLECTIONS.id -> restoreStateSaved
        AdapterSwitch.SAVEDCONTENT.id -> restoreStateCollection
        else -> restoreContent
    }

    fun savedInstance(id: Int) {
        when (id) {
            AdapterSwitch.MYCONTENT.id -> restoreContent = home_page.layoutManager?.onSaveInstanceState()
            AdapterSwitch.COLLECTIONS.id -> restoreStateSaved = home_page.layoutManager?.onSaveInstanceState()
            AdapterSwitch.SAVEDCONTENT.id -> restoreStateCollection = home_page.layoutManager?.onSaveInstanceState()
        }
    }


    fun moveBorder(id:Int){
        when(id){

            AdapterSwitch.MYCONTENT.id -> {
                view?.findViewById<Button>(AdapterSwitch.MYCONTENT.id)?.setBackgroundResource(R.drawable.rounded_button_post_list_option)
                view?.findViewById<Button>(AdapterSwitch.COLLECTIONS.id)?.setBackgroundResource(R.color.basically_black)
                view?.findViewById<Button>(AdapterSwitch.SAVEDCONTENT.id)?.setBackgroundResource(R.color.basically_black)

                view?.findViewById<Button>(AdapterSwitch.MYCONTENT.id)?.setTextColor(Color.LTGRAY)
                view?.findViewById<Button>(AdapterSwitch.COLLECTIONS.id)?.setTextColor(Color.GRAY)
                view?.findViewById<Button>(AdapterSwitch.SAVEDCONTENT.id)?.setTextColor(Color.GRAY)



            }

            AdapterSwitch.SAVEDCONTENT.id -> {
                view?.findViewById<Button>(AdapterSwitch.SAVEDCONTENT.id)?.setBackgroundResource(R.drawable.rounded_button_post_list_option)
                view?.findViewById<Button>(AdapterSwitch.MYCONTENT.id)?.setBackgroundResource(R.color.basically_black)
                view?.findViewById<Button>(AdapterSwitch.COLLECTIONS.id)?.setBackgroundResource(R.color.basically_black)


                view?.findViewById<Button>(AdapterSwitch.SAVEDCONTENT.id)?.setTextColor(Color.LTGRAY)
                view?.findViewById<Button>(AdapterSwitch.COLLECTIONS.id)?.setTextColor(Color.GRAY)
                view?.findViewById<Button>(AdapterSwitch.MYCONTENT.id)?.setTextColor(Color.GRAY)
            }

            AdapterSwitch.COLLECTIONS.id -> {
                view?.findViewById<Button>(AdapterSwitch.COLLECTIONS.id)?.setBackgroundResource(R.drawable.rounded_button_post_list_option)
                view?.findViewById<Button>(AdapterSwitch.MYCONTENT.id)?.setBackgroundResource(R.color.basically_black)
                view?.findViewById<Button>(AdapterSwitch.SAVEDCONTENT.id)?.setBackgroundResource(R.color.basically_black)

                view?.findViewById<Button>(AdapterSwitch.COLLECTIONS.id)?.setTextColor(Color.LTGRAY)
                view?.findViewById<Button>(AdapterSwitch.SAVEDCONTENT.id)?.setTextColor(Color.GRAY)
                view?.findViewById<Button>(AdapterSwitch.MYCONTENT.id)?.setTextColor(Color.GRAY)
            }


        }
    }









}




