package com.example.moviepedia.DataClass

data class Common(
    val page : String,
    val total_pages : String,
    val results : ArrayList<Common_results>
)

data class Common_results(
    val id : String,
    val original_title : String,
    val poster_path : String
)