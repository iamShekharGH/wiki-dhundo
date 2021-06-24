package com.iamshekhargh.wikidhundoo.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.iamshekhargh.wikidhundoo.network.response.Terms
import com.iamshekhargh.wikidhundoo.network.response.Thumbnail

/**
 * Created by <<-- iamShekharGH -->>
 * on 23 June 2021, Wednesday
 * at 4:27 PM
 */
class MyTypeConverter {

    @TypeConverter
    fun fromTermsToString(terms: Terms): String {
        val gson = Gson()
        return gson.toJson(terms)
    }

    @TypeConverter
    fun fromStringToTerms(json: String): Terms {
        val gson = Gson()
        return gson.fromJson(json, Terms::class.java)
    }

    @TypeConverter
    fun fromThumbnailToString(thumbnail: Thumbnail): String {
        val gson = Gson()
        return gson.toJson(thumbnail)
    }

    @TypeConverter
    fun fromStringToThumbnail(json: String): Thumbnail {
        val gson = Gson()
        return gson.fromJson(json, Thumbnail::class.java)
    }
}