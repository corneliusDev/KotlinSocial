package com.photoglyde.justincornelius.photoglyde.Data

import com.google.gson.annotations.SerializedName
import com.photoglyde.justincornelius.photoglyde.Networking.NYCTimesMedia
import com.squareup.moshi.Json

data class UnSplashBegin(@field:Json(name = "results") val results:ArrayList<CoreUnSplashCall>)

data class CategBegin(@field:Json(name = "category") val category:ArrayList<CoreUnSplashCall>)

data class CoreUnSplashCall(
    @SerializedName("id") val id: String? = null,
    @SerializedName("created_at") val created: String?,
    @SerializedName("updated_at") val updated: String?,
    @SerializedName("width") val width: Double? = 0.0,
    @SerializedName("height") val height: Double? = 0.0,
    @SerializedName("description") val description: String?,
    @field:Json(name = "urls") val urls: UrlsCall?,
    @SerializedName("username") val username: String?,
    @SerializedName("first_name") val first_name: String?,
    @SerializedName("last_name") val last_name: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("twitter_username") val twitter_username: String?,
    @SerializedName("profile_image") val profile_image:ProfileImageCall?,
    @SerializedName("location") val location:Location?
)


data class UrlsCall(
    @SerializedName("raw") @field:Json(name = "urls")val raw: String?,
    @SerializedName("full") @field:Json(name = "urls")val full: String?,
    @SerializedName("regular") @field:Json(name = "urls")val regular: String?,
    @SerializedName("small") @field:Json(name = "urls")val small: String?,
    @SerializedName("thumb") @field:Json(name = "urls")val thumb: String?
)


data class ProfileImageCall(
    @field:Json(name = "small") val small: String?,
    @field:Json(name = "medium") val full: String?,
    @field:Json(name = "large") val large: String?
)

data class Location(
    @field:Json(name = "city") val city: String?,
    @field:Json(name = "country") val country: String?,
    @field:Json(name = "position") val position: LATLONG? = null
)

data class LATLONG(
    @field:Json(name = "latitude") val latitude: Double? = 0.0,
    @field:Json(name = "longitude") val longitude: Double? = 0.0
)


data class CoreUnSplash(
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
    var user:Locate? = null,
    var collection:Collection? = null
)

data class Collection(
    var first:String? = "",
    var second:String? = "",
    var third:String? = "",
    var fourth:String? = ""
)

data class Locate(
    var location:String? = ""
)


data class Urls(
    val raw: String? = "",
    val full: String? = "",
    var regular: String? = "",
    val small: String? = "",
    val thumb: String? = ""
)


data class ProfileImage(
    val small: String? = "",
    val full: String? = "",
    val large: String? = ""
)