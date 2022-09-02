package com.vagner.marvel.data.model.comic

import java.io.Serializable

data class ComicModelData(
    val results: List<ComicModel>
) : Serializable
