package com.example.storyappsubmission.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.storyappsubmission.data.enitity.StoryEntity
import com.example.storyappsubmission.data.remote.response.ListStoryItem
import com.example.storyappsubmission.databinding.StoryRowBinding
import com.example.storyappsubmission.view.detail.DetailActivity

class StoryAdapter:
    PagingDataAdapter<StoryEntity, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {


    class MyViewHolder(private val binding: StoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("checkResult")
        fun bind(context: Context,user: StoryEntity) {
            binding.titleTextView.text = user.name
            binding.descTextView.text = user.description
            Glide.with(binding.ImageView)
                .load(user.photoUrl)
                .into(binding.ImageView)

            binding.root.setOnClickListener {
                Intent(context,DetailActivity::class.java).also { intent ->
                    intent.putExtra(DetailActivity.EXTRA_ID,user)
                    context.startActivity(intent)
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = StoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val list = getItem(position)
        if (list != null) {
            holder.bind(holder.itemView.context,list)
        }

    }


    companion object {

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }


        }

    }

}