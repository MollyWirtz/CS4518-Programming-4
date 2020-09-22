package com.bignerdranch.android.basketballscore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders


private const val TEAM_A = "teamA"
private const val TEAM_B = "teamB"
private const val REQUEST_CODE_SAVE = 0
private const val TAG = "MainFragment"
private const val ARG_GAME_INDEX = "game.index"



class MainFragment: Fragment() {

    // Use Callbacks to communicate with GameListFragment
    interface Callbacks {
        fun onDisplaySelected(winner: String)
    }
    private var callbacks: Callbacks? = null

    private var gameIndex: String? = ""

    private lateinit var threePointBtnA: Button
    private lateinit var threePointBtnB: Button
    private lateinit var twoPointBtnA: Button
    private lateinit var twoPointBtnB: Button
    private lateinit var freeThrowBtnA: Button
    private lateinit var freeThrowBtnB: Button
    private lateinit var resetBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var displayBtn: Button
    private lateinit var scoreA: TextView
    private lateinit var scoreB: TextView
    private lateinit var teamAName: TextView
    private lateinit var teamBName: TextView
    private var isSaved = false

    // Create a ScoreViewModel
    private val scoreViewModel: ScoreViewModel by lazy {
        ViewModelProviders.of(this).get(ScoreViewModel::class.java)
    }

    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProviders.of(this).get(GameListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameIndex = arguments?.getSerializable(ARG_GAME_INDEX) as String?
        Log.d(TAG, "args bundle game index: $gameIndex")

        // Initialize the singleton GameListViewModel
        //GameListViewModel.initialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        // Retrieve saved instance state
        val teamA = savedInstanceState?.getInt(TEAM_A, 0) ?: 0
        val teamB = savedInstanceState?.getInt(TEAM_B, 0) ?: 0
        scoreViewModel.teamA.score = teamA
        scoreViewModel.teamB.score = teamB

        // Assign variables to views
        threePointBtnA = view.findViewById(R.id.three_points_a)
        threePointBtnB = view.findViewById(R.id.three_points_b)
        twoPointBtnA = view.findViewById(R.id.two_points_a)
        twoPointBtnB = view.findViewById(R.id.two_points_b)
        freeThrowBtnA = view.findViewById(R.id.free_throw_a)
        freeThrowBtnB = view.findViewById(R.id.free_throw_b)
        resetBtn = view.findViewById(R.id.reset)
        saveBtn = view.findViewById(R.id.save)
        displayBtn = view.findViewById(R.id.display)
        scoreA = view.findViewById(R.id.score_a)
        scoreB = view.findViewById(R.id.score_b)
        teamAName = view.findViewById(R.id.team_a_name_id)
        teamBName = view.findViewById(R.id.team_b_name_id)

        // Set initial score
        scoreA.setText(scoreViewModel.teamA.score.toString())
        scoreB.setText(scoreViewModel.teamB.score.toString())

        // Set button listeners
        threePointBtnA.setOnClickListener {
            scoreViewModel.addScore(3, 'A')
            scoreA.setText(scoreViewModel.teamA.score.toString())
        }
        threePointBtnB.setOnClickListener {
            scoreViewModel.addScore(3, 'B')
            scoreB.setText(scoreViewModel.teamB.score.toString())
        }
        twoPointBtnA.setOnClickListener {
            scoreViewModel.addScore(2, 'A')
            scoreA.setText(scoreViewModel.teamA.score.toString())
        }
        twoPointBtnB.setOnClickListener {
            scoreViewModel.addScore(2, 'B')
            scoreB.setText(scoreViewModel.teamB.score.toString())
        }
        freeThrowBtnA.setOnClickListener {
            scoreViewModel.addScore(2, 'A')
            scoreA.setText(scoreViewModel.teamA.score.toString())
        }
        freeThrowBtnB.setOnClickListener {
            scoreViewModel.addScore(2, 'B')
            scoreB.setText(scoreViewModel.teamB.score.toString())
        }
        resetBtn.setOnClickListener {
            resetMain()
        }
        saveBtn.setOnClickListener{
            // Get data for extras
            val team_a_name = teamAName.getText().toString()
            val team_b_name = teamBName.getText().toString()
            val team_a_score = scoreViewModel.teamA.score
            val team_b_score = scoreViewModel.teamB.score

            // Create intent
            val intent = SaveScoreActivity.newIntent(
                getActivity() as Activity,
                team_a_name,
                team_b_name,
                team_a_score,
                team_b_score)

            // Start SaveScoreActivity
            startActivityForResult(intent, REQUEST_CODE_SAVE)
        }
        displayBtn.setOnClickListener{
            // Determine winner
            if (scoreViewModel.teamA.score > scoreViewModel.teamB.score){
                callbacks?.onDisplaySelected("A")
            } else {
                callbacks?.onDisplaySelected("B")
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameListViewModel.gameListLiveData.observe(
            viewLifecycleOwner, Observer { games ->
                games.let {
                    Log.i(TAG, "Got games ${games.size}")
                    showClickedGame(games)
                }
            })
    }


    override fun onDetach() {
        super.onDetach()
        gameIndex = null
        callbacks = null
    }

    companion object {
        fun newInstance(gameIndex: String): MainFragment {
            val args = Bundle().apply {
                putSerializable(ARG_GAME_INDEX, gameIndex)
            }
            return MainFragment().apply { arguments = args }
        }
    }

    fun showClickedGame(games: List<Game>) {
        //Find correct game information and set in scoreViewModel for display
        for (game in games) {
            if (game.id == gameIndex) {
                teamAName.setText(game.teamAName)
                teamBName.setText(game.teamBName)
                scoreViewModel.addScore(game.teamAScore, 'A')
                scoreViewModel.addScore(game.teamBScore, 'B')
                scoreA.setText(scoreViewModel.teamA.score.toString())
                scoreB.setText(scoreViewModel.teamB.score.toString())
                break
            }
        }
    }

    // Reset text fields on button click, saved data
    fun resetMain() {
        scoreViewModel.addScore(0, 'A')
        scoreViewModel.addScore(0, 'B')
        scoreA.setText(scoreViewModel.teamA.score.toString())
        scoreB.setText(scoreViewModel.teamB.score.toString())
        teamAName.setText(R.string.team_a_header)
        teamBName.setText(R.string.team_b_header)
    }

    // Handle result of child intent response
    override fun onActivityResult (requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Return if bad code
        if (resultCode != Activity.RESULT_OK) { return }

        // Get reponse extra
        if (requestCode == REQUEST_CODE_SAVE) {
            isSaved = data?.getBooleanExtra(EXTRA_IS_RESULT_SAVED, false) ?: false
        }

        // Toast as appropriate
        if (isSaved) {
            resetMain()
            Toast.makeText(getActivity() as Activity, "Saved sucessfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(getActivity() as Activity, "Data not saved", Toast.LENGTH_SHORT).show()
        }
    }

    // Override onSaveInstanceState to save scores for each team
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(TEAM_A, scoreViewModel.teamA.score)
        savedInstanceState.putInt(TEAM_B, scoreViewModel.teamB.score)
    }
}