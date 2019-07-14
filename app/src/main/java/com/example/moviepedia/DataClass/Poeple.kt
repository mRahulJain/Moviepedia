package com.example.moviepedia.DataClass

data class People(
    val page : String,
    val total_pages : String,
    val results : ArrayList<People_results>
)

data class People_results(
    val id : String,
    val name : String,
    val profile_path : String
)

data class People_search(
    val profile_path : String,
    val name : String
)