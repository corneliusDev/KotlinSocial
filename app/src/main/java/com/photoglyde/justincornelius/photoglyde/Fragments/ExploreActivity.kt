package com.photoglyde.justincornelius.photoglyde.Fragments

import android.animation.Animator
import android.arch.lifecycle.Observer
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.Fragment
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.*
import com.photoglyde.justincornelius.photoglyde.Adapters.*
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Networking.ImageDataSource
import com.photoglyde.justincornelius.photoglyde.R
import im.ene.toro.PlayerSelector
import im.ene.toro.ToroPlayer
import im.ene.toro.widget.Container
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.full_view.view.*
import kotlinx.android.synthetic.main.toolbar.view.*
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
//private val adapter = ImageAdapter()
private val request_code = 101
lateinit private var adapter : FeedAdapter
lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager
lateinit private var GridLay: GridLayoutManager

private var mCurrentAnimator: Animator? = null

// The system "short" animation time duration, in milliseconds. This
// duration is ideal for subtle animations or animations that occur
// very frequently.
private var mShortAnimationDuration: Int = 0
 var gDetector: GestureDetectorCompat? = null
  var listenerExplore: ExploreActivity.OnFragmentInteractionListenerExplore? = null
private var scrolDown = true



/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ExploreActivity.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ExploreActivity.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

interface HideShow {
    fun onCallback(animate:String)
}
class ExploreActivity : Fragment(){











