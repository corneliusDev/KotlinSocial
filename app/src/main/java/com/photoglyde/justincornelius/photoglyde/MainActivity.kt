

package com.photoglyde.justincornelius.photoglyde

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.StaggeredGridLayoutManager
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mancj.materialsearchbar.MaterialSearchBar
import com.photoglyde.justincornelius.photoglyde.Adapters.ImageAdapter
import com.photoglyde.justincornelius.photoglyde.Camera.CamerOpenActivity
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Fragments.*
import com.photoglyde.justincornelius.photoglyde.Networking.PostUN
import com.photoglyde.justincornelius.photoglyde.ProfileFragments.ProfileLanding
import com.photoglyde.justincornelius.photoglyde.Utilities.AnimateWindow
import com.photoglyde.justincornelius.photoglyde.Utilities.FileHandler
import com.photoglyde.justincornelius.photoglyde.Utilities.FindUserImage
import com.photoglyde.justincornelius.photoglyde.Utilities.PREFIX_FILE
import com.photoglyde.justincornelius.photoglyde.Web.MapFragment
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    PostingOptions.OnFragmentInteractionListener,
    ExploreActivity.OnFragmentInteractionListenerExplore,
    ProfileLanding.OnFragmentInteractionListenerNews, WhatsNew.OnListFragmentInteractionListener, MaterialSearchBar.OnSearchActionListener, MapFragment.OnFragmentInteractionListener
     {

  private val adapter = ImageAdapter()
    private val request_code = 101
    private lateinit var staggeredLayoutManager: StaggeredGridLayoutManager
    private lateinit var toolbar: Toolbar
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.HOME
    lateinit var fragment:Fragment
    private val client = OkHttpClient()
    val MOVEVALUE = 240f
    private lateinit var mMap: GoogleMap
         var mapFragment = MapFragment.newInstance()
         private lateinit var bottomNavigation: BottomNavigationView


    override fun onButtonClicked(buttonCode: Int) {

    }

     override fun onPause() {
             super.onPause()
             GlobalVals.cameFromMain = false
         }

     override fun onResume() {
             super.onResume()
             GlobalVals.cameFromMain = true
         }

     override fun onFragmentInteraction(ani: String, fragment: Fragment) {

             when(ani) {

                 "Map Close" -> {

                     supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_down, R.anim.slide_down)
                         .remove(fragment).commit()

                     AnimateWindow().animateDown(this, containerOptionsDim)

                 }
             }

         }

     override fun onSearchStateChanged(enabled: Boolean) {

    }

    override fun onSearchConfirmed(text: CharSequence?) {

    }

    override fun onListFragmentInteraction(animate: String) {
        when(animate){


            DOWN -> {
                ObjectAnimator.ofFloat(navigation, "translationY", MOVEVALUE).apply {
                    duration = 500
                    start()
                }
            }

            UP -> {

                ObjectAnimator.ofFloat(navigation, "translationY", 0f).apply {
                    duration = 500
                    start()
                }
            }



        }
    }

    override fun onFragmentInteractionNews(fragment: Fragment, animateDirection: String) {
      //  println("====we are on main $animateDirection")
        when(animateDirection){


            DOWN -> {
                ObjectAnimator.ofFloat(navigation, "translationY", MOVEVALUE).apply {
                    duration = 500
                    start()
                }
            }

            UP -> {

                ObjectAnimator.ofFloat(navigation, "translationY", 0f).apply {
                    duration = 500
                    start()
                }
            }



        }
    }

    override fun onFragmentInteractionExplore(fragment: Fragment, animateDirection: String) {

        when(animateDirection){

            DOWN -> {
                ObjectAnimator.ofFloat(navigation, "translationY", MOVEVALUE).apply {
                    duration = 500
                    start()
                }
            }

            UP -> {

                ObjectAnimator.ofFloat(navigation, "translationY", 0f).apply {
                    duration = 500
                    start()
                }
            }

            "Map Open" ->{

             //   println("Map Main")


                containerOptionsDim.visibility = View.VISIBLE
                containerOptionsDim.bringToFront()
                containerMap.visibility = View.VISIBLE
                containerMap.bringToFront()

                containerOptionsDim.isEnabled  = false



                AnimateWindow().animateUp(this, containerOptionsDim, object : AnimateWindow.AnimationEnd {

                    override fun animationOver() {

                    }

                })

                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.slide_up, R.anim.slide_up)
                    .replace(R.id.containerMap, mapFragment)
                    .commit()
            }



        }


    }

    override fun onFragmentInteraction(fragment: Fragment, action:String) {

        when(action){

            "close" ->{
                val anim = AlphaAnimation(1.0f, 0.0f)
                anim.duration = 500
                containerOptionsDim.startAnimation(anim)
               supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_down,R.anim.slide_down).remove(fragment).commit()
                anim.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        containerOptionsDim.visibility = View.GONE
                    }

                    override fun onAnimationStart(p0: Animation?) {

                    }
                })
            }

            "camera" ->{

                val i = Intent(this, CamerOpenActivity::class.java)
                startActivityForResult(i, request_code)
            }




        }





    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {



        when(item?.itemId){

            R.id.miCompose ->{
                containerOptionsDim.visibility = View.VISIBLE
                containerOptionsDim.bringToFront()
                containerOptions.bringToFront()

//
                val anim = AlphaAnimation(0.0f, 1.0f)
                anim.duration = 500
                containerOptionsDim.startAnimation(anim)
                fragment = PostingOptions.newInstance()
//
//
                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_up,R.anim.slide_down).replace(R.id.containerOptions, fragment).commit()


                val fragment = PostingOptions.newInstance()
                val bundle = Bundle()
                bundle.putString("VIDEO", "https://firebasestorage.googleapis.com/v0/b/spotfile-ae64e.appspot.com/o/socialspots%2FGeorgia%2FAtlanta%2F-LWcxeKzPlv7Vlur8Kwk?alt=media&token=1e3f7c3a-ba6f-4314-8e9d-16f164a4d9b0")
                fragment.arguments = bundle




            }


            16908332 ->{

                val anim = AlphaAnimation(1.0f, 0.0f)
                anim.duration = 500
                containerOptionsDim.startAnimation(anim)
                anim.setAnimationListener(object : Animation.AnimationListener{
                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        containerOptionsDim.visibility = View.GONE
                    }

                    override fun onAnimationStart(p0: Animation?) {

                    }
                })



              //  window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_down,R.anim.slide_down).remove(fragment).commit()
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
                supportActionBar?.setDisplayShowHomeEnabled(false)

            }

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navPosition = findNavigationPositionById(item.itemId)
        return switchFragment(navPosition)
    }





  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_main)

   //   GlobalVals.onBoardingComplete = false

      if(!GlobalVals.onBoardingComplete){
          GlobalVals.onBoardingComplete = true
      val intent = Intent(this, Register::class.java)
      startActivity(intent)}

      if (GlobalVals.listCateg.size == 0) {
          PostUN().downloadCategs(object : PostUN.DownloadCategories {
              override fun onCallBack(bool: Boolean) {

              }
          })
      }

      containerMap.visibility = View.INVISIBLE
      val transaction = supportFragmentManager
          .beginTransaction()
          .setCustomAnimations(R.anim.slide_up, R.anim.slide_up)
          .replace(R.id.containerMap, mapFragment)

      transaction.commit()
      transaction.remove(mapFragment)


      setUpDummyUserStart()
      FileHandler(this, "TESTING.jpg",  "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Google_Chrome_icon_%28September_2014%29.svg/1200px-Google_Chrome_icon_%28September_2014%29.svg.png").start()





      //FindUserImage(this).run()
//      this.fileList().forEach {
//          println("*******" + it)
//
//      }

      SetUpToolBar()
      setUpDummyUserStart()

      bottomNavigation = findViewById(R.id.navigation)
      initBottomNavigation()


      initFragment(savedInstanceState)


      println("CURRENT PATH: " + applicationContext.filesDir)


      val displayMetrics = DisplayMetrics()
      windowManager.defaultDisplay.getMetrics(displayMetrics)
      GlobalVals.heightWindow = displayMetrics.heightPixels
      GlobalVals.widthWindow = displayMetrics.widthPixels



  }


    fun FragmentManager.detach() {
        findFragmentById(R.id.container)?.also {
            beginTransaction().detach(it).commit()
        }
    }

    private fun switchFragment(navPosition: BottomNavigationPosition): Boolean {
        return supportFragmentManager.findFragment(navPosition).let {
            if (it.isAdded) return false
            supportFragmentManager.detach() // Extension function
            supportFragmentManager.attach(it, navPosition.getTag()) // Extension function
            supportFragmentManager.executePendingTransactions()
        }
    }

    private fun FragmentManager.findFragment(position: BottomNavigationPosition): Fragment {
        return findFragmentByTag(position.getTag()) ?: position.createFragment()
    }

    fun FragmentManager.attach(fragment: Fragment, tag: String) {
        if (fragment.isDetached) {
            beginTransaction().attach(fragment).commit()
        } else {
            beginTransaction().add(R.id.container, fragment, tag).commit()
        }
        // Set a transition animation for this transaction.
        beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        savedInstanceState ?: switchFragment(BottomNavigationPosition.HOME)
    }

    private fun initBottomNavigation() {
        // This is required in Support Library 27 or lower.
        // bottomNavigation.disableShiftMode()
        bottomNavigation.active(navPosition.position)   // Extension function
        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }


    fun SetUpToolBar(){
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_black_18dp)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }


    fun setUpDummyUserStart(){

        val description = "Aspiring journalist with a dream to sing and change lives"
        val URI = "https://thedailyhustleonline.com/wp-content/uploads/2017/10/circular-profile-image.png"

//        val myImages = ImageClass(
//            BitmapFactory.decodeResource(resources, R.drawable.usej), Uri.parse(URI), 100101, LatLng((84).toDouble(),(84).toDouble()), ArrayList(), "", "", 100, 200
//        )
//
//        val imageArray = ArrayList<ImageClass>()
//        imageArray.add(myImages)
//        imageArray.add(myImages)
//        imageArray.add(myImages)
//        imageArray.add(myImages)

        var dummyUser = UserInfo(
            "Judy Lien", description,"","", Uri.parse(URI), null, ArrayList<ImageClass>(), ArrayList(), ArrayList()
        )

        GlobalVals.currentUser = dummyUser

        this.fileList().forEach {
            println("=========Looking File: " + it.toString())
            if (it.toString().contains(PREFIX_FILE)){
                var image = ImageClass()
                println("=========Found File: " + it.toString())
                image.file = this.filesDir.path + "/" + it.toString()

                GlobalVals.currentUser?.userImages?.add(image)

            }

        }



    }






}
