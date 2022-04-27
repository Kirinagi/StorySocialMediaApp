package com.example.myapplication.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.example.dicodingstoryapp1.R
import com.example.dicodingstoryapp1.databinding.ActivityMainBinding
import com.example.myapplication.SharedViewModel
import com.example.myapplication.UserPreference
import com.example.myapplication.ViewModelFactory
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.data.MainActStories
import com.example.myapplication.data.ResponseLogin
import com.example.myapplication.emailFormat
import com.example.myapplication.view.story.StoryActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: SharedViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showLoading(false)
        setupViewModel()
        creds()
        enableButton()
        editTextListener()
        playAnimation()
    }


    private fun creds() {
        binding.emailEditText.type = "email"
        binding.passwordEditText.type = "password"

        binding.btnLogin.setOnClickListener {
            val inputEmail = binding.emailEditText.text.toString()
            val inputPassword = binding.passwordEditText.text.toString()

            login(inputEmail, inputPassword)
        }

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[SharedViewModel::class.java]

        mainViewModel.getUser().observe(this) {
            if (it.isLogin) {
                val intent = Intent(this, StoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    private fun login(inputEmail: String, inputPassword: String) {

        showLoading(true)

        val client = ApiConfig.getApiService().login(inputEmail, inputPassword)

        client.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                showLoading(false)
                val responseBody = response.body()
                Log.d("TAG", "onResponse: $responseBody")

                if (response.isSuccessful && responseBody?.message == "success") {
                    mainViewModel.saveUser(MainActStories(responseBody.loginResult.token, true))
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.login_success),
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Log.e("TAG", "onFailure: ${response.message()}")
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.login_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                showLoading(false)
                Log.e("TAG", "onFailure: ${t.message}")
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.login_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun editTextListener() {
        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                enableButton()
            }

            override fun afterTextChanged(s: Editable) {

            }

        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enableButton()
            }

            override fun afterTextChanged(s: Editable?) {

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


    private fun enableButton() {
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text

        binding.btnLogin.isEnabled =
            password != null && email != null && binding.passwordEditText.text.toString().length >= 6 && emailFormat(
                binding.emailEditText.text.toString()
            )
    }


    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.loginTextView, View.ALPHA, 1f).setDuration(200)
        val subtitle =
            ObjectAnimator.ofFloat(binding.subtitle, View.ALPHA, 1f).setDuration(200)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(200)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(200)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(200)
        val register = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(
                title,
                subtitle,
                emailEditTextLayout,
                passwordEditTextLayout,
                login,
                register
            )
            startDelay = 500
        }.start()
    }
}