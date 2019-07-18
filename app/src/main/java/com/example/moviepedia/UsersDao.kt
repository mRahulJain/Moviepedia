package com.example.moviepedia

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UsersDao {
    @Insert
    fun insertRow(user: Users)

    @Query("Select * from Users where state = 1")
    fun getAcitveUser() : Users

}