package com.moviessquare.moviepedia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Rated(
    @PrimaryKey(autoGenerate = true)
    val id : Long?=null,
    val media_id : String
)