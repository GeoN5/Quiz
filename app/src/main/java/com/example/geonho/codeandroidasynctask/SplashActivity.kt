package com.example.geonho.codeandroidasynctask

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.geonho.codeandroidasynctask.util.saveData
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.saveData("count",0)
        confetti.playAnimation()

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        },2000)


    }

}


