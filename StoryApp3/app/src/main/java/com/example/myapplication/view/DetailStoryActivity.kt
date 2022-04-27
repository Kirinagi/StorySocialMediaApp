package com.example.myapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.dicodingstoryapp1.databinding.ActivityDetailStoryBinding
import com.example.myapplication.api.ListStory

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailStory()
    }


    private fun detailStory() {
        val story = intent.getParcelableExtra<ListStory>(EXTRA_STORY) as ListStory
        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.photoDetail)
        binding.nameDetail.text = story.name
        binding.descDetail.text = story.description
    }


    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}