package com.iamshekhargh.wikidhundoo.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.asLiveData
import com.iamshekhargh.wikidhundoo.database.PagesDao
import com.iamshekhargh.wikidhundoo.network.WikiApi
import com.iamshekhargh.wikidhundoo.network.response.ResponseWikiList
import com.iamshekhargh.wikidhundoo.network.response.itemResponse.ResponseSearchItem
import com.iamshekhargh.wikidhundoo.util.Resource
import com.iamshekhargh.wikidhundoo.util.networkBoundResource
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject


/**
 * Created by <<-- iamShekharGH -->>
 * on 24 June 2021, Thursday
 * at 2:09 PM
 */
@ActivityScoped
class WikiRepository @Inject constructor(
    private val api: WikiApi,
    private val scope: CoroutineScope,
    private val dao: PagesDao,
    private val app: Application
) {
//
//    private val searchQueryChannel = Channel<String>()
//    private val searchQueryAsFlow = searchQueryChannel.receiveAsFlow()
//    private val searchQueryAsLiveData = searchQueryAsFlow.asLiveData()

    var querytext = ""

    fun getQueriedPageList(forceRefresh: Boolean) = networkBoundResource(
        searchThis = {
            querytext
        },
        query = { queryText ->
            dao.getSearchResults(queryText)
        },
        fetch = { queryText ->
            searchOnWikipedia(queryText)
        },
        saveFetchResult = { responseList ->
            responseList.query.pages.forEach { page ->
                try {
                    dao.insertPage(page)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        },
        shouldFetch = {
            forceRefresh
        }
    )

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }

    fun submitSearchQuery(text: String) {
        querytext = text
    }

    private suspend fun searchOnWikipedia(query: String): ResponseWikiList {
        val m = mutableMapOf<String, String>()
        m["format"] = "json"
        m["generator"] = "prefixsearch"
        m["action"] = "query"
        m["prop"] = "extracts|pageimages|pageterms"
        m["formatversion"] = "2"
        m["pithumbsize"] = "200"
        m["wbptterms"] = "description"
        m["gpslimit"] = "10"
        m["gpssearch"] = query
        val response = api.getListStateless(m)
//        return response.query.pages
        return response
    }

    suspend fun getPageInfo(pageId: String): Resource<ResponseSearchItem> {
        val m = mutableMapOf<String, String>()
        m["format"] = "json"
        m["action"] = "query"
        m["prop"] = "extracts|pageimages|pageterms&exintro&explaintext"
        m["formatversion"] = "2"
        m["pithumbsize"] = "500"
        m["pageids"] = pageId
//        m["pageids"] = "232224"

        val response = try {
            api.getPageInfo(m)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return Resource.Error("Sorry! Could not connect now.${e.message}")
        }
        return if (response != null) {
            Resource.Success(response)
        } else Resource.Error("Sorry! Could not connect now.")
    }
}
