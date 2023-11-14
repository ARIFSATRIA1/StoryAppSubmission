package com.example.storyappsubmission.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.R
import com.example.storyappsubmission.data.ResultState
import com.example.storyappsubmission.databinding.ActivityRegisterBinding
import com.example.storyappsubmission.view.ViewModelFactory
import com.example.storyappsubmission.view.login.LoginActivity
import java.time.Duration

class RegisterActivity: AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.registerButton.setOnClickListener(this)

        playAnimation()
    }

    /*
        Function To Get Fetching Api From Register User
     */

    private fun registerUser(name: String,email: String,password: String) {
        viewModel.registerUsers(name, email, password).observe(this) {result ->
            if (result != null) {
                when (result) {
                    is ResultState.Sucsess -> {
                        showToast(result.data.message!!)
                        showLoading(true)
                        AlertDialog.Builder(this).apply {
                            setTitle(R.string.yeah)
                            setMessage(R.string.createdAcc)
                            setPositiveButton(R.string.lanjut) {_,_ ->
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                    is ResultState.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.registerButton) {
            val name = binding.edRegisterName.text.toString().trim()
            val email = binding.edRegisterEmail.text.toString().trim()
            val password = binding.edRegisterPassword.text.toString().trim()

            if (name.isEmpty()) {
                binding.edRegisterName.error
                return
            }

            if (email.isEmpty()) {
                binding.edRegisterEmail.error
                return
            }

            if (!isValidEmail(email)) {
                binding.edRegisterEmail.error
                return
            }

            if (password.isEmpty()) {
                binding.edRegisterPassword.error
                return
            }

            registerUser(name, email, password)
        }
    }


    /*
        Function To InputEmail is Valid Or Not
     */
    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /*
        Private fun Toast Text
     */
    private fun showToast(message: String) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    /*
        Function To Play Animation
     */

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ImageView, View.TRANSLATION_X,-30f,30f).apply {
            duration = 6000
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }.start()

        val titleView = ObjectAnimator.ofFloat(binding.titleTextView,View.ALPHA, 1f).setDuration(100)
        val nameText = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(100)
        val emailText = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA,1f).setDuration(100)
        val passwordText = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA,1f).setDuration(100)
        val emailEdit = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA,1f).setDuration(100)
        val passwordEdit = ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA,1f).setDuration(100)
        val namaEdit = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA,1f).setDuration(100)
        val btnRegister = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA,1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(titleView,nameText,emailText,passwordText,emailEdit,passwordEdit,namaEdit,btnRegister)
            start()
        }
    }


    /*
        Function To Show Loading
     */
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}