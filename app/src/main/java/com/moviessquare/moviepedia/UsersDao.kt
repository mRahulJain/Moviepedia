package com.moviessquare.moviepedia

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UsersDao {
    @Insert
    fun insertRow(user: Users)

    @Query("Select * from Users")
    fun getUser() : Users

    @Query("Delete from Users")
    fun deleteUser()
}