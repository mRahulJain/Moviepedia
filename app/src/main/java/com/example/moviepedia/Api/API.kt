package com.example.moviepedia.Api

import com.example.moviepedia.DataClass.Common
import com.example.moviepedia.DataClass.People
import com.example.moviepedia.Movie_search
import com.example.moviepedia.Trending
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {

    @GET("3/movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") key : String,
        @Query("page") page : String
    ) : Call<Common>

    @GET("3/movie/{id}")
    fun getMovie(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<Movie_search>

    @GET("3/movie/popular")
    fun getPopular(
        @Query("api_key") key : String,
        @Query("page") page : String
    ) : Call<Common>

    @GET("3/person/popular")
    fun getPopularPeople(
        @Query("api_key") key : String,
        @Query("page") number : Int
    ) : Call<People>

    @GET("3/movie/top_rated")
    fun getTopRated(
        @Query("api_key") key : String,
        @Query("page") page : String
    ) : Call<Common>

    @GET("3/trending/{media_type}/{time_window}")
    fun getTrending(
        @Path("media_type") type : String,
        @Path("time_window") time : String,
        @Query("api_key") key : String,
        @Query("page") page : String
    ) : Call<Trending>

    @GET("3/movie/upcoming")
    fun getUpcoming(
        @Query("api_key") key : String,
        @Query("page") page : String
    ) : Call<Common>
}