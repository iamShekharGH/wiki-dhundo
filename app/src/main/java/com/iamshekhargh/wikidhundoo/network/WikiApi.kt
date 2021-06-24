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
//     For details based on page no.
//        https://en.wikipedia.org/w/api.php?
//                format=json&action=query
//                &prop=extracts|pageimages|pageterms
//                &pithumbsize=500
//                &exintro&explaintext
//                &redirects=1
//                &pageids=232224

//     For list based on search
//     https://en.wikipedia.org/w/api.php?
        //     action=query
        //     &format=json
        //     &gpssearch=Shekhar
        //     &formatversion=2
        //     &generator=prefixsearch
        //     &prop=pageimages|pageterms
//         &pithumbsize=500
        //         &pilimit=10
        //         &wbptterms=description
        //         &gpssearch=Shekhar
        //         &gpslimit=10

    }
//
//    @Headers("Content-Type: application/json")
//    @GET("?")
//    suspend fun getList(@QueryMap queries: Map<String, String>): Resource<ResponseWikiList>

    @Headers("Content-Type: application/json")
    @GET("?")
    suspend fun getListStateless(@QueryMap queries: Map<String, String>): ResponseWikiList

    @Headers("Content-Type: application/json")
    @GET("?")
    suspend fun getPageInfo(@QueryMap queries: Map<String, String>): ResponseSearchItem


}