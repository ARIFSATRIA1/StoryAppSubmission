package com.example.storyappsubmission.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyappsubmission.data.enitity.RemoteEntity

@Dao
interface RemoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStories(remoteKey: List<RemoteEntity>)

    @Query("SELECT * FROM remote_keys WHERE ID = :id")
    suspend fun getRemoteKeys(id:String): RemoteEntity?

    @Query("DELETE FROM remote_keys")
    suspend fun deleteRemoteKeys()
}