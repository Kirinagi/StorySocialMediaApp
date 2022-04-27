package com.example.myapplication.view.story

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.util.Pair
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingstoryapp1.databinding.ItemNoteBinding
import com.example.myapplication.api.ListStory
import com.example.myapplication.view.DetailStoryActivity


class StoryAdapter :
    PagingDataAdapter<ListStory, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    private var stories: PagingData<ListStory>? = null

    fun setList(stories: PagingData<ListStory>?) {
        this.stories = stories
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemNoteBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val storyData = getItem(position)
        if (storyData == null) {
            Log.d("TAG", "onBindViewHolder: Data is empty")
        } else {
            holder.bind(storyData)
        }
    }


    class ViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStory) {

            itemView.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.storyImage, "storyImage"),
                        Pair(binding.username, "username"),
                        Pair(binding.description, "description")
                    )
                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY, story)
                startActivity(itemView.context, intent, optionsCompat.toBundle())
            }


            binding.apply {
                Glide.with(itemView)
                    .load(story.photoUrl)
                    .into(storyImage)
                username.text = story.name
                description.text = story.description
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }


            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}

