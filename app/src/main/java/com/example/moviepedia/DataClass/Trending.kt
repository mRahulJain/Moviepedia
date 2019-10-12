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
    val poster_path : String,
    val media_type : String
)

data class Movie_search(
    val id : String,
    val genres : ArrayList<Genre>,
    val poster_path: String,
    val original_title: String,
    val overview : String,
    val release_date : String,
    val tagline : String,
    val vote_average : String,
    val rating : Float
)

data class Movie_review_search(
    val page : String,
    val total_pages : String,
    val results: ArrayList<reviews>
)

data class reviews(
    val author : String,
    val content : String
)

data class Genre(
    val name : String
)