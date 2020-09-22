package com.bignerdranch.android.basketballscore.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bignerdranch.android.basketballscore.Game

@Database(entities = [Game::class], version=1)
abstract class GameDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao
}