package com.bignerdranch.android.basketballscore.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.bignerdranch.android.basketballscore.Game

@Dao
interface GameDao{
    @Query("SELECT * FROM table_game")
    fun getGames(): LiveData<List<Game>>

    @Query("SELECT * FROM table_game WHERE `id`=(:id)") // maybe
    fun getGame(id: String): LiveData<Game?>

}