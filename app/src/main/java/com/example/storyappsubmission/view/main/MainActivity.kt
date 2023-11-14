package com.example.storyappsubmission.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.storyappsubmission.R
import com.example.storyappsubmission.data.adapter.LoadingStateAdapter
import com.example.storyappsubmission.data.remote.response.ListStoryItem
import com.example.storyappsubmission.databinding.ActivityMainBinding
import com.example.storyappsubmission.view.ViewModelFactory
import com.example.storyappsubmission.view.detail.DetailActivity
import com.example.storyappsubmission.view.maps.MapsActivity
import com.example.storyappsubmission.view.story.StoryActivity
import com.example.storyappsubmission.view.welcome.WelcomeActivity
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_TOKEN = "TOKEN"
    }

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar

        isSession()

        setStories()

        viewModel.loading.observe(this@MainActivity) {
            showLoading(it)
        }

        binding.fabButton.setOnClickListener(this)

        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding.recylerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recylerView.addItemDecoration(itemDecoration)

    }

    /*
        Function To SetList Stories To RecylerView
     */
    private fun setStories() {
        val adapter = StoryAdapter()
        binding.recylerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        viewModel.getToken().observe(this) {user ->
            viewModel.getAllStories("Bearer ${user.token}").observe(this@MainActivity) {
                adapter.submitData(lifecycle,it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflate: MenuInflater = menuInflater
        inflate.inflate(R.menu.app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btnLogout -> {
                viewModel.logout()
                finish()
                true
            }
            R.id.btnMap -> {
                val intent = Intent(this@MainActivity,MapsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }


    override fun onBackPressed() {
        exitProcess(0)
        super.onBackPressed()
    }

    /*
        Function To ShowLoading Progress Bar
     */
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    /*
        Check Session With Token
     */
    private fun isSession() {
        viewModel.getToken().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                viewModel.getAllStories("Bearer ${user.token}")
            }
        }
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.fabButton -> {
                val intent = Intent(this@MainActivity, StoryActivity::class.java)
                startActivity(intent)
            }
        }
    }

}