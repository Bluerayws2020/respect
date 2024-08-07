package com.blueray.respect.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blueray.respect_new.activities.MainActivity
import com.blueray.respect_new.databinding.ActivitySplashScreenBinding
import com.blueray.respect_new.helpers.HelperUtils

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("POXCXAA" , HelperUtils.getUID(this).toString())
        Handler(Looper.getMainLooper()).postDelayed({
            if (HelperUtils.getUID(this) == "0" || HelperUtils.getUserName(this)
                    .isNullOrEmpty()
            ) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 2000)
    }
}