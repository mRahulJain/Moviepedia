package com.example.moviepedia.Api

import com.example.moviepedia.DataClass.Common
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TopRatedAPI {

    @GET("3/movie/top_rated")
    fun getTopRated(
        @Query("api_key") key : String
    ) : Call<Common>
}