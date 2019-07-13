package com.example.moviepedia

data class Trending(
    val results : ArrayList<Trending_results>
)

data class Trending_results(
    val original_title : String,
    val original_name : String,
    val poster_path : String
)