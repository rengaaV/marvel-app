package com.vagner.marvel.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.vagner.marvel.data.model.ThumbnailModel

class MarvelConverters {

    @TypeConverter
    fun fromThumbnail(thumbnailModel : ThumbnailModel) : String = Gson().toJson(thumbnailModel)

    @TypeConverter
    fun toThumbnail(thumbnailModel: String) : ThumbnailModel =
        Gson().fromJson(thumbnailModel, ThumbnailModel::class.java)
}