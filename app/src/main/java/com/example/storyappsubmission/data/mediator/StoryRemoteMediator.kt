package com.example.storyappsubmission.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.storyappsubmission.data.database.StoryDataBase
import com.example.storyappsubmission.data.enitity.RemoteEntity
import com.example.storyappsubmission.data.enitity.StoryEntity
import com.example.storyappsubmission.data.remote.retrofit.ApiService
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator (
    private val dataBase: StoryDataBase,
    private val apiService: ApiService,
    private val token: String,
): RemoteMediator<Int,StoryEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {
        val page = when(loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosesToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }
        try {
            val response = apiService.getStories(token,page,state.config.pageSize)
            val endOfPaginationReached = response.listStory.isEmpty()

            dataBase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dataBase.remoteKeysDao().deleteRemoteKeys()
                    dataBase.storyDao().deleteAllStories()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = response.listStory.map {
                    RemoteEntity(id = it.id!!,prevKey = prevKey, nextKey = nextKey)
                }
                dataBase.remoteKeysDao().insertAllStories(keys)

                response.listStory.forEach {listStoryItem ->
                    val stories = StoryEntity (
                        listStoryItem.id!!,
                        listStoryItem.name!!,
                        listStoryItem.description!!,
                        listStoryItem.photoUrl!!,
                        listStoryItem.lat,
                        listStoryItem.lon
                    )
                    dataBase.storyDao().insertStory(stories)
                }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteEntity? {
        return state.pages.lastOrNull {it.data.isNotEmpty()}?.data?.lastOrNull()?.let { data ->
            dataBase.remoteKeysDao().getRemoteKeys(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteEntity? {
        return state.pages.firstOrNull {it.data.isNotEmpty()}?.data?.firstOrNull()?.let { data ->
            dataBase.remoteKeysDao().getRemoteKeys(data.id)
        }
    }

    private suspend fun getRemoteKeyClosesToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                dataBase.remoteKeysDao().getRemoteKeys(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}