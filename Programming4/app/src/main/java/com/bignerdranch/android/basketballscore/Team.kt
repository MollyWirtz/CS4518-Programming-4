package com.bignerdranch.android.basketballscore

import androidx.annotation.StringRes

open class Team(open val name: String, open val score: Int){}


class TeamA (override var name: String, override var score: Int): Team(name = name, score = score)

class TeamB (override var name: String, override var score: Int): Team(name = name, score = score)