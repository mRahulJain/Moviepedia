package com.moviessquare.moviepedia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Watchlist(
    @PrimaryKey(autoGenerate = true)
    val id : Long?=null,
    val movie_id : String
)