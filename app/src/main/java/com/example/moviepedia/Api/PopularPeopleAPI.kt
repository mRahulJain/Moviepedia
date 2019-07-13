package com.example.moviepedia.Api

import com.example.moviepedia.DataClass.People
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularPeopleAPI {

    @GET("3/person/popular")
    fun getPopularPeople(
        @Query("api_key") key : String
    ) : Call<People>
}