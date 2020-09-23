package com.bignerdranch.android.basketballscore

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.list_item_game.*
import kotlinx.coroutines.launch
import java.util.*

private const val EXTRA_TEAM_A_NAME = "com.bignerdranch.android.basketballscore.team_a_name"
private const val EXTRA_TEAM_B_NAME = "com.bignerdranch.android.basketballscore.team_b_name"
private const val EXTRA_TEAM_A_SCORE = "com.bignerdranch.android.basketballscore.team_a_score"
private const val EXTRA_TEAM_B_SCORE = "com.bignerdranch.android.basketballscore.team_b_score"
private const val EXTRA_ID = "com.bignerdranch.android.basketballscore.id"
private const val EXTRA_DATE = "com.bignerdranch.android.basketballscore.date"
const val EXTRA_IS_RESULT_SAVED = "com.bignerdranch.android.basketballscore.isShown"
private const val TAG = "SaveScoreActivity"

class SaveScoreActivity : AppCompatActivity() {

    private var id: String? = null
    private var date: Int? = null
    private var team_a_name = ""
    private var team_b_name = ""
    private var team_a_score = 0
    private var team_b_score = 0

    private lateinit var saveBtn: Button
    private lateinit var backBtn: Button
    private lateinit var teamANameText: TextView
    private lateinit var teamBNameText: TextView
    private lateinit var teamAScoreText: TextView
    private lateinit var teamBScoreText: TextView

    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProviders.of(this).get(GameListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_score)

        Log.d(TAG, "onCreate() in SaveScoreActivity called")

        // Get data from extras
        id = intent.getStringExtra(EXTRA_ID)
        date = intent.getIntExtra(EXTRA_DATE, 0)
        team_a_name = intent.getStringExtra(EXTRA_TEAM_A_NAME) as String
        team_b_name = intent.getStringExtra(EXTRA_TEAM_B_NAME) as String
        team_a_score = intent.getIntExtra(EXTRA_TEAM_A_SCORE, 0)
        team_b_score = intent.getIntExtra(EXTRA_TEAM_B_SCORE, 0)

        // Find views
        saveBtn = findViewById(R.id.confirm_save)
        backBtn = findViewById(R.id.back)
        teamANameText = findViewById(R.id.team_a_name_id)
        teamBNameText = findViewById(R.id.team_b_name_id)
        teamAScoreText = findViewById(R.id.score_a)
        teamBScoreText = findViewById(R.id.score_b)

        // Set views to extra data
        teamANameText.setText(team_a_name)
        teamBNameText.setText(team_b_name)
        teamAScoreText.setText(team_a_score.toString())
        teamBScoreText.setText(team_b_score.toString())

        // Button listeners
        saveBtn.setOnClickListener {
            // Create intent
            setIsSaved(true)

            if (id == null || date == null) {
                // Create id
                val poss = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                var id = ""
                for (i in 0..10) {
                    id += poss[Math.floor(Math.random() * poss.length).toInt()]
                }

                date = Date().time.toInt()

                // Create game and insert into DB
                var game = Game(id, team_a_name, team_b_name, team_a_score, team_b_score, date as Int)
                lifecycleScope.launch {
                    gameListViewModel.insert(game)
                }
            }
            finish()
        }
        backBtn.setOnClickListener {
            setIsSaved(false)
            finish()
        }

    }

    //  Build intent and ask ActivityManager to send
    private fun setIsSaved(saved: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_IS_RESULT_SAVED, saved)
        }
        setResult(Activity.RESULT_OK, data)
    }

    // Use companion object to create Intents and access functions without class instances
    companion object {

        fun newIntent(

            packageContext: Context,
            id: String?,
            date: Int?,
            team_a_name: String,
            team_b_name: String,
            team_a_score: Int,
            team_b_score: Int): Intent {
            Log.d(TAG, "New intent in save")

            return Intent(packageContext, SaveScoreActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
                putExtra(EXTRA_DATE, date)
                putExtra(EXTRA_TEAM_A_NAME, team_a_name)
                putExtra(EXTRA_TEAM_B_NAME, team_b_name)
                putExtra(EXTRA_TEAM_A_SCORE, team_a_score)
                putExtra(EXTRA_TEAM_B_SCORE, team_b_score)
            }
        }

    }
}
