package com.photoglyde.justincornelius.photoglyde

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.photoglyde.justincornelius.photoglyde.BottomNavigation.*
import com.photoglyde.justincornelius.photoglyde.Camera.CamerOpenActivity
import com.photoglyde.justincornelius.photoglyde.Data.*
import com.photoglyde.justincornelius.photoglyde.Fragments.ExploreActivity
import com.photoglyde.justincornelius.photoglyde.Fragments.PostingOptions
import com.photoglyde.justincornelius.photoglyde.Fragments.WhatsNew
import com.photoglyde.justincornelius.photoglyde.Networking.PostUN
import com.photoglyde.justincornelius.photoglyde.Utilities.AnimateWindow
import com.photoglyde.justincornelius.photoglyde.Web.MapFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    PostingOptions.OnFragmentInteractionListener,
    ExploreActivity.OnFragmentInteractionListenerExplore,
    WhatsNew.OnListFragmentInteractionListener {

    private val request_code = 101
    private lateinit var toolbar: Toolbar
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.HOME
    lateinit var fragmentPosting: Fragment
    private val MOVE_UP = 240f
    private val MOVE_ORIGIN = 0f
    private var mapFragment = MapFragment.newInstance()
    private lateinit var bottomNavigation: BottomNavigationView


    override fun onPause() {
        super.onPause()
        GlobalVals.cameFromMain = false
    }

    override fun onResume() {
        super.onResume()
        GlobalVals.cameFromMain = true
    }


    override fun onListFragmentInteraction(animate: String) {

        when (animate) {

            DOWN -> {
                fragmentAnimator(DOWN).apply {
                    duration = ANI_DURATION
                    start()
                }
            }

            UP -> {

                fragmentAnimator(UP).apply {
                    duration = ANI_DURATION
                    start()
                }
            }

        }
    }


    override fun onFragmentInteractionExplore(fragment: Fragment, animateDirection: String) {

        when (animateDirection) {

            DOWN -> {
                fragmentAnimator(DOWN).apply {
                    duration = ANI_DURATION
                    start()
                }
            }

            UP -> {

                fragmentAnimator(UP).apply {
                    duration = ANI_DURATION
                    start()
                }
            }

            MAP_OPEN -> {

                containerOptionsDim.visibility = View.VISIBLE
                containerOptionsDim.bringToFront()
                containerMap.visibility = View.VISIBLE
                containerMap.bringToFront()
                containerOptionsDim.isEnabled = false



                AnimateWindow().animateUp(this, containerOptionsDim, object : AnimateWindow.AnimationEnd {

                    override fun animationOver() {
                        fragmentReturn(mapFragment).commit()
                    }

                })

//                supportFragmentManager
//                    .beginTransaction()
//                    .setCustomAnimations(R.anim.slide_up, R.anim.slide_up)
//                    .replace(R.id.containerMap, mapFragment)
//                    .commit()
            }


        }


    }

    override fun onFragmentInteraction(fragment: Fragment, action: String) {

        when (action) {

            CLOSE -> {

                val anim = AlphaAnimation(1.0f, 0.0f)
                anim.duration = ANI_DURATION
                containerOptionsDim.startAnimation(anim)
                fragmentRemove(fragment).commit()



                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        containerOptionsDim.visibility = View.GONE
                    }

                    override fun onAnimationStart(p0: Animation?) {

                    }
                })
            }

            CAMERA -> {

                val i = Intent(this, CamerOpenActivity::class.java)
                startActivityForResult(i, request_code)

            }


        }


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {


        when (item?.itemId) {

            R.id.miCompose -> {

                containerOptionsDim.visibility = View.VISIBLE
                containerOptionsDim.bringToFront()
                containerOptions.bringToFront()
                val anim = AlphaAnimation(0.0f, 1.0f)
                anim.duration = ANI_DURATION
                containerOptionsDim.startAnimation(anim)
                fragmentPosting = PostingOptions.newInstance()
                fragmentReturn(fragmentPosting).commit()


            }


            R.drawable.ic_close -> {

                val anim = AlphaAnimation(1.0f, 0.0f)
                anim.duration = ANI_DURATION
                containerOptionsDim.startAnimation(anim)

                anim.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(p0: Animation?) {

                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        containerOptionsDim.visibility = View.GONE
                        fragmentRemove(fragmentPosting).commit()
                    }

                    override fun onAnimationStart(p0: Animation?) {

                    }
                })

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

        if (GlobalVals.listCateg.size == 0) {
            PostUN().downloadCategs(object : PostUN.DownloadCategories {
                override fun onCallBack(bool: Boolean) {

                }
            })
        }

        bottomNavigation = findViewById(R.id.navigation)

        SetUpToolBar()
        getDisplayDimensions()
        initBottomNavigation()
        initFragment(savedInstanceState)





    }


    private fun getDisplayDimensions(){
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        GlobalVals.heightWindow = displayMetrics.heightPixels
        GlobalVals.widthWindow = displayMetrics.widthPixels
    }

    private fun FragmentManager.detach() {
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

    private fun fragmentReturn(fragment: Fragment) : FragmentTransaction {
       return supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_up, R.anim.slide_up)
            .replace(R.id.containerOptions, fragment)

    }

    private fun fragmentRemove(fragment: Fragment) : FragmentTransaction {
        return supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_down, R.anim.slide_down)
            .remove(fragment)

    }

    private fun FragmentManager.attach(fragment: Fragment, tag: String) {
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


    private fun SetUpToolBar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_black_18dp)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    private fun fragmentAnimator(direction:String)  : ObjectAnimator {

        return when(direction){

            UP -> ObjectAnimator.ofFloat(navigation, TRANSLATION_Y, MOVE_ORIGIN)

            DOWN -> ObjectAnimator.ofFloat(navigation, TRANSLATION_Y, MOVE_UP)

            else -> ObjectAnimator.ofFloat(navigation, TRANSLATION_Y, MOVE_ORIGIN)
        }


    }


}
