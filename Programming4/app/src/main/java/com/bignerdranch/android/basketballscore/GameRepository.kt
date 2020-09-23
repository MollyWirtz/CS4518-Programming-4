package com.bignerdranch.android.basketballscore

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.basketballscore.database.GameDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


private const val DATABASE_NAME = "game-database"

class GameRepository private constructor(context: Context) {

    // Create a database callback to initialize database with dummy data
    var rdc: RoomDatabase.Callback = object : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            // Write 150 games to DB
            for (j in 0..150) {

                // Create id
                val poss = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                var id = ""
                for (i in 0..10) {
                    id += poss[Math.floor(Math.random() * poss.length).toInt()]
                }

                // Create names
                val possName = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                var nameA = ""
                var nameB = ""
                for (i in 0..5) {
                    nameA += possName[Math.floor(Math.random() * possName.length).toInt()]
                    nameB += possName[Math.floor(Math.random() * possName.length).toInt()]
                }

                // Create scores
                var scoreA = (1..100).shuffled().first()
                var scoreB = (1..100).shuffled().first()

                // Create date
                var date = Date().time.toInt()


                // Create game and insert into DB
                var game = Game(id, nameA, nameB, scoreA, scoreB, date)
                GlobalScope.launch {
                    insert(game)
                }
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
//            GlobalScope.launch {
//                deleteTableContents()
//            }
        }
    }

    private val database: GameDatabase = Room.databaseBuilder(
        context.applicationContext,
        GameDatabase::class.java,
        DATABASE_NAME
    ).addCallback(rdc)
        .build()


    private val gameDao = database.gameDao()

    fun getGames(): LiveData<List<Game>> = gameDao.getGames()

    fun getGame(id: String): LiveData<Game> = gameDao.getGame(id)

    suspend fun insert(game: Game) = gameDao.insert(game)

    suspend fun update(game: Game) = gameDao.update(game)

    suspend fun deleteTableContents() = gameDao.deleteTableContents()


    companion object {
        private var INSTANCE: GameRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = GameRepository(context)
            }
        }

        fun get(): GameRepository {
            return INSTANCE ?: throw IllegalStateException("GameRepository must be initialized")
        }
    }


}