    private val listener3 = object : FeedAdapter.OnItemLockClickListener {
       // , View.OnTouchListener {

        override fun onItemLongClick(view: View, position: Int, core:CoreUnSplash?) {
            println("========== long click $position")
           // view.setOnTouchListener(new)
            Data.sendIndexToDetail = position
        ImagePreview().show(this@ExploreActivity.requireContext(), view.placeImageHere, object : ImagePreview.ExpandActivity{


            override fun onCallback(source: View) {
                println("=======we have swiped up $position")



                val v = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
// Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    //deprecated in API 26
                    v.vibrate(500)
                }



            }
        })
            out.bringToFront()


           // zoomImageFromThumb(view, GlobalVals.picassoUnit[position])

            expanded_image_holder.bringToFront()
            expanded_linear.bringToFront()
            expanded_image.bringToFront()
        }

//        override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
//            println("========inside on touch")
//            if (p1?.getAction() == android.view.MotionEvent.ACTION_UP) {
//                Log.d("TouchTest", "Touch down");
//            }
//
//            return true
//        }
    }







    private val onItemClickListenerVertical = object : FeedAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, data:CoreUnSplash) {


            println("===========scroll down")


        }
    }
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null




    override fun onPause() {
        super.onPause()

        GlobalVals.cameFromMain = true


        println("=======we are on pause")

        if (profile_list_explore != null) GlobalVals.recyclerState2 = profile_list_explore.layoutManager?.onSaveInstanceState()




    }

    override fun onResume() {
        super.onResume()



//        val api = UnSplashService.createService()
//
//
//                api.getPosts("photos", "5").enqueue(object : retrofit2.Callback<ArrayList<CoreUnSplash>> {
//                    override fun onFailure(call: retrofit2.Call<ArrayList<CoreUnSplash>>, t: Throwable) {
//                        println("=======this is unsplash error ${t.message} and ${t.localizedMessage} and ${t.localizedMessage} and ${t}")
//                    }
//
//                    override fun onResponse(
//                        call: retrofit2.Call<ArrayList<CoreUnSplash>>,
//                        response: retrofit2.Response<ArrayList<CoreUnSplash>>
//                    ) {
//
//                        println("=======this is unsplash${response.body()?.size}  ${response.code()} and ${response.raw()}\" ${response.message()}\"")
//
//                        val splashData = response.body()
//                        GlobalVals.upSplash = splashData!!
//                        GlobalVals.apiCount++
//
//                        //  PostUN().postData(splashData)
//
//                      //  FirebaseDatabase.getInstance().getReference("Feed").push().setValue(splashData)
//                    }
//                })


//        GlobalVals.cameFromMain = false
//
//        if (GlobalVals.loadPicasso) {
//            GlobalVals.loadPicasso = true
            staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            // val layout = view?.findViewById<RecyclerView>(R.id.list)
            profile_list_explore?.layoutManager =
                staggeredLayoutManager
////
////
////
////
////
////
////
////
////            println("======this is null o nresume ${GlobalVals.recyclerState2}")
            if (GlobalVals.recyclerState2 != null) {

                println("======applied${GlobalVals.recyclerState2}")
                profile_list_explore.layoutManager?.onRestoreInstanceState(GlobalVals.recyclerState2)
                profile_list_explore?.adapter = adapter
                adapter.setOnItemClickListener(onItemClickListenerVertical)
                adapter.SetOnLock(listener3)
//
//
//
                ScrollDownListener().show(this@ExploreActivity.requireContext(), profile_list_explore, object : ScrollDownListener.HideShow{
                    override fun onCallback(animate: String) {
                        println("=======we have call back $animate and $listenerExplore")
                        listenerExplore?.onFragmentInteractionExplore(this@ExploreActivity, animate)
                    }
                })
//
            } else {


                initializeList()



                //val api = UnSplashService.createService()
//
//
//                api.getPosts("photos", "30").enqueue(object : retrofit2.Callback<ArrayList<CoreUnSplash>> {
//                    override fun onFailure(call: retrofit2.Call<ArrayList<CoreUnSplash>>, t: Throwable) {
//                        println("=======this is unsplash error ${t.message} and ${t.localizedMessage} and ${t.localizedMessage} and ${t}")
//                    }
//
//                    override fun onResponse(
//                        call: retrofit2.Call<ArrayList<CoreUnSplash>>,
//                        response: retrofit2.Response<ArrayList<CoreUnSplash>>
//                    ) {
//
//                        println("=======this is unsplash${response.body()?.size}  ${response.code()} and ${response.raw()}\" ${response.message()}\"")
//
//                        val splashData = response.body()
//                        GlobalVals.upSplash = splashData!!
//                        GlobalVals.apiCount++
//
//                      //  PostUN().postData(splashData)
//
//                        FirebaseDatabase.getInstance().getReference("Feed").push().setValue(splashData[0])
//                        FirebaseDatabase.getInstance().getReference("Feed").push().setValue(splashData[1])
//
//                        adapterSetUpVertical(splashData)
//
//
//                    }
//                })
//                //  initializeList()
//                GlobalVals.test++
//            }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)







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
        //list.visibility = View.VISIBLE
        println("========activity created")


   // initializeList()




        activity?.findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar)?.searchBar?.visibility = View.INVISIBLE




            println("========is init ${GlobalVals.test}")






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
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) as StaggeredGridLayoutManager







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

        ScrollDownListener().show(this@ExploreActivity.requireContext(), profile_list_explore, object : ScrollDownListener.HideShow{
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

                return ImageDataSource(ref, "", 1)
            }
        }
        return LivePagedListBuilder<String, CoreUnSplash>(dataSourceFactory, config)
    }




    inner class WrapContentLinearLayoutManager : LinearLayoutManager(this.context) {
        //... constructor
        override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (e: IndexOutOfBoundsException) {
                Log.e("TAG", "meet a IOOBE in RecyclerView")
            }

        }
    }





    fun adapterSetUpVertical(Data:List<CoreUnSplash>) {

//        staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
//        profile_list.layoutManager = staggeredLayoutManager
//        adapterProfile = ProfileAdapter(this@ItemFragment.requireContext(), Data, arrayListOf(0,1,4))
//        profile_list.isNestedScrollingEnabled = true
//        profile_list.adapter = adapterProfile

       // GlobalVals.loadPicasso = true








//        runBlocking {
//
//            val adapterJob = launch {
//                println("=======job1")
//
//                staggeredLayoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
//                profile_list_explore.layoutManager = staggeredLayoutManager
//                adapterProfile = ProfileAdapter(
//                    this@ExploreActivity.requireContext(),
//                    Data,
//                    arrayListOf(7, 7, 8, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7)
//                )
//                adapterProfile.setOnItemClickListener(onItemClickListenerVertical)
//                adapterProfile.SetOnLock(listener3)
//                profile_list_explore.isNestedScrollingEnabled = true
//                profile_list_explore.adapter = adapterProfile
//
//
//
//            }
//
//            adapterJob.join()
//            delay(1000)
//            println("=======job2")
//
//
//
//        }






        if (GlobalVals.recyclerState2 != null) {
            profile_list_explore.layoutManager?.onRestoreInstanceState(GlobalVals.recyclerState2)
        }

        ScrollDownListener().show(this@ExploreActivity.requireContext(), profile_list_explore, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                println("=======we have call back $animate and $listenerExplore")
                listenerExplore?.onFragmentInteractionExplore(this@ExploreActivity, animate)
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

        val container = view?.findViewById<Container>(R.id.profile_list_explore)
        container?.playerSelector = selector

       // adapterProfile.setOnItemClickListener(onItemClickListenerVertical)

    }


    }




