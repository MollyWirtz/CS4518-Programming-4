package com.bignerdranch.android.basketballscore

import android.util.Log
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class GameListViewModel : ViewModel() {
    val games = mutableListOf<Game>()

    init{
        for (i in 0 until 100) {
            val game = Game()
            game.index = "#$i"
            game.date = getSimpleDate()
            game.teamA = getRandomName()
            game.teamB = getRandomName()
            game.scoreA = getRandomScore()
            game.scoreB = getRandomScore()
            getWinner(game)

            games += game
        }
    }

    fun getRandomName(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var name = ""
        for (i in 0..5) {
            name += chars[Math.floor(Math.random() * chars.length).toInt()]
        }
        return name
    }

    fun getRandomScore(): String {
        return Random.nextInt(0, 100).toString()
    }

    fun getSimpleDate(): String {
        val formatter = SimpleDateFormat("MM/dd/yyyy hh:mm")
        val strDate: String = formatter.format(Date())
        return strDate
    }

    fun getWinner(game: Game) {
        if (game.scoreA.toInt() > game.scoreB.toInt()) {
            game.winner = game.teamA
        }
        else {
            game.winner = game.teamB
        }
    }
}