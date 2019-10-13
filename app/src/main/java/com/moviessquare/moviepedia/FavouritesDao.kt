package com.moviessquare.moviepedia

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavouritesDao {
    @Insert
    fun insertRow(favourite: Favourites)

    @Query("Select * from Favourites where movie_id =  :id")
    fun checkFavourite(id : String) : Favourites

    @Query("Delete from Favourites where movie_id = :id")
    fun delete(id : String)
}