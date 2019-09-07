package com.photoglyde.justincornelius.photoglyde.Data


class ImageClass(
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



