package com.photoglyde.justincornelius.photoglyde.Data

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json

data class PexelData(@field:Json(name = "photos") val photos:ArrayList<photo>)

data class photo(
@SerializedName("width") val width: Double? = 0.0,
@SerializedName("height") val height: Double? = 0.0,
@SerializedName("url") val url: String?,
@field:Json(name = "src") val src: Links?)

data class Links(
    @SerializedName("original") @field:Json(name = "src")val original: String?,
    @SerializedName("large") @field:Json(name = "src")val large: String?,
    @SerializedName("large2x") @field:Json(name = "src")val large2x: String?,
    @SerializedName("medium") @field:Json(name = "src")val medium: String?,
    @SerializedName("small") @field:Json(name = "src")val small: String?
)