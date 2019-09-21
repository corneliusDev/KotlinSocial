package com.photoglyde.justincornelius.photoglyde.UI.activity

import android.animation.ObjectAnimator
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import com.photoglyde.justincornelius.photoglyde.UI.BottomNavigation.*
import com.photoglyde.justincornelius.photoglyde.data.*
import com.photoglyde.justincornelius.photoglyde.UI.fragment.MainFeed
import com.photoglyde.justincornelius.photoglyde.UI.fragment.StaggeredFeedFragment
import com.photoglyde.justincornelius.photoglyde.data.DownloadCategories
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.UI.fragment.VideoWatch
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    MainFeed.OnFragmentInteractionListenerExplore,
    StaggeredFeedFragment.StaggeredFeedFragmentListener, VideoWatch.OnVideoWatchListener {

    private lateinit var toolbar: Toolbar
    private var navPosition: BottomNavigationPosition = BottomNavigationPosition.HOME
    private val MOVE_UP = 240f
    private val MOVE_ORIGIN = 0f
    private lateinit var bottomNavigation: BottomNavigationView


    override fun onPause() {
        super.onPause()
        GlobalValues.cameFromMain = false
    }

    override fun onResume() {
        super.onResume()
        GlobalValues.cameFromMain = true
    }

    override fun onVideoWatchInteraction(animate: String) {
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

    override fun staggeredFeedFragmentInteractor(animate: String) {

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


    override fun onFragmentInteractionExplore(fragment: androidx.fragment.app.Fragment, animateDirection: String) {

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




        }


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

        if (GlobalValues.listCateg.size == 0) {
            DownloadCategories()
                .downloadCategs(object : DownloadCategories.DownloadCategories {
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
        GlobalValues.heightWindow = displayMetrics.heightPixels
        GlobalValues.widthWindow = displayMetrics.widthPixels
    }

    private fun androidx.fragment.app.FragmentManager.detach() {
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

    private fun androidx.fragment.app.FragmentManager.findFragment(position: BottomNavigationPosition): androidx.fragment.app.Fragment {
        return findFragmentByTag(position.getTag()) ?: position.createFragment()
    }

    private fun fragmentReturn(fragment: androidx.fragment.app.Fragment) : androidx.fragment.app.FragmentTransaction {
       return supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_up,
                R.anim.slide_up
            )
            .replace(R.id.containerMap, fragment)

    }

    private fun fragmentRemove(fragment: androidx.fragment.app.Fragment) : androidx.fragment.app.FragmentTransaction {
        return supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_down,
                R.anim.slide_down
            )
            .remove(fragment)

    }

    private fun androidx.fragment.app.FragmentManager.attach(fragment: androidx.fragment.app.Fragment, tag: String) {
        if (fragment.isDetached) {
            beginTransaction().attach(fragment).commit()
        } else {
            beginTransaction().add(R.id.container, fragment, tag).commit()
        }
        // Set a transition animation for this transaction.
        beginTransaction().setTransition(androidx.fragment.app.FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit()
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
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_black_18dp)
            setDisplayShowTitleEnabled(false)
        }
    }

    private fun fragmentAnimator(direction:String)  : ObjectAnimator {

        return when(direction){

            UP -> ObjectAnimator.ofFloat(navigation, TRANSLATION_Y, MOVE_ORIGIN)

            DOWN -> ObjectAnimator.ofFloat(navigation, TRANSLATION_Y, MOVE_UP)

            else -> ObjectAnimator.ofFloat(navigation, TRANSLATION_Y, MOVE_ORIGIN)
        }


    }


}


