package com.bignerdranch.android.basketballscore

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class GameListViewModel : ViewModel() {

    private val gameRepository = GameRepository.get()
    val gameListLiveData = gameRepository.getGames()

    suspend fun insert(game: Game){
        gameRepository.insert(game)
    }

    suspend fun update(game: Game) {
        gameRepository.update(game)
    }

    suspend fun deleteTableContents() {
        gameRepository.deleteTableContents()
    }

}