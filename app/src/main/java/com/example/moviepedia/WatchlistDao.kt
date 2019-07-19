package com.example.moviepedia

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface WatchlistDao {
    @Insert
    fun insertRow(watchlist: Watchlist)

    @Query("Select * from Watchlist where movie_id =  :id")
    fun checkWatchlist(id : String) : Watchlist


    @Query("Delete from Watchlist where movie_id = :id")
    fun delete(id : String)
}