package com.iamshekhargh.wikidhundoo.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.iamshekhargh.wikidhundoo.network.response.Terms

/**
 * Created by <<-- iamShekharGH -->>
 * on 23 June 2021, Wednesday
 * at 4:41 PM
 */
class TypeConverterTerms {

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
}