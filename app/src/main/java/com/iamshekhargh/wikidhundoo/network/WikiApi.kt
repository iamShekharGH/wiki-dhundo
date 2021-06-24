package com.iamshekhargh.wikidhundoo.network

import com.iamshekhargh.wikidhundoo.network.response.ResponseWikiList
import com.iamshekhargh.wikidhundoo.network.response.itemResponse.ResponseSearchItem
import com.iamshekhargh.wikidhundoo.util.Resource
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface WikiApi {

    companion object {
        const val BASE_URL = "https://en.wikipedia.org/w/api.php/"

    }

    @Headers("Content-Type: application/json")
    @GET("?")
    suspend fun getListStateless(@QueryMap queries: Map<String, String>): ResponseWikiList

    @Headers("Content-Type: application/json")
    @GET("?")
    suspend fun getPageInfo(@QueryMap queries: Map<String, String>): ResponseSearchItem


}