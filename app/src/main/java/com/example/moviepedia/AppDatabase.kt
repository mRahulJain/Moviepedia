package com.example.moviepedia

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = arrayOf(Users::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UsersDao() : UsersDao
}