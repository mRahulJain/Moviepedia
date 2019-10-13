package com.moviessquare.moviepedia.DataClass

import com.moviessquare.moviepedia.Genre

data class TV(
    val page : String,
    val total_pages : String,
    val results : ArrayList<TV_details>
)

data class TV_details(
    val rating : String,
    val id : String,
    val genres : ArrayList<Genre>,
    val name : String,
    val overview : String,
    val vote_average : String,
    val poster_path : String,
    val first_air_date : String
)

data class TVCast(
    val cast : ArrayList<People_results>
)