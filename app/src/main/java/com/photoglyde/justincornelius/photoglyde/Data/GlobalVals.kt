package com.photoglyde.justincornelius.photoglyde.Data

import android.content.Context
import android.net.Uri
import android.os.Parcelable
import android.support.design.internal.ParcelableSparseArray
import android.util.SparseArray
import com.google.android.exoplayer2.ui.PlayerView
import com.photoglyde.justincornelius.photoglyde.Networking.NYCTimesDataResponse
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

object GlobalVals {

 var apiCount = 19
   var heightWindow = 0
    var widthWindow= 0
    var recyclerState2: Parcelable? = null
    var videoWatchState: Parcelable? = null
    var recyclerStateNews: Parcelable? = null
    var recyclerStateCategExplore: Parcelable? = null
   var myContentState: Parcelable? = null
    var pagerState: Parcelable? = null
    var pagerStateTest = SparseArray<Parcelable>()
    var loadPicasso = true
    var loadPicassoFromExplore = true
    var test = 0
    var exploreLoad = 0
 var loadNews = 0
    var imageClassUser = ArrayList<GrabImageData?>()
   var test2 = ArrayList<Uri>()
    var isExpanded = false
    var awayFromFrag = false
    var picassoUnit = ArrayList<Any>()
    var categoryList = ArrayList<String?>()
    var savedNews = ArrayList<NYCTimesDataResponse>()
    var currentUser:UserInfo? = null
    var currentCreator:RequestCreator? = null
   var testUri = ArrayList<String>()
    var whatsNew = false
    var upSplash = ArrayList<CoreUnSplash>()
    var transitionRatioHeight = ArrayList<Int>()
    var currentTransitionHeight = 0
 var cameFromMain = false
    var videoWatch = false
    val CATEGORY = "category"
    var listCateg = ArrayList<CoreUnSplash?>()
    var cameFromExa = false
    var onBoardingComplete = true
    var currentCore:CoreUnSplash? = null

}

object Categories {
    val CARS = "cars"
    val DESIGN = "design"
    val DIY = "diy"
    val EXPLORE = "explore"
    val FOOD = "food"
    val HOME = "home"
    val MENS_STYLE = "mens style"
    val RANDOM = "random"
    val TECH = "tech"
    val TRAVEL = "travel"
    val WOMANS_STYLE = "womans style"


}