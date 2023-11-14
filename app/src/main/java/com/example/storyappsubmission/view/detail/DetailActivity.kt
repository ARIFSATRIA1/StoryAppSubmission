package com.example.storyappsubmission.view.detail

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.storyappsubmission.data.enitity.StoryEntity
import com.example.storyappsubmission.data.remote.response.Story
import com.example.storyappsubmission.databinding.ActivityDetailBinding
import com.example.storyappsubmission.view.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding



    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val stories = intent.getParcelableExtra<StoryEntity>(EXTRA_ID)



        viewModel.loading.observe(this) {
            showLoading(it)
        }


        detailStories(stories!!)
    }

    /*
        function to Show Detail Stories
     */
    private fun detailStories(storyEntity: StoryEntity) {
            if (storyEntity != null) {
                binding.titleTextView.text = storyEntity.name
                binding.descTextView.text = storyEntity.description
                Glide.with(binding.ImageView)
                    .load(storyEntity.photoUrl)
                    .into(binding.ImageView)
            }
    }

    /*
        Function to Show Loading Proggres Bar
     */
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_ID = "id"
    }
}