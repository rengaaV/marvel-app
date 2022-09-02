package com.vagner.marvel.data.model.character

import java.io.Serializable

data class CharacterModelData(
    val results: List<CharacterModel>
) : Serializable
