package com

import com.example.storyappsubmission.data.enitity.StoryEntity

object DataDummy {

    fun generateStoryResponse(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                "id $i",
                "name $i ",
                "Lorem Ipsum + $i",
                "photourl + $i",
                        10.212,
                -16.00
            )
            items.add(story)
        }
        return items
    }
}