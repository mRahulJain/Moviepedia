package com.example.moviepedia.DataClass

data class Common(
    val results : ArrayList<Common_results>
)

data class Common_results(
    val original_title : String,
    val poster_path : String
)