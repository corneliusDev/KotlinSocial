package com.photoglyde.justincornelius.photoglyde.Fragments

import android.arch.lifecycle.Observer
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.util.Pair
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import android.widget.ImageView
import com.photoglyde.justincornelius.photoglyde.Adapters.*
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.ExapandDetailActivity
import com.photoglyde.justincornelius.photoglyde.HoldImageViewer.ImagePreview
import com.photoglyde.justincornelius.photoglyde.Networking.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.Utilities.ScrollDownListener
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.full_view.view.*
import kotlinx.android.synthetic.main.toolbar.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
//private val adapter = ImageAdapter()
private val request_code = 101
private lateinit var adapter : FeedAdapter
private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
private var mShortAnimationDuration: Int = 0
 var gDetector: GestureDetectorCompat? = null
  var listenerExplore: ExploreActivity.OnFragmentInteractionListenerExplore? = null
private var scrolDown = true
interface HideShow {
    fun onCallback(animate:String)
}
class ExploreActivity : Fragment(){


    private val listener3 = object : FeedAdapter.OnItemLockClickListener {
       // , View.OnTouchListener {
        override fun onItemLongClick(view: View, position: Int, core:CoreUnSplash?) {

           println("Info on Image: " + core)

           GlobalVals.currentCore = core

           GlobalVals.sendIndexToDetail = position
             ImagePreview()
                 .show(this@ExploreActivity.requireContext(), view.placeImageHere, object : ImagePreview.ExpandActivity{


                override fun onCallback(action:String) {




                val v = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
// Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    //deprecated in API 26
                    v.vibrate(500)
                }

                when(action){

                    "map_open" ->{
                        listenerExplore?.onFragmentInteractionExplore(this@ExploreActivity, "Map Open")
                    }

                    "expand_image" ->{
                        println("Expand Image")
                        val transitionIntent = ExapandDetailActivity.newIntent(this@ExploreActivity.requireContext(), position)

                        val imagePair = Pair.create(view.placeImageHere as View, "x")
                        val textPair = Pair.create(view.textFullView as View, "categ_text")
                        transitionIntent.putExtra("type", core?.type)
                        transitionIntent.putExtra("image_uri", core?.urls?.regular)
                        transitionIntent.putExtra("height", core?.height)
                        transitionIntent.putExtra("width", core?.width)
                        transitionIntent.putExtra("cameFrom", "HOME")
                        transitionIntent.putExtra("image_tag", core?.user?.location)

                        val pairs = mutableListOf(imagePair, textPair)

                        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ExploreActivity.requireActivity(), *pairs.toTypedArray())

                        ActivityCompat.startActivity(this@ExploreActivity.requireActivity(), transitionIntent, options.toBundle())
                    }


                }





            }
        })
            expanded_image_holder.bringToFront()
            expanded_linear.bringToFront()
            expanded_image.bringToFront()
        }

    }







    private val onItemClickListenerVertical = object : FeedAdapter.OnItemClickListener {


        override fun onItemClick(view: View, position: Int, data:CoreUnSplash) {
            println("Single Click")

            val transitionIntent = ExapandDetailActivity.newIntent(this@ExploreActivity.requireContext(), position)
            transitionIntent.putExtra("image_uri", data.urls?.regular)
            transitionIntent.putExtra("type", data.type)
            transitionIntent.putExtra("cameFrom", "HOME")
            transitionIntent.putExtra("image_tag", data.user?.location)

            println("Image Height first: " + data.height + " and " + data.width)

            transitionIntent.putExtra("height", data.height)
            transitionIntent.putExtra("width", data.width)

            val placeImage = view.findViewById<ImageView>(R.id.placeImageH)


            val statusBar = view.findViewById<View>(R.id.navigation)

            val imagePair = Pair.create(view.placeImageHere as View, "x")
            val textPair = Pair.create(view.textFullView as View, "categ_text")

            //  val navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME)
            val statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
            //  val toolbarPair = Pair.create(toolbar as View, "tActionBar")

            val pairs = mutableListOf(imagePair, textPair)

            println("===========check values $view and ${ViewCompat.getTransitionName(view)}")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ExploreActivity.requireActivity(), *pairs.toTypedArray())

            //  val new = ViewCompat.getTransitionName(view.placeImageH)


            //val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ItemFragment.requireActivity(), view, ViewCompat.getTransitionName(view.placeImageH)!!)

            ActivityCompat.startActivity(view.context, transitionIntent, options.toBundle())
        }
    }
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null




    override fun onPause() {
        super.onPause()

        GlobalVals.cameFromMain = true

        if (profile_list_explore != null) GlobalVals.recyclerState2 = profile_list_explore.layoutManager?.onSaveInstanceState()

    }

    override fun onResume() {
        super.onResume()


            staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            profile_list_explore?.layoutManager =
                staggeredLayoutManager

            if (GlobalVals.recyclerState2 != null) {

                println("======applied${GlobalVals.recyclerState2}")
                profile_list_explore.layoutManager?.onRestoreInstanceState(GlobalVals.recyclerState2)
                profile_list_explore?.adapter = adapter
                adapter.setOnItemClickListener(onItemClickListenerVertical)
                adapter.SetOnLock(listener3)
//
//
//
                ScrollDownListener()
                    .show(this@ExploreActivity.requireContext(), profile_list_explore, object : ScrollDownListener.HideShow{
                    override fun onCallback(animate: String) {
                        listenerExplore?.onFragmentInteractionExplore(this@ExploreActivity, animate)
                    }
                })
//
            } else {

                initializeList()

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        println("========onsaved")

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)


        println("============outstatre ${savedInstanceState?.get("layoutrestore")}")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        //listener?.onFragmentInteraction(uri)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }





    override fun onDetach() {
        super.onDetach()
        println("are we detaching")
        listenerExplore = null
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListenerExplore) {
            listenerExplore = context
        } else {
            throw ClassCastException(context.toString() + " must implement OnRageComicSelected.")
        }
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
    interface OnFragmentInteractionListenerExplore {
        fun onFragmentInteractionExplore(fragment:Fragment, animateDirection:String)
    }

    companion object {

        val TAG: String = ExploreActivity::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExploreActivity.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() : ExploreActivity {
            val fragment = ExploreActivity()
            val args = Bundle()

            return fragment


        }
    }

    private fun initializeList() {

        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        profile_list_explore?.layoutManager = staggeredLayoutManager
        adapter = FeedAdapter()
        profile_list_explore.adapter = adapter
        println("======this is null ${GlobalVals.recyclerState2}")
        if (GlobalVals.recyclerState2 != null) {
            profile_list_explore.layoutManager?.onRestoreInstanceState(GlobalVals.recyclerState2)
        }

        adapter.setOnItemClickListener(onItemClickListenerVertical)
        adapter.SetOnLock(listener3)

        val layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


//blanked out for no wifi work session
            val config = PagedList.Config.Builder()
                .setPageSize(30)
                .setEnablePlaceholders(false)
                .build()
            GlobalVals.test = GlobalVals.test + 1



            val liveData = initializedPagedListBuilder(config, FEED).build()

            3
            liveData.observe(this, Observer<PagedList<CoreUnSplash>> { pagedList ->


                adapter.submitList(pagedList)


            })

        ScrollDownListener()
            .show(this@ExploreActivity.requireContext(), profile_list_explore, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                println("=======we have call back $animate and $listenerExplore")
                listenerExplore?.onFragmentInteractionExplore(this@ExploreActivity, animate)
            }
        })

    }




    private fun initializedPagedListBuilder(config: PagedList.Config, ref:String):
            LivePagedListBuilder<String, CoreUnSplash> {

        val dataSourceFactory = object : DataSource.Factory<String, CoreUnSplash>() {
            override fun create(): DataSource<String, CoreUnSplash> {

                return ImageDataSource(ref, "Explore", 2)
            }
        }
        return LivePagedListBuilder<String, CoreUnSplash>(dataSourceFactory, config)
    }






    }




