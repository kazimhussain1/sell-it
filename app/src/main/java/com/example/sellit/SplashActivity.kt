package com.example.sellit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.DecelerateInterpolator
import androidx.databinding.DataBindingUtil
import com.example.sellit.databinding.ActivitySplashBinding
import com.example.sellit.home.view.HomeActivity


class SplashActivity : AppCompatActivity() {

    private var binding: ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        binding?.imageView!!.apply {
            scaleX = 0f
            scaleY = 0f
        }
        binding?.imageView!!.animate().apply {
            interpolator = DecelerateInterpolator()
            duration = 1000
            scaleXBy(1f)
            scaleYBy(1f)
            start()
        }


        Handler(Looper.getMainLooper()).postDelayed({

            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
            overridePendingTransition(0, R.anim.explode)
            finish()


        }, 2000)
    }
}