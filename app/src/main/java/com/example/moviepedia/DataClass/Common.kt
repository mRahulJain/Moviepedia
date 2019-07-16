package com.example.moviepedia.DataClass

data class Common(
    val page : String,
    val total_pages : String,
    val results : ArrayList<Common_results>
)

data class Common_results(
    var id : String,
    var original_title : String,
    var poster_path : String
)

data class Video(
    val results : ArrayList<Movie_videos>
)

data class Movie_videos(
    val key : String,
    val name : String
)
