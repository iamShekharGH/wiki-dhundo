package com.iamshekhargh.wikidhundoo.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.iamshekhargh.wikidhundoo.network.response.Terms
import com.iamshekhargh.wikidhundoo.network.response.Thumbnail

/**
 * Created by <<-- iamShekharGH -->>
 * on 23 June 2021, Wednesday
 * at 4:40 PM
 */
class TypeConverterThumbnail {

    @TypeConverter
    fun fromThumbnailToString(terms: Terms): String {
        val gson = Gson()
        return gson.toJson(terms)
    }

    @TypeConverter
    fun fromStringToThumbnail(json: String): Thumbnail {
        val gson = Gson()
        return gson.fromJson(json, Thumbnail::class.java)
    }
}