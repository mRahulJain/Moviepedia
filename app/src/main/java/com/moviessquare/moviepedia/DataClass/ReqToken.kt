package com.moviessquare.moviepedia.DataClass

data class ReqToken(
    val request_token : String
)

data class Session(
    val session_id : String
)

data class AccDetails(
    val id : String
)

data class fav(
    val media_type : String,
    val media_id : Int,
    val favorite : Boolean
)

data class watchlist(
    val media_type : String,
    val media_id : Int,
    val watchlist : Boolean
)

data class Rate(
    val value : Float
)