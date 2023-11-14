package com.example.storyappsubmission.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyappsubmission.data.enitity.StoryEntity

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: StoryEntity)

    @Query("SELECT * FROM story")
    fun getAllStories(): PagingSource<Int,StoryEntity>


    @Query("DELETE FROM story")
    fun deleteAllStories()
}