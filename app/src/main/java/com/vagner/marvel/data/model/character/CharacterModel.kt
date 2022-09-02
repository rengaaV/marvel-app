package com.vagner.marvel.data.model.character

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vagner.marvel.data.model.ThumbnailModel
import java.io.Serializable

@Entity(tableName = "characterModel")
data class CharacterModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: ThumbnailModel
) : Serializable
