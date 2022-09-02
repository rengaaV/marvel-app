package com.vagner.marvel.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vagner.marvel.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupSplash()
        supportActionBar?.hide()
    }

    private fun setupSplash() {
        binding.tvSplash.apply {
            alpha = 0f
            animate().setDuration(1000).alpha(1f).withEndAction {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
        }
    }
}