package com.example.moviepedia

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrendingAPI {

    @GET("3/trending/{media_type}/{time_window}")
    fun getTrending(
        @Path("media_type") type : String,
        @Path("time_window") time : String,
        @Query("api_key") key : String
    ) : Call<Trending>
}