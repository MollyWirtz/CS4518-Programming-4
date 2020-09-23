package com.bignerdranch.android.basketballscore

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity (tableName = "table_game")
data class Game (
    @PrimaryKey var id: String,
    var teamAName: String,
    var teamBName: String,
    var teamAScore: Int,
    var teamBScore: Int,
    var date: Int
){}