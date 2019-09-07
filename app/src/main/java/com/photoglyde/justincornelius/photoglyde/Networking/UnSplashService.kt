package com.photoglyde.justincornelius.photoglyde.Networking

import com.photoglyde.justincornelius.photoglyde.Data.UnSplashBegin
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UnSplashService {
    @Headers("Authorization: Client-ID 4fca14a83206184190e2d00cf0336a7adec837411321bb22703561656ff8f231")
    @GET("search/{photos}")
    fun getPosts(@Path("photos") photos: String? = null, @Query("query") query: String? = null, @Query("page") page: String? = null, @Query("per_page") per_page: String? = null): Call<UnSplashBegin>

    companion object {
        private const val BASE_URL = "https://api.unsplash.com/"

        fun createService(): UnSplashService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UnSplashService::class.java)
        }
    }

}

