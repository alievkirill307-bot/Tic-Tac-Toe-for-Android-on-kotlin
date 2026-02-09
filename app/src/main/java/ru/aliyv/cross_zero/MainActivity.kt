package ru.aliyv.cross_zero

import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.aliyv.cross_zero.databinding.ActivityMainBinding

const val EXTRA_TIME = "my.tick_tac_toe.TIME"
const val EXTRA_GAME_FIELD = "my.tick_tac_toe.GAME_FIELD"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MyTickTacToe)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textSize.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        binding.toContinueGame.setOnClickListener {
            val gameInfo = getInfoAboutLastGame()
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra(EXTRA_TIME, gameInfo.time)
                putExtra(EXTRA_GAME_FIELD, gameInfo.gameField)
            }
            startActivity(intent)
        }

        binding.toSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getInfoAboutLastGame(): GameInfo {
        return with(getSharedPreferences("game", MODE_PRIVATE)) {
            val time = getLong("time", 0)
            val gameField = getString("gameField", "")
            if (gameField != null) {
                GameInfo(time, gameField)
            } else {
                GameInfo(0, "")
            }
        }
    }
}

data class GameInfo(val time: Long, val gameField: String)