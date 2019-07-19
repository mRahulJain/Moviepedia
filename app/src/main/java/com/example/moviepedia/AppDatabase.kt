package com.example.moviepedia

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Users::class), version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UsersDao() : UsersDao
}


@Database(entities = arrayOf(Favourites::class), version = 1)
abstract class FavDatabase : RoomDatabase() {
    abstract fun FavDao() : FavouritesDao
}