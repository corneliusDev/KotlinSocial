package com.photoglyde.justincornelius.photoglyde.Data

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import com.photoglyde.justincornelius.photoglyde.Networking.NYCTimesDataResponse
import com.squareup.moshi.Json

class UserInfo(
    var email:String? = "",
    var userName:String? = "",
    var age:String? = "",
    var userDescription:String? = "",
    var userImageURI: Uri? = null,
    var userImageBitmap:Bitmap? = null,
    var userImages:ArrayList<ImageClass>? = null,
    var userVideo:ArrayList<VideoClass>? = null,
    var savedArticles:ArrayList<NYCTimesDataResponse>? = null
)

class ImageClass(
//    var urls: UrlsCall?,
//    var created: String?,
//    var location:Location?,
//    var categoryTag:ArrayList<String>?,
//    var ownerName:String? = "",
//    var ownerUID:String? = "",
//    var width:Int = 0,
//    var height:Int = 0,
//    val profile_image:ProfileImageCall?

    var id: String? = "",
    var created: String? = "",
    var updated: String? = "",
    var width: Double? = 0.0,
    var height: Double? = 0.0,
    var description: String? = "",
    var urls: Urls? = null,
    var username: String? = "",
    var first_name: String? = "",
    var last_name: String? = "",
    var type: String? = "",
    var twitter_username: String? = "",
    var video_image: String? = "",
    var video_uri: String? = "",
    var profile_image: ProfileImage? = null,
    var categ_name:String? = "",
    var categ_image_uri:String? = "",
    var categ_created:String? = "",
    var file:String? = ""

)

//class VideoClass(
//    var imageUri:Uri?,
//    var timeStamp:Int = 0,
//    var videoUri:Uri?,
//    var location:LatLng?,
//    var categoryTag:ArrayList<String>?,
//    var ownerName:String? = "",
//    var ownerUID:String? = "",
//    var width:Int = 0,
//    var height:Int = 0
//)

data class VideoClass(
    var id: String? = null,
    var created: String? = "",
    var updated: String? = "",
    var width: Double? = 0.0,
    var height: Double? = 0.0,
    var description: String? = "",
    var username: String? = "",
    var first_name: String? = "",
    var last_name: String? = "",
    var type: String? = "",
    var twitter_username: String? = "",
    var video_image:String? = null,
    var video_uri:String? = null
)

class Interest(
  var interest:String? = "",
  var interestUriImage:Uri

)

