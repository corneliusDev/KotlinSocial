package com.photoglyde.justincornelius.photoglyde.Networking

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import org.jetbrains.annotations.Nullable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*
import kotlin.collections.ArrayList

interface NYTimeService {
    @GET("{section}")
    fun getPosts(@Path("section") section: String? = null,
                 @Query("country") country: String? = null,
                 @Query("apiKey") apiKey: String? = null): Call<NYCTimesResponse>

    @GET("{section}")
    fun getEverything(@Path("section") section: String? = null,
                 @Query("q") q: String? = null,
                 @Query("apiKey") apiKey: String? = null): Call<NYCTimesResponse>


    @GET("{section}")
    fun getSources(@Path("section") section: String? = null,
                   @Query("apiKey") apiKey: String? = null): Call<NYCTimesResponse>

    companion object {
        private const val BASE_URL = "https://newsapi.org/v2/"

        fun createService(): NYTimeService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NYTimeService::class.java)
        }
    }


//    "https://api.nytimes.com/svc/mostpopular/v2/mostemailed/Arts/1.json?api-key=2b0818c86c3046c99ff2e04cd5fc7458"
}

data class NYCTimesResponse(@Nullable@field:Json(name = "articles") val articles:ArrayList<NYCTimesDataResponse>)



data class NYCTimesDataResponse(
    @field:Json(name = "articles") val author: String? = null,
    @field:Json(name = "articles") val title: String?,
    @field:Json(name = "articles") val description: String?,
    @field:Json(name = "articles") val url: String?,
    @field:Json(name = "articles") val urlToImage: String?,
    @field:Json(name = "articles") val publishedAt: String?,
    @field:Json(name = "articles") val content: String?,
    @field:Json(name = "articles") val media: ArrayList<NYCTimesMedia?>



)

data class NYCTimesMedia(
    @field:Json(name = "media") val type: String?,
    @SerializedName("media-metadata") val media_metadata: ArrayList<NYCTimesMediaInfo>?
)

class NYCTimesMediaInfo(
    @field:Json(name = "media-metadata") val url: String?,
    @field:Json(name = "media-metadata") val format: String?,
    @field:Json(name = "media-metadata") val height: String?,
    @field:Json(name = "media-metadata") val width: String?
)