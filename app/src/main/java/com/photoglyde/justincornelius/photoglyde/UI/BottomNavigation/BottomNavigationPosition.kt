package com.photoglyde.justincornelius.photoglyde.UI.BottomNavigation

import com.photoglyde.justincornelius.photoglyde.UI.fragment.VideoWatch
import com.photoglyde.justincornelius.photoglyde.UI.fragment.MainFeed
import com.photoglyde.justincornelius.photoglyde.UI.fragment.StaggeredFeedFragment
import com.photoglyde.justincornelius.photoglyde.R


enum class BottomNavigationPosition(val position: Int, val id: Int) {
    HOME(0, R.id.home),
    DASHBOARD(1, R.id.dashboard),
    NOTIFICATIONS(2, R.id.notifications)
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
    BottomNavigationPosition.HOME.id -> BottomNavigationPosition.HOME
    BottomNavigationPosition.DASHBOARD.id -> BottomNavigationPosition.DASHBOARD
    BottomNavigationPosition.NOTIFICATIONS.id -> BottomNavigationPosition.NOTIFICATIONS
    else -> BottomNavigationPosition.HOME
}

fun BottomNavigationPosition.createFragment(): androidx.fragment.app.Fragment = when (this) {
    BottomNavigationPosition.HOME -> MainFeed.newInstance()
    BottomNavigationPosition.DASHBOARD -> StaggeredFeedFragment.newInstance()
    BottomNavigationPosition.NOTIFICATIONS -> VideoWatch.newInstance()
}

fun BottomNavigationPosition.getTag(): String = when (this) {
    BottomNavigationPosition.HOME -> MainFeed.TAG
    BottomNavigationPosition.DASHBOARD -> StaggeredFeedFragment.TAG
    BottomNavigationPosition.NOTIFICATIONS -> VideoWatch.TAG
}

