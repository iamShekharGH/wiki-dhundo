package com.iamshekhargh.wikidhundoo.repository

import com.iamshekhargh.wikidhundoo.network.WikiApi
import com.iamshekhargh.wikidhundoo.network.response.ResponseWikiList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WikiDhundooRepo @Inject constructor(
    private val api: WikiApi
) {

    private val responseEvents = Channel<RepoEvents>()
    val responseEventsFlow = responseEvents.receiveAsFlow()

    suspend fun getSearchResults(query: String) {
        val m = mutableMapOf<String, String>()
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

        m["format"] = "json"
        m["generator"] = "prefixsearch"
        m["action"] = "query"
        m["prop"] = "extracts|pageimages|pageterms"
        m["formatversion"] = "2"
        m["pithumbsize"] = "200"
        m["wbptterms"] = "description"
        m["gpslimit"] = "10"
        m["gpssearch"] = query

        val response = api.getList(m)
        responseEvents.send(RepoEvents.ListResponseArrived(response))
    }
}

sealed class RepoEvents {
    data class ListResponseArrived(val responseWikiList: ResponseWikiList) : RepoEvents()

}