package com.example.myapplication.view.story

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingstoryapp1.R
import com.example.dicodingstoryapp1.databinding.ActivityStoryBinding
import com.example.myapplication.UserPreference
import com.example.myapplication.ViewModelFactory
import com.example.myapplication.data.PagingStories
import com.example.myapplication.view.AddStoryActivity
import com.example.myapplication.view.MainActivity
import com.example.myapplication.view.MapsActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@ExperimentalPagingApi
class StoryActivity : AppCompatActivity() {

    private lateinit var storyViewModel: StoryViewModel
    private lateinit var binding: ActivityStoryBinding
    private lateinit var adapter: StoryAdapter
    private lateinit var pagingStories: PagingStories


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        layoutManager()
        setupCameraMenu()
        showLoading(true)
    }


    private fun setupViewModel() {
        storyViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[StoryViewModel::class.java]

        storyViewModel.getUser().observe(this) { story ->
            this.pagingStories = story
            if (story.isLogin) {
                showLoading(false)
                storyViewModel.pagingStory(story.token).observe(this) {
                    adapter.setList(it)
                    adapter.submitData(lifecycle, it)
                }
            }
        }
    }


    private fun layoutManager() {
        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStories.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStories.layoutManager = LinearLayoutManager(this)
        }
        adapter = StoryAdapter()
        binding.rvStories.adapter = adapter.withLoadStateFooter(footer = LoadingStateAdapter {
            adapter.retry()
        })
    }


    private fun setupCameraMenu() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@StoryActivity, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                storyViewModel.logout()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.story_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


    companion object {
        private const val TAG = "Story Activity"
    }
}