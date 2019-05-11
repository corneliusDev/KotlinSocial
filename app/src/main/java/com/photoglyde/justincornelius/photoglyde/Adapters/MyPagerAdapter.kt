package com.photoglyde.justincornelius.photoglyde.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.photoglyde.justincornelius.photoglyde.ProfileFragments.CollectionContent
import com.photoglyde.justincornelius.photoglyde.ProfileFragments.MyContent
import com.photoglyde.justincornelius.photoglyde.ProfileFragments.SavedContent


class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    // Returns total number of pages
    override fun getCount(): Int {
        return NUM_ITEMS
    }

    // Returns the fragment to display for that page
    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 // Fragment # 0 - This will show FirstFragment
            -> {
                println("==========frag 0")
                return MyContent.newInstance()}
            1 // Fragment # 0 - This will show FirstFragment different title
            -> {
                println("==========frag 1")
                return SavedContent.newInstance()}

            2 // Fragment # 1 - This will show SecondFragment
            ->{
                println("==========frag 2")
                return CollectionContent.newInstance()}
            else -> return null
        }
    }

    // Returns the page title for the top indicator
    override fun getPageTitle(position: Int): CharSequence? {
        return headers[position]
    }

    companion object {
        private val NUM_ITEMS = 3
        private val headers = arrayOf("photos", "collections", "saves")
    }

}