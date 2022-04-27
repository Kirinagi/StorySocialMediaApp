package com.example.myapplication.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import com.example.dicodingstoryapp1.R
import com.example.dicodingstoryapp1.databinding.ActivityRegisterBinding
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.ResponseRegister
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
@ExperimentalPagingApi
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        registration()
        playAnimation()
    }


    private fun playAnimation() {
        val title =
            ObjectAnimator.ofFloat(binding.registerTextView, View.ALPHA, 1f).setDuration(200)
        val name = ObjectAnimator.ofFloat(binding.registerName, View.ALPHA, 1f).setDuration(200)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.registerEmail, View.ALPHA, 1f).setDuration(200)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.registerPassword, View.ALPHA, 1f).setDuration(200)
        val login = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                title,
                name,
                emailEditTextLayout,
                passwordEditTextLayout,
                login,
            )
            startDelay = 500
        }.start()
    }


    private fun registration() {
        binding.registerName.type = "name"
        binding.registerEmail.type = "email"
        binding.registerPassword.type = "password"

        binding.btnRegister.setOnClickListener {
            val inputName = binding.registerName.text.toString()
            val inputEmail = binding.registerEmail.text.toString()
            val inputPassword = binding.registerPassword.text.toString()

            createAccount(inputName, inputEmail, inputPassword)
        }
    }


    private fun createAccount(inputName: String, inputEmail: String, inputPassword: String) {
        showLoading(true)

        val client = ApiConfig.getApiService().createAccount(inputName, inputEmail, inputPassword)
        client.enqueue(object : Callback<ResponseRegister> {
            override fun onResponse(
                call: Call<ResponseRegister>,
                response: Response<ResponseRegister>
            ) {
                showLoading(false)
                val responseBody = response.body()
                Log.d(TAG, "onResponse: $responseBody")
                if (response.isSuccessful && responseBody?.message == "User created") {
                    Toast.makeText(
                        this@RegisterActivity,
                        getString(R.string.register_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    Toast.makeText(
                        this@RegisterActivity,
                        getString(R.string.register_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseRegister>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
                Toast.makeText(
                    this@RegisterActivity,
                    getString(R.string.register_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    companion object {
        private const val TAG = "Register Activity"
    }

}