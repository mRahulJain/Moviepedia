package com.example.moviepedia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favourites(
    @PrimaryKey(autoGenerate = true)
    val id : Long?=null,
    val movie_id : String
)