package com.example.storyappsubmission.view.login


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.storyappsubmission.R
import com.example.storyappsubmission.data.ResultState
import com.example.storyappsubmission.data.preferences.TokenModel
import com.example.storyappsubmission.databinding.ActivityLoginBinding
import com.example.storyappsubmission.view.ViewModelFactory
import com.example.storyappsubmission.view.main.MainActivity

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding


    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener(this)

        playAnimation()
    }

    /*
        Function to get Login Result From Api
     */

    private fun loginUser(email: String, password: String) {
        viewModel.loginUser(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Sucsess -> {
                        showToast(result.data.message!!)
                        intent.putExtra(
                            MainActivity.EXTRA_TOKEN,
                            result.data.loginResult?.token.toString()
                        )
                        viewModel.saveToken(TokenModel(result.data.loginResult?.token.toString()))
                        showLoading(false)
                        AlertDialog.Builder(this).apply {
                            setTitle("Yeah!")
                            setMessage("Anda Berhasil Login")
                            setPositiveButton("Lanjut") { _, _ ->
                                viewModel.getToken().observe(this@LoginActivity) { user ->
                                    if (user.isLogin) {
                                        val intent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                }
                            }
                                .create()
                                .show()
                        }

                    }

                    is ResultState.Error -> {
                        showLoading(true)
                        showToast(result.error)
                    }
                }
            }
        }
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.loginButton) {
            val email = binding.edLoginEmail.text.toString().trim()
            val password = binding.edLoginPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.edLoginEmail.error = FIELD_REQUIRED
                return
            }

            if (!isValidEmail(email)) {
                binding.edLoginEmail.error = FIELD_IS_INVALID
                return
            }

            if (password.isEmpty()) {
                binding.edLoginPassword.error = FIELD_REQUIRED
                return
            }
            loginUser(email, password)
        }
    }

    /*
        Function To Play Animation
     */

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ImageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val email = ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(100)
        val password =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(100)
        val emailText =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val passwordText =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val loginBtn = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val messageText =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val titleText =
            ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                email,
                password,
                emailText,
                passwordText,
                loginBtn,
                messageText,
                titleText
            )
            start()
        }
    }

    // Function To Show Toast
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /*
        Check Input Email IsValid or Not
     */

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /*
        Function To ShowLoading Bar
     */
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    companion object {
        const val FIELD_REQUIRED = "Harus Diisi"
        const val FIELD_IS_INVALID = "Email Tidak Valid"
    }
}