package com.example.geonho.codeandroidasynctask

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val FINSH_INTERVAL_TIME = 2000
    private var backPressedTime:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        setListeners()
    }


    private fun init(){
        var result = this.getData("count")
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val pastScore = preferences.getInt("pastScore", 0)
        if(pastScore == 5 && result ==1){
            trophy.playAnimation()
        }
        val text = "Past Score : $pastScore"
        scoreText.text = text
        ratingBar.rating = pastScore.toFloat()
    }

    private fun setListeners(){
        startButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, QuestionActivity::class.java))
        }
    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime
        if (intervalTime in 0..FINSH_INTERVAL_TIME) {
            ActivityCompat.finishAffinity(this)
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
    }

}