package com.bignerdranch.android.basketballscore.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bignerdranch.android.basketballscore.Game

@Dao
interface GameDao{
    @Query("SELECT * FROM table_game")
    fun getGames(): LiveData<List<Game>>

    @Query("SELECT * FROM table_game WHERE `id`=(:id)")
    fun getGame(id: String): LiveData<Game>

    @Insert
    suspend fun insert(game: Game)

    @Update
    suspend fun update(game: Game)

    @Query ("DELETE FROM table_game")
    suspend fun deleteTableContents()

}