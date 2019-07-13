package com.example.moviepedia

data class Trending(
    val page : String,
    val total_pages : String,
    val results : ArrayList<Trending_results>
)

data class Trending_results(
    val id : String,
    val original_title : String,
    val original_name : String,
    val poster_path : String
)

data class Movie_search(
    val poster_path: String,
    val original_title: String
)