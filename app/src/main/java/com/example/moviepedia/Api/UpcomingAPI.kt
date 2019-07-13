package com.example.moviepedia.Api


import com.example.moviepedia.DataClass.Common
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UpcomingAPI {

    @GET("3/movie/upcoming")
    fun getUpcoming(
        @Query("api_key") key : String
    ) : Call<Common>
}