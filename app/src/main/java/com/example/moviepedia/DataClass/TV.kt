package com.example.moviepedia.DataClass

import com.example.moviepedia.Genre

data class TV(
    val total_pages : String,
    val results : ArrayList<TV_details>
)

data class TV_details(
    val id : String,
    val genres : ArrayList<Genre>,
    val name : String,
    val overview : String,
    val vote_average : String,
    val poster_path : String,
    val first_air_date : String
)