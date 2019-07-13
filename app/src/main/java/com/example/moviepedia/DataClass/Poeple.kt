package com.example.moviepedia.DataClass

data class People(
    val results : ArrayList<People_results>
)

data class People_results(
    val name : String,
    val profile_path : String
)