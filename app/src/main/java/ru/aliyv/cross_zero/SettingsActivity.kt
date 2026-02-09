package ru.aliyv.cross_zero

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import ru.aliyv.cross_zero.databinding.ActivitySettingsBinding

const val PREF_SOUND = "my.tick_tac_toe.SOUND"
const val PREF_LEVEL = "my.tick_tac_toe.LEVEL"
const val PREF_RULES = "my.tick_tac_toe.RULES"

class SettingsActivity : AppCompatActivity() {

    private lateinit var settingsBinding: ActivitySettingsBinding
    private var currentLevel: Int = 0
    private var currentVolumeSound: Int = 0
    private var currentRules: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsBinding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(settingsBinding.root)

        val currentSettings = getCurrentSettings()

        currentLevel = currentSettings.level
        currentVolumeSound = currentSettings.sound
        currentRules = currentSettings.rules

        if (currentLevel == 0) {
            settingsBinding.prevLvl.visibility = View.INVISIBLE
        } else if (currentLevel == 2) {
            settingsBinding.nextLvlBtn.visibility = View.INVISIBLE
        }

        settingsBinding.infoLevel.text = resources.getStringArray(R.array.level)[currentLevel]
        settingsBinding.soundBar.progress = currentVolumeSound

        when (currentSettings.rules) {
            1 -> settingsBinding.checkboxHorizontal.isChecked = true
            2 -> settingsBinding.checkboxVertical.isChecked = true
            3 -> {
                settingsBinding.checkboxHorizontal.isChecked = true
                settingsBinding.checkboxVertical.isChecked = true
            }
            4 -> settingsBinding.checkboxDiagonal.isChecked = true
            5 -> {
                settingsBinding.checkboxDiagonal.isChecked = true
                settingsBinding.checkboxHorizontal.isChecked = true
            }
            6 -> {
                settingsBinding.checkboxDiagonal.isChecked = true
                settingsBinding.checkboxVertical.isChecked = true
            }
            7 -> {
                settingsBinding.checkboxHorizontal.isChecked = true
                settingsBinding.checkboxVertical.isChecked = true
                settingsBinding.checkboxDiagonal.isChecked = true
            }
        }

        settingsBinding.prevLvl.setOnClickListener {
            currentLevel--
            if (currentLevel == 0) {
                settingsBinding.prevLvl.visibility = View.INVISIBLE
            } else if (currentLevel == 1) {
                settingsBinding.nextLvlBtn.visibility = View.VISIBLE
            }
            updateLevel(currentLevel)
            settingsBinding.infoLevel.text = resources.getStringArray(R.array.level)[currentLevel]
        }

        settingsBinding.nextLvlBtn.setOnClickListener {
            currentLevel++
            if (currentLevel == 2) {
                settingsBinding.nextLvlBtn.visibility = View.INVISIBLE
            } else if (currentLevel == 1) {
                settingsBinding.prevLvl.visibility = View.VISIBLE
            }
            updateLevel(currentLevel)
            settingsBinding.infoLevel.text = resources.getStringArray(R.array.level)[currentLevel]
        }

        settingsBinding.soundBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                currentVolumeSound = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                updateVolumeSound(currentVolumeSound)
            }
        })

        settingsBinding.checkboxHorizontal.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                currentRules += 1
            } else {
                currentRules -= 1
            }
            updateRules(currentRules)
        }

        settingsBinding.checkboxVertical.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                currentRules += 2
            } else {
                currentRules -= 2
            }
            updateRules(currentRules)
        }

        settingsBinding.checkboxDiagonal.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                currentRules += 4
            } else {
                currentRules -= 4
            }
            updateRules(currentRules)
        }

        settingsBinding.toback.setOnClickListener {
            setResult(RESULT_OK)
            onBackPressed()
        }
    }

    private fun updateVolumeSound(volume: Int) {
        getSharedPreferences("game", MODE_PRIVATE).edit().apply {
            putInt(PREF_SOUND, volume)
            apply()
        }
        setResult(RESULT_OK)
    }

    private fun updateLevel(level: Int) {
        getSharedPreferences("game", MODE_PRIVATE).edit().apply {
            putInt(PREF_LEVEL, level)
            apply()
        }
        setResult(RESULT_OK)
    }

    private fun updateRules(rules: Int) {
        val validRules = rules.coerceIn(0, 7)
        getSharedPreferences("game", MODE_PRIVATE).edit().apply {
            putInt(PREF_RULES, validRules)
            apply()
        }
        setResult(RESULT_OK)
    }

    private fun getCurrentSettings(): SettingsInfo {
        return getSharedPreferences("game", MODE_PRIVATE).run {
            val sound = getInt(PREF_SOUND, 100)
            val level = getInt(PREF_LEVEL, 1)
            val rules = getInt(PREF_RULES, 7)
            SettingsInfo(sound, level, rules)
        }
    }
}

data class SettingsInfo(val sound: Int, val level: Int, val rules: Int)