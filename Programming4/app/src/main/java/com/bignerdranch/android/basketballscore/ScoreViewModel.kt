package com.bignerdranch.android.basketballscore

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "ScoreViewModel"

class ScoreViewModel: ViewModel() {

    // Initialize scores
    val teamA = TeamA("Team A", 0)
    val teamB = TeamB("Team B", 0)

    // Method to increase score
    fun addScore(score: Int, team: Char) {

        // If passed score is 0, reset both scores
        if (score == 0) {
            teamA.score = 0
            teamB.score = 0
        }
        else if (team == 'A') {
            teamA.score = teamA.score + score
        }
        else {
            teamB.score = teamB.score + score
        }

    }

}