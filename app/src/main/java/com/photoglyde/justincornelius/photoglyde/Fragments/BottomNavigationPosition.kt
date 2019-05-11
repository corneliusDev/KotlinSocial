package com.photoglyde.justincornelius.photoglyde.Fragments

import android.support.v4.app.Fragment
import com.photoglyde.justincornelius.photoglyde.ExploreActivity
import com.photoglyde.justincornelius.photoglyde.R
import com.photoglyde.justincornelius.photoglyde.VideoPlayback.VideoActivity
import com.photoglyde.justincornelius.photoglyde.VideoPlayback.VideoFragment


enum class BottomNavigationPosition(val position: Int, val id: Int) {
    HOME(0, R.id.home),
    DASHBOARD(1, R.id.dashboard),
    NOTIFICATIONS(2, R.id.notifications),
    PROFILE(3, R.id.profile);
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
    BottomNavigationPosition.HOME.id -> BottomNavigationPosition.HOME
    BottomNavigationPosition.DASHBOARD.id -> BottomNavigationPosition.DASHBOARD
    BottomNavigationPosition.NOTIFICATIONS.id -> BottomNavigationPosition.NOTIFICATIONS
    BottomNavigationPosition.PROFILE.id -> BottomNavigationPosition.PROFILE
    else -> BottomNavigationPosition.HOME
}

fun BottomNavigationPosition.createFragment(): Fragment = when (this) {
    BottomNavigationPosition.HOME -> ExploreActivity.newInstance()
    BottomNavigationPosition.DASHBOARD -> ItemFragment.newInstance()
    BottomNavigationPosition.NOTIFICATIONS -> BlankFragment2.newInstance()
    BottomNavigationPosition.PROFILE -> BlankFragment.newInstance()
}

fun BottomNavigationPosition.getTag(): String = when (this) {
    BottomNavigationPosition.HOME -> ExploreActivity.TAG
    BottomNavigationPosition.DASHBOARD -> ItemFragment.TAG
    BottomNavigationPosition.NOTIFICATIONS -> BlankFragment2.TAG
    BottomNavigationPosition.PROFILE -> BlankFragment.TAG
}

