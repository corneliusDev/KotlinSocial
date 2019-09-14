package com.photoglyde.justincornelius.photoglyde.Data



data class CoreData(
    var id: String? = "",
    var created: String? = "",
    var updated: String? = "",
    var width: Double? = 0.0,
    var height: Double? = 0.0,
    var description: String? = "",
    var urls: Urls? = null,
    var type: String? = "",
    var video_image: String? = "",
    var video_uri: String? = "",
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
