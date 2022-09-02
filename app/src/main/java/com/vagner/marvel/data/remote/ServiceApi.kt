package com.vagner.marvel.data.remote

import com.vagner.marvel.data.model.character.CharacterModelResponse
import com.vagner.marvel.data.model.comic.ComicModelResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {

    @GET("characters")
    suspend fun list(
        @Query("nameStartsWith") nameStartsWith : String? = null
    ) : Response<CharacterModelResponse>

    @GET("characters/{characterId}/comics")
    suspend fun getComics(
        @Path("characterId") characterId : Int
    ):Response<ComicModelResponse>
}