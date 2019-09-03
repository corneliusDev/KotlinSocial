package com.photoglyde.justincornelius.photoglyde

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.LiveData
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v4.util.Pair
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.photoglyde.justincornelius.photoglyde.Adapters.FeedAdapter
import com.photoglyde.justincornelius.photoglyde.Adapters.ImageAdapter
import com.photoglyde.justincornelius.photoglyde.Adapters.ImagePreview
import com.photoglyde.justincornelius.photoglyde.Adapters.ScrollDownListener
import com.photoglyde.justincornelius.photoglyde.Data.CoreUnSplash
import com.photoglyde.justincornelius.photoglyde.Data.Data
import com.photoglyde.justincornelius.photoglyde.Data.GlobalVals
import com.photoglyde.justincornelius.photoglyde.Fragments.listenerExplore
import com.photoglyde.justincornelius.photoglyde.Networking.ImageDataSource
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*
import im.ene.toro.PlayerSelector
import im.ene.toro.ToroPlayer
import im.ene.toro.widget.Container
import kotlinx.android.synthetic.main.activity_test2.*
import kotlinx.android.synthetic.main.adapter_row_similar.view.*
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.full_view.*
import kotlinx.android.synthetic.main.full_view.view.*
import kotlinx.android.synthetic.main.test_include.*
import kotlinx.android.synthetic.main.toolbar.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

import java.lang.Exception
import kotlin.math.roundToInt



class ExapandDetailActivity : AppCompatActivity(), View.OnClickListener  {


  private val adapter1 = ImageAdapter()
  private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    private lateinit var inputManager: InputMethodManager
    var test:Int = 0
     var type:String? = ""

    private lateinit var todoList: ArrayList<String>
    private lateinit var toDoAdapter: ArrayAdapter<*>
    private lateinit var adapterProfile: FeedAdapter

    private var isEditTextVisible: Boolean = false
    private var defaultColor: Int = 0


  companion object {
    val EXTRA_PARAM_ID = "place_id"

    fun newIntent(context: Context, position: Int): Intent {
      println("=======thid id my position $position")

   //   val new = GlobalVals.imageClassUser[position]
      val intent = Intent(context, ExapandDetailActivity::class.java)
      intent.putExtra(EXTRA_PARAM_ID, position)
      return intent
    }
  }

    override fun onResume() {
        super.onResume()
        GlobalVals.whatsNew = true
    }

    private val listener3 = object : FeedAdapter.OnItemLockClickListener {
        // , View.OnTouchListener {
        override fun onItemLongClick(view: View, position: Int, core:CoreUnSplash?) {

            println("Info on Image: " + core)

            GlobalVals.currentCore = core

            Data.sendIndexToDetail = position
            ImagePreview().show(this@ExapandDetailActivity, view.placeImage, object : ImagePreview.ExpandActivity{


                override fun onCallback(action:String) {




                    val v = this@ExapandDetailActivity.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
// Vibrate for 500 milliseconds
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        //    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        //deprecated in API 26
                        v.vibrate(500)
                    }

                    when(action){

//                        "map_open" ->{
//                            listenerExplore?.onFragmentInteractionExplore(this@ExapandDetailActivity, "Map Open")
//                        }

                        "expand_image" ->{
                            println("Expand Image")
                            val transitionIntent = ExapandDetailActivity.newIntent(this@ExapandDetailActivity, position)

                            val imagePair = Pair.create(view.placeImageHere as View, "x")
                            val textPair = Pair.create(view.textFullView as View, "categ_text")
                            transitionIntent.putExtra("type", core?.type)
                            transitionIntent.putExtra("image_uri", core?.urls?.regular)
                            transitionIntent.putExtra("height", core?.height)
                            transitionIntent.putExtra("width", core?.width)
                            transitionIntent.putExtra("cameFrom", "HOME")
                            transitionIntent.putExtra("image_tag", core?.user?.location)

                            val pairs = mutableListOf(imagePair, textPair)

                            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ExapandDetailActivity, *pairs.toTypedArray())

                            ActivityCompat.startActivity(this@ExapandDetailActivity, transitionIntent, options.toBundle())
                        }

                    }





                }
            })
            expanded_image_holder_sec.bringToFront()
            expanded_linear_sec.bringToFront()
            expanded_image_sec.bringToFront()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){

            android.R.id.home ->{

                println("=======home button selecteed")
                onBackPressed()
            }

        }


        return super.onOptionsItemSelected(item)

    }



    private val onItemClickListenerVertical = object : FeedAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int, data:CoreUnSplash) {
            placeImage2.transitionName = "newTrans"
            GlobalVals.isExpanded = true
            GlobalVals.awayFromFrag = true
            // 1

            Data.sendIndexToDetail = position

            println("======this is the position $position")


            listVertical2.smoothScrollToPosition(position)

            val transitionIntent = ExapandDetailActivity.newIntent(this@ExapandDetailActivity, position)
            transitionIntent.putExtra("type", data.type)
            transitionIntent.putExtra("image_uri", data.urls?.regular)
            transitionIntent.putExtra("height", data.height)
            transitionIntent.putExtra("width", data.width)
            transitionIntent.putExtra("cameFrom", "HOME")
            transitionIntent.putExtra("image_tag", data.user?.location)
            val placeImage = view.findViewById<ImageView>(R.id.placeImage)
            val placeNameHolder = view.findViewById<LinearLayout>(R.id.placeNameHolder)

            val navigationBar = view.findViewById<View>(android.R.id.navigationBarBackground)
            val statusBar = view.findViewById<View>(R.id.navigation)

            val imagePair = Pair.create(placeImage2 as View, "x")
            //    val holderPair = Pair.create(placeNameHolder as View, "tNameHolder")

            val navPair = Pair.create(navigationBar, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME)
            val statusPair = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
            //  val toolbarPair = Pair.create(toolbar as View, "tActionBar")

            val pairs = mutableListOf(imagePair)
            if (navigationBar != null && navPair != null) {
                pairs += navPair
            }
            // 5

            println("===========scroll up")
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this@ExapandDetailActivity,
                *pairs.toTypedArray())

            ActivityCompat.startActivity(view.context, transitionIntent, options.toBundle())
            finishAfterTransition()


        }
    }

    var gDetector: GestureDetectorCompat? = null
  override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_test2)
    text_categ.bringToFront()
    //  scrollview.setBackgroundResource(R.drawable.round_expanded)
      val toolbar = findViewById<Toolbar>(R.id.toolbar)


     // scrollview.visibility = View.GONE
      setSupportActionBar(toolbar)
      supportActionBar?.setDisplayShowTitleEnabled(false)
      supportActionBar?.setDisplayHomeAsUpEnabled(true)
      supportActionBar?.setDisplayShowHomeEnabled(true)
      supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24px)

      window.sharedElementEnterTransition.duration = 100L

      val cameFrom:String? = intent.getStringExtra("cameFrom")


      if (cameFrom == "HOME"){
          val imageTag:String? = intent.getStringExtra("image_tag")
          collapsingToolbarLayout.setBackgroundResource(R.color.dark_gray)
          toolbar.searchBar.visibility = View.GONE
          toolbar.setBackgroundResource(R.color.dark_gray)
          text_categ.text = imageTag
          println("*******EXPAND: " + imageTag)
      }else{
         // toolbar.setBackgroundColor(Color.WHITE)
      }



      type = intent.getStringExtra("type").toString()

      println("======== this is the expanded type $type")

      var height = placeImage2.layoutParams
      var width = placeImage2.layoutParams

      var imageHeight = intent.getDoubleExtra("height", 0.0)
      var imageWidth = intent.getDoubleExtra("width", 0.0)

      println("Image Height second: " + imageHeight + " and " + imageWidth)

      height.height = GlobalVals.widthWindow
      width.width = GlobalVals.widthWindow


     if (imageHeight != 0.0) {
         val ratio = imageHeight.div(imageWidth)
         println("Image ratio Double: " + ratio)
         println("Image ratio Int: " + ratio.toInt())


         val ratioHeight = GlobalVals.widthWindow.times(ratio).roundToInt()

         println("Image ratio: " + ratioHeight)

         height.height = ratioHeight
         width.width = GlobalVals.widthWindow
     }









      val image_categ = intent.getStringExtra("image_uri").toString()

      runBlocking {


         val job1 = GlobalScope.launch {

              loadPlace(image_categ)

          }

          job1.join()

      }

//      if (type == GlobalVals.CATEGORY) {
//          val ref1 = intent.getStringExtra("ref1").toString()
//          val ref2 = intent.getStringExtra("ref2").toString()
//          val count = intent.getIntExtra("count", 0)
//          initializeList(ref1, ref2, count)
//      }
  }

    fun showToast(message : String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()




  private fun loadPlace(uri:String?) {
   // placeTitle.text = place.name
    //placeImage.setImageResource(Data.foodPictures[Data.sendIndexToDetail])

   // placeImage2.layoutParams.height = GlobalVals.heightWindow-100
    //Picasso.get().load(GlobalVals.test2[Data.sendIndexToDetail]).fit().into(placeImage2)

//      val resizeImage = placeImage2
//      resizeImage.layoutParams.height = GlobalVals.currentTransitionHeight
//
//      GlobalVals.currentCreator?.into(placeImage2)

    //  placeNameH.text = intent.getStringExtra("ref2")


      supportPostponeEnterTransition()

      runOnUiThread {
          Picasso.get()
              .load(Uri.parse(uri))
              .fit()
              .noFade()
              .centerCrop()
              .networkPolicy(NetworkPolicy.OFFLINE)
              .into(placeImage2, object : Callback {
                  override fun onSuccess() {
                      supportStartPostponedEnterTransition()
                      if (type == GlobalVals.CATEGORY) {
                          val ref1 = intent.getStringExtra("ref1").toString()
                          val ref2 = intent.getStringExtra("ref2").toString()
                          val count = intent.getIntExtra("count", 0)
                          initializeList(ref1, ref2, count)
                      }



                      print("=======block3")

                  }

                  override fun onError(e: Exception?) {
                      supportStartPostponedEnterTransition()
                      // initializeList(ref1, ref2, count)
                  }


              })
      }

          // GlobalVals.picassoUnit[GlobalVals.currentCreator.into(placeImage2))

  }



  private fun addToDo(todo: String) {
    todoList.add(todo)
  }



  override fun onClick(v: View) {
    when (v.id) {
      R.id.addButton -> if (!isEditTextVisible) {
        revealEditText(revealView)
        todoText.requestFocus()
        inputManager.showSoftInput(todoText, InputMethodManager.SHOW_IMPLICIT)
        addButton2.setImageResource(R.drawable.icn_morph)
        val animatable = addButton2.drawable as Animatable
       animatable.start()
      } else {
        addToDo(todoText.text.toString())
        toDoAdapter.notifyDataSetChanged()
        inputManager.hideSoftInputFromWindow(todoText.windowToken, 0)
        hideEditText(revealView)
        addButton2.setImageResource(R.drawable.icn_morph_reverse)
        val animatable = addButton2.drawable as Animatable
        animatable.start()
      }
    }
  }

  private fun revealEditText(view: LinearLayout) {
    val cx = view.right - 30
    val cy = view.bottom - 60
    val finalRadius = Math.max(view.width, view.height)
    val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius.toFloat())
    view.visibility = View.VISIBLE
    isEditTextVisible = true
    anim.start()
  }

  private fun hideEditText(view: LinearLayout) {
    val cx = view.right - 30
    val cy = view.bottom - 60
    val initialRadius = view.width
    val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius.toFloat(), 0f)
    anim.addListener(object : AnimatorListenerAdapter() {
      override fun onAnimationEnd(animation: Animator) {
        super.onAnimationEnd(animation)
        view.visibility = View.INVISIBLE
      }
    })
    isEditTextVisible = false
    anim.start()
  }

  override fun onBackPressed() {
      GlobalVals.isExpanded = false

      println("=========check trans ${placeImage2.transitionName}")

      if (GlobalVals.awayFromFrag) {
          println("=======force finish")
          GlobalVals.awayFromFrag = false
          finish()
      }else{
          finish()


      }

  }

    override fun onPause() {
        super.onPause()
        GlobalVals.whatsNew = false
    }

    private fun initializeList(ref1:String?, ref2:String?, count:Int) {


        // if (GlobalVals.recyclerState2 == null) {
//        println("======this is null")
        staggeredLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)




        // val layout = view?.findViewById<RecyclerView>(R.id.list)
        listVertical2?.layoutManager = staggeredLayoutManager


        adapterProfile = FeedAdapter()
        listVertical2.adapter = adapterProfile
        adapterProfile.SetOnLock(listener3)


        //  adapter.SetOnLock(listener3)






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

        liveData.observe(this, android.arch.lifecycle.Observer<PagedList<CoreUnSplash>> { pagedList ->


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
                    toSelect = Collections.emptyList()
                }else{

                    val firstOrder = items.get(0).playerOrder

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

        //listVertical2?.playerSelector = selector

        ScrollDownListener().show(this@ExapandDetailActivity, listVertical2, object : ScrollDownListener.HideShow{
            override fun onCallback(animate: String) {
                println("=======we have item call back $animate and $listenerExplore")
               // listener?.onListFragmentInteraction(animate)
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

private fun <T> LiveData<T>.observe(exapandDetailActivity: ExapandDetailActivity, observer: Observer) {

}

