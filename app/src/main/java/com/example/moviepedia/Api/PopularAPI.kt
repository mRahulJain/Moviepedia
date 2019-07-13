package com.example.moviepedia.Api

import com.example.moviepedia.DataClass.Common
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularAPI {

    @GET("3/movie/popular")
    fun getPopular(
        @Query("api_key") key : String
    ) : Call<Common>
}