package com.example.geonho.codeandroidasynctask

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val pastScore = preferences.getInt("pastScore", 0)
        val text = "Past Score : $pastScore"
        scoreText.text = text
        ratingBar.rating = pastScore.toFloat()
        startButton.setOnClickListener {
            val i = Intent(this@MainActivity, QuestionActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}