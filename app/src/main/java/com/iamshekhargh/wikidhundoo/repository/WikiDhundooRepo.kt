package com.iamshekhargh.wikidhundoo.repository

import android.util.Log
import com.iamshekhargh.wikidhundoo.database.PagesDao
import com.iamshekhargh.wikidhundoo.network.WikiApi
import com.iamshekhargh.wikidhundoo.network.response.Page
import com.iamshekhargh.wikidhundoo.network.response.ResponseWikiList
import com.iamshekhargh.wikidhundoo.network.response.itemResponse.ResponseSearchItem
import com.iamshekhargh.wikidhundoo.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

class WikiDhundooRepo constructor(
    private val api: WikiApi,
    private val scope: CoroutineScope,
    private val dao: PagesDao
) {

    private val responseEvents = Channel<RepoEvents>()
    val responseEventsFlow = responseEvents.consumeAsFlow()

    var pageList = mutableListOf<Page>()
    private var query = ""

    suspend fun getSearchResults(q: String) {
        query = q
        val response = searchOnWikipedia(q)
        if (response != null && response.query != null) {
            addToDb(response.query.pages)
            getFromDb(query)
        }
    }

    private suspend fun addToDb(pagesResponse: List<Page>) {
        pagesResponse.forEach { page ->
            try {
                dao.insertPage(page)
            } catch (e: Exception) {
                Log.d("addToDb: ", "${page.title} dident save coz ${e.message}")
            }
        }

//        dao.insertPages(pagesResponse)
    }

    private suspend fun getFromDb(q: String) {
        scope.launch {
            val items = dao.getSearchResultsList(query)
            populateList(items)
        }
    }

    private suspend fun populateList(pagesResponse: List<Page>) {
        responseEvents.send(RepoEvents.ListResponseArrived(pagesResponse))
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
        return response
    }

    suspend fun getPageInfo(pageId: String): Resource<ResponseSearchItem> {
        //https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts|pageimages&pithumbsize=500
        // &exintro&explaintext&formatversion=2&pageids=232224
        val m = mutableMapOf<String, String>()
        //https://en.wikipedia.org/w/api.php?format=json&action=query
        // &prop=extracts|pageimages&pithumbsize=500&exintro&explaintext&formatversion=2&pageids=232224
        m["format"] = "json"
//        m["generator"] = "prefixsearch"
        m["action"] = "query"
        m["prop"] = "extracts|pageimages|pageterms"
        m["formatversion"] = "2"
        m["pithumbsize"] = "500"
//        m["exintro"] = ""
//        m["explaintext"] = ""
        m["pageids"] = pageId
//        m["pageids"] = "232224"
        val response = try {
            api.getPageInfo(m)
        } catch (e: Exception) {
            e.printStackTrace()
            return Resource.Error("Sorry! Could not connect now.${e.message}")
        }
        return if (response != null) {
            Resource.Success(response)
        } else Resource.Error("Sorry! Could not connect now.")
    }

//    private suspend fun getResults(query: String): Resource<ResponseWikiList> {
//
//        val m = mutableMapOf<String, String>()
//        m["format"] = "json"
//        m["generator"] = "prefixsearch"
//        m["action"] = "query"
//        m["prop"] = "extracts|pageimages|pageterms"
//        m["formatversion"] = "2"
//        m["pithumbsize"] = "200"
//        m["wbptterms"] = "description"
//        m["gpslimit"] = "10"
//        m["gpssearch"] = query
//
//        val response = try {
//            api.getList(m)
//        } catch (e: Exception) {
//            return Resource.Error("Sorry! Could not connect now.${e.message}")
//        }
//        val data = response.data
//        return if (data != null) {
//            populateList(data.query.pages)
//            Resource.Success(data)
//        } else Resource.Error("Sorry! Could not connect now.")
//    }
}

sealed class RepoEvents {
    data class ListResponseArrived(val responseWikiList: List<Page>) : RepoEvents()
    data class ListResponseError(val text: String) : RepoEvents()

}

/*
suspend fun getSearchResultsState(q: String) {
        query = q
        val response = getResults(query)
        if (response.data != null) {

            addToDb(response.data.query.pages)
        } else {
            responseEvents.send(RepoEvents.ListResponseError("Sorry Could not load from wiki."))
        }
        getFromDb(query)
    }
* */