package com.example.storyappsubmission.view.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.databinding.ActivityWelcomeBinding
import com.example.storyappsubmission.view.login.LoginActivity
import com.example.storyappsubmission.view.main.MainActivity
import com.example.storyappsubmission.view.register.RegisterActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        playAnimation()

        binding.buttonLogin.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    /*
        Function To Play Animation
     */
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ImageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }.start()

        val titleView = ObjectAnimator.ofFloat(binding.titleTextView,View.ALPHA,1f).setDuration(100)
        val descView = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(100)
        val loginBtn = ObjectAnimator.ofFloat(binding.buttonLogin, View.ALPHA,1f).setDuration(100)
        val registerBtn = ObjectAnimator.ofFloat(binding.buttonRegister, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(loginBtn,registerBtn)
        }

        AnimatorSet().apply {
            playSequentially(titleView,descView,together)
            start()
        }
    }



}