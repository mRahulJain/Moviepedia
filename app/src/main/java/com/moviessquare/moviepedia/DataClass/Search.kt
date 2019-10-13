package com.moviessquare.moviepedia.DataClass

data class Search(
    val results : ArrayList<Search_result>
)

data class Search_result(
    val media_type : String,
    val id : String,
    val profile_path : String,
    val poster_path : String,
    val name : String,
    val original_title : String
)