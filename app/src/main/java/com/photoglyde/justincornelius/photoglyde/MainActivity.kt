

package com.photoglyde.justincornelius.photoglyde

import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.mancj.materialsearchbar.MaterialSearchBar
import com.photoglyde.justincornelius.photoglyde.Adapters.ImageAdapter
import com.photoglyde.justincornelius.photoglyde.Camera.CamerOpenActivity
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Fragments.*
import com.photoglyde.justincornelius.photoglyde.Networking.PostUN
import com.photoglyde.justincornelius.photoglyde.ProfileFragments.ProfileLanding
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    PostingOptions.OnFragmentInteractionListener,
    ExploreActivity.OnFragmentInteractionListenerExplore,
    ProfileLanding.OnFragmentInteractionListenerNews, WhatsNew.OnListFragmentInteractionListener, MaterialSearchBar.OnSearchActionListener{

  private val adapter = ImageAdapter()
    private val request_code = 101
    lateinit private var staggeredLayoutManager: StaggeredGridLayoutManager
    private lateinit var toolbar: Toolbar
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.HOME
    lateinit var fragment:Fragment
    private val client = OkHttpClient()
    val MOVEVALUE = 240f


    override fun onButtonClicked(buttonCode: Int) {

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
        println("====we are on main $animateDirection")
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
        println("=============on main listener")
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

    //


    override fun onFragmentInteraction(fragment: Fragment, action:String) {

        when(action){

            "close" ->{
                val anim = AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(500);
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
               // i.putExtra("qString", R.drawable.judy_use)

               // val options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_up, R.anim.slide_up);
               // this.startActivity(i, options.toBundle())


                startActivityForResult(i, request_code)
              //  supportFragmentManager.beginTransaction().remove(fragment).commit()

            }




        }





    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        println("=======home check selecteed ${item?.itemId} and ${R.id.mClose} and ${R.id.miCompose}")

        when(item?.itemId){

            R.id.miCompose ->{
                containerOptionsDim.visibility = View.VISIBLE
                containerOptionsDim.bringToFront()
                containerOptions.bringToFront()

//
                val anim = AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(500);
                containerOptionsDim.startAnimation(anim);
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

                val anim = AlphaAnimation(1.0f, 0.0f);
                anim.setDuration(500);
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


                println("=======are in close")
              //  window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_down,R.anim.slide_down).remove(fragment).commit()
                getSupportActionBar()?.setDisplayHomeAsUpEnabled(false);
                getSupportActionBar()?.setDisplayShowHomeEnabled(false);

            }

        }
        return super.onOptionsItemSelected(item)
    }



    private lateinit var bottomNavigation: BottomNavigationView




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

      GlobalVals.onBoardingComplete = false

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



      GlobalVals.testUri.add("https://firebasestorage.googleapis.com/v0/b/photoglyde.appspot.com/o/video%2FVideos%2Fvideos%2FTrump%E2%80%99s%20Conflicting%20Border%20Wall%20Demands%20_%20The%20Daily%20Show.mp4?alt=media&token=fe5288d3-2754-42d8-9259-dc31a9e62ac7")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605138/nasa_orion_sm.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541597296/Nasa_orinon_swing_drop.mp4")

      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1538912176/porto.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605224/nasa_earth_night_rotate.mov")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605138/nasa_orion_sm.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541597296/Nasa_orinon_swing_drop.mp4")

      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1538912176/porto.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605224/nasa_earth_night_rotate.mov")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605138/nasa_orion_sm.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541597296/Nasa_orinon_swing_drop.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541410675/Product")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541408816/Product")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1538912176/porto.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605224/nasa_earth_night_rotate.mov")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605138/nasa_orion_sm.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541597296/Nasa_orinon_swing_drop.mp4")

      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1538912176/porto.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605224/nasa_earth_night_rotate.mov")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605138/nasa_orion_sm.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541597296/Nasa_orinon_swing_drop.mp4")

      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1538912176/porto.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605224/nasa_earth_night_rotate.mov")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541605138/nasa_orion_sm.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541597296/Nasa_orinon_swing_drop.mp4")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541410675/Product")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1541408816/Product")
      GlobalVals.testUri.add("https://res.cloudinary.com/demo/video/upload/v1538912176/porto.mp4")

//
//      GlobalVals.listCateg.add(Categories.CARS)
//      GlobalVals.listCateg.add(Categories.DESIGN)
//      GlobalVals.listCateg.add(Categories.DIY)
//      GlobalVals.listCateg.add(Categories.EXPLORE)
//      GlobalVals.listCateg.add(Categories.FOOD)
//      GlobalVals.listCateg.add(Categories.HOME)
//      GlobalVals.listCateg.add(Categories.MENS_STYLE)
//      GlobalVals.listCateg.add(Categories.RANDOM)
//      GlobalVals.listCateg.add(Categories.TECH)
//      GlobalVals.listCateg.add(Categories.TRAVEL)
//      GlobalVals.listCateg.add(Categories.WOMANS_STYLE)
//
//      PostUN().postCategory()

      //PostUN().downloadCategs()




      val apiCall = ArrayList<String>()
      apiCall.add(NEWS.TOPHEADLINES)
      apiCall.add(NEWS.SOURCES)
      apiCall.add(NEWS.EVERYTHING)

      GlobalVals.categoryList.addAll(arrayListOf("ABC News","Technology", "Turner Broadcasting", "Tesla", "All Weather", "Coming Home", "Random Shit", "Stub Hub Hotter", "Turner Broadcasting"))














      SetUpToolBar()
      setUpDummyUserStart()

      bottomNavigation = findViewById(R.id.navigation)
      initBottomNavigation()


      initFragment(savedInstanceState)


      val displayMetrics = DisplayMetrics()
      windowManager.defaultDisplay.getMetrics(displayMetrics)
      GlobalVals.heightWindow = displayMetrics.heightPixels
      GlobalVals.widthWindow = displayMetrics.widthPixels



  }





//    private fun initializedPagedListBuilder(config: PagedList.Config):
//            LivePagedListBuilder<String, GrabImageData> {
//
//        val dataSourceFactory = object : DataSource.Factory<String, GrabImageData>() {
//            override fun create(): DataSource<String, GrabImageData> {
//                return ImageDataSource()
//            }
//        }
//        return LivePagedListBuilder<String, GrabImageData>(dataSourceFactory, config)
//    }


    fun printThis(string:String){
        println("==================$string")
    }

    fun createInstance(fragment:Fragment){



        supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()


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
       // supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_black_18dp)
        supportActionBar?.setDisplayShowTitleEnabled(false)

      //  toolbar.searchBar.setCardViewElevation(0)
       // toolbar.searchBar.setSpeechMode(false)


//        toolbar.searchBar.setSearchIcon(R.id.)





        // supportActionBar?.elevation = 7.0f
      //  getSupportActionBar()?.setDisplayShowTitleEnabled(false)

    }

    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                println("main body" +response.body().toString())




            }

        })
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



    }






}
