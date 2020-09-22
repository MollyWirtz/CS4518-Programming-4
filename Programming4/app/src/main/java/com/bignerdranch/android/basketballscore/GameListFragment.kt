package com.bignerdranch.android.basketballscore

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "GameListFragment"
private const val ARG_GAME_WINNER = "game.winner"

class GameListFragment: Fragment() {

    // Implement Callbacks for communication with MainFragment
    interface Callbacks {
        fun onGameSelected(gameIndex: String)
    }
    private var callbacks: Callbacks? = null

    private lateinit var gameRecyclerView: RecyclerView
    private var adapter: GameAdapter? = null
    private var winner: String = ""

    private val gameListViewModel: GameListViewModel by lazy {
        ViewModelProviders.of(this).get(GameListViewModel::class.java)
    }

    // Callback methods
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }
    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get fragment argument from MainFragment
        winner = arguments?.getSerializable(ARG_GAME_WINNER) as String
        Log.d(TAG, "args bundle winner: $winner")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView in GameListFragment")

        val view = inflater.inflate(R.layout.fragment_game_list, container, false)
        gameRecyclerView = view.findViewById(R.id.game_recycler_view) as RecyclerView
        gameRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()
        return view
    }

    private fun updateUI() {
        val tempGames = GameListViewModel.games
        val games = mutableListOf<Game>()

        // Filter games by current winner in MainFragment
        for (t in tempGames) {
            if (winner == "A" && t.winner == "A") {
                games += t
            } else if (winner == "B" && t.winner == "B") {
                games += t
            }
        }
        Log.d(TAG,"${games.size} games have been added for winner ${winner}")

        // Set games in recycler view
        adapter = GameAdapter(games)
        gameRecyclerView.adapter = adapter
    }

    private inner class GameHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var game : Game
        private val indexTextView: TextView = itemView.findViewById(R.id.game_index)
        private val teamsTextView: TextView = itemView.findViewById(R.id.game_teams)
        private val dateTextView: TextView = itemView.findViewById(R.id.game_date)
        private val scoreTextView: TextView = itemView.findViewById(R.id.game_score)
        private val teamImage: ImageView = itemView.findViewById(R.id.team_img)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(game: Game) {
            this.game = game
            indexTextView.text = game.index
            dateTextView.text = game.date
            teamsTextView.text = game.teamA + " vs " + game.teamB
            scoreTextView.text = game.scoreA + " : " + game.scoreB

            // Set team image based on winning team
            if (game.winner == "A") {
                teamImage.setImageResource(R.drawable.wpi)
            } else if (game.winner == "B"){
                teamImage.setImageResource(R.drawable.huskylogo)
            } else {
                teamImage.setImageResource(R.drawable.mit)
            }
        }

        override fun onClick(v: View?) {
            Log.d(TAG, "clicked index  = ${game.index}")
            callbacks?.onGameSelected(game.index)
        }

    }


    private inner class GameAdapter(var games: List<Game>): RecyclerView.Adapter<GameHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameHolder {
            val view = layoutInflater.inflate(R.layout.list_item_game, parent, false)
            return GameHolder(view)
        }

        override fun getItemCount() = games.size

        override fun onBindViewHolder(holder: GameHolder, position: Int) {
            val game = games[position]
            holder.bind(game)

        }

    }

    companion object {
        fun newInstance(winner: String): GameListFragment {
            val args = Bundle().apply {
                putSerializable(ARG_GAME_WINNER, winner)
            }
            return GameListFragment().apply {
                arguments = args
            }
        }
    }
}