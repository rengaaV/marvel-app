package com.vagner.marvel.data.model.comic

import com.vagner.marvel.data.model.ThumbnailModel
import java.io.Serializable

data class ComicModel(
    val id: Int,
    val title: String,
    val description: String,
    val thumbnail: ThumbnailModel
) : Serializable
