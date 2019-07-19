package com.example.moviepedia

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavouritesDao {
    @Insert
    fun insertRow(favourite: Favourites)

    @Query("Select * from Favourites where movie_id =  :id")
    fun checkFavourite(id : String) : Favourites

//    @Delete
//    fun deleteRow(favourite: Favourites) : Favourites

    @Query("Delete from Favourites where movie_id = :id")
    fun delete(id : String)
}