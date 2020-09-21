package com.bignerdranch.android.basketballscore

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

private const val TAG = "GameListActivity"

class GameListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_list)

        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_list)
        if (currentFragment == null) {
            val fragment = GameListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_list, fragment)
                .commit()
        }
    }


    // Use companion object to create Intents and access functions without class instances
    companion object {

        fun newIntent(
            packageContext: Context): Intent {
            Log.d(TAG, "New intent in game")
            return Intent(packageContext, GameListActivity::class.java)
        }

    }

}
