package com.moviessquare.moviepedia

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface RatedDao {
    @Insert
    fun insertRow(rated: Rated)

    @Query("Select * from Rated where media_id = :id")
    fun getRated(id : String) : Rated

    @Query("Delete from Rated where media_id = :id")
    fun deletRated(id: String)
}