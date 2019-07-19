package com.example.moviepedia

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Users(
    @PrimaryKey(autoGenerate = true)
    val id : Long?=null,
    val name : String,
    val username : String,
    val password : String,
    val session_id : String
)