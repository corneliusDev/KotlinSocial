package com.photoglyde.justincornelius.photoglyde.data

import android.os.Parcelable

object GlobalValues {
    var heightWindow = 0
    var widthWindow= 0
    var recyclerState2: Parcelable? = null
    var videoWatchState: Parcelable? = null
    var recyclerStaggered: Parcelable? = null
    var awayFromFrag = false
    var picassoUnit = ArrayList<Any>()
    var whatsNew = false
    var transitionRatioHeight = ArrayList<Int>()
    var cameFromMain = false
    var videoWatch = false
    val CATEGORY = "category"
    var listCateg = ArrayList<CoreData?>()
    var currentCore:CoreData? = null
    var cameFromCateg = false
}
