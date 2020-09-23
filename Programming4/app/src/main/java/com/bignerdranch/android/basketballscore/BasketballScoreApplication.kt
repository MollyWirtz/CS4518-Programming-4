package com.bignerdranch.android.basketballscore

import android.app.Application

class BasketballScoreApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        GameRepository.initialize(this)
    }
}