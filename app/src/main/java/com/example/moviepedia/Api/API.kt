package com.example.moviepedia.Api

import com.example.moviepedia.DataClass.*
import com.example.moviepedia.Movie_review_search
import com.example.moviepedia.Movie_search
import com.example.moviepedia.Trending
import retrofit2.Call
import retrofit2.http.*

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

    @GET("3/movie/{id}/similar")
    fun getSimilarMovie(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<Common>

    @GET("3/movie/{id}/reviews")
    fun getReview(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<Movie_review_search>

    @GET("3/person/{id}")
    fun getPeople(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<People_results>

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

    @GET("3/movie/{id}/videos")
    fun getVideo(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<Video>

    @GET("3/tv/{id}/videos")
    fun getVideoTV(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<Video>

    @GET("3/person/{id}/combined_credits")
    fun getPeopleWork(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<PeopleWork>

    @GET("3/person/{id}/images")
    fun getPhoto(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<PeoplePhoto>

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

    @GET("3/tv/airing_today")
    fun getTVAiringToday(
        @Query("api_key") key : String
    ) : Call<TV>

    @GET("3/tv/on_the_air")
    fun getTVonAir(
        @Query("api_key") key : String
    ) : Call<TV>

    @GET("3/tv/popular")
    fun getTVPopular(
        @Query("api_key") key : String
    ) : Call<TV>

    @GET("3/tv/top_rated")
    fun getTVTopRated(
        @Query("api_key") key : String
    ) : Call<TV>

    @GET("3/tv/airing_today")
    fun getTVAiringTodayF(
        @Query("api_key") key : String,
        @Query("page") page : String
    ) : Call<TV>

    @GET("3/tv/on_the_air")
    fun getTVonAirF(
        @Query("api_key") key : String,
        @Query("page") page : String
    ) : Call<TV>

    @GET("3/tv/popular")
    fun getTVPopularF(
        @Query("api_key") key : String,
        @Query("page") page : String
    ) : Call<TV>

    @GET("3/tv/top_rated")
    fun getTVTopRatedF(
        @Query("api_key") key : String,
        @Query("page") page : String
    ) : Call<TV>

    @GET("3/tv/{id}")
    fun getTV(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<TV_details>
    @GET("3/tv/{id}/similar")
    fun getTVSimilar(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<TV>

    @GET("3/tv/{id}/reviews")
    fun getTVReviews(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<TV>

    @GET("3/tv/{id}/credits")
    fun getTVCast(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<TVCast>

    @GET("3/movie/{id}/credits")
    fun getCast(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<TVCast>

    @GET("3/tv/{id}/reviews")
    fun getReviewTV(
        @Path("id") id : Int,
        @Query("api_key") key : String
    ) : Call<Movie_review_search>

    @GET("3/search/multi")
    fun getSearch(
        @Query("api_key") key : String,
        @Query("query") query : String
    ) : Call<Search>

    @GET("3/authentication/token/new")
    fun generateRequestToken(
        @Query("api_key") key : String
    ) : Call<ReqToken>

    @POST("authenticate/{REQUEST_TOKEN}/allow")
    fun allowToken(
        @Path("REQUEST_TOKEN") token : String
    ) : Call<Unit>

    @POST("3/authentication/session/new")
    fun create(
        @Body token: ReqToken,
        @Query("api_key") key : String
    ) : Call<Session>

    @GET("3/account")
    fun getAccountDetail(
        @Query("api_key") key : String,
        @Query("session_id") id : String
    ) : Call<AccDetails>

    @POST("3/account/{account_id}/favorite")
    fun putFavourite(
        @Path("account_id") id : String,
        @Header("Content_type")type: String,
        @Body fab : fav,
        @Query("api_key") key : String,
        @Query("session_id") session_id : String
    ) : Call<Unit>

    @POST("3/account/{account_id}/watchlist")
    fun putWatchlist(
        @Path("account_id") id : String,
        @Header("Content_type")type: String,
        @Body watchL : watchlist,
        @Query("api_key") key : String,
        @Query("session_id") session_id : String
    ) : Call<Unit>

    @GET("3/account/{account_id}/favorite/movies")
    fun getFavouriteMovie(
        @Path("account_id") id : String,
        @Query("api_key") key : String,
        @Query("session_id") session_id : String,
        @Query("page") num : Int
    ) : Call<Common>

    @GET("3/account/{account_id}/favorite/tv")
    fun getFavouriteTV(
        @Path("account_id") id : String,
        @Query("api_key") key : String,
        @Query("session_id") session_id : String,
        @Query("page") num : Int
    ) : Call<TV>

    @GET("3/account/{account_id}/watchlist/movies")
    fun getMovieWatchlist(
        @Path("account_id") id : String,
        @Query("api_key") key : String,
        @Query("session_id") session_id : String,
        @Query("page") num : Int
    ) : Call<Common>

    @GET("3/account/{account_id}/watchlist/tv")
    fun getTVWatchlist(
        @Path("account_id") id : String,
        @Query("api_key") key : String,
        @Query("session_id") session_id : String,
        @Query("page") num : Int
    ) : Call<TV>
}