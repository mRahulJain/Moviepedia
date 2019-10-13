package com.moviessquare.moviepedia.DataClass

data class People(
    val page : String,
    val total_pages : String,
    val results : ArrayList<People_results>
)

data class People_results(
    val character : String,
    val id : String,
    val known_for_department : String,
    val also_known_as : ArrayList<String>,
    val biography : String,
    val popularity : String,
    val name : String,
    val profile_path : String
)

data class PeopleWork(
    val cast : ArrayList<Details_work>
)

data class PeoplePhoto(
    val profiles : ArrayList<Photo_work>
)

data class Details_work(
    val id : String,
    val original_title : String,
    val media_type : String,
    val poster_path : String
)

data class Photo_work(
    val file_path : String
)



