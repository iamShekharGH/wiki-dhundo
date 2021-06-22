package com.iamshekhargh.wikidhundoo.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamshekhargh.wikidhundoo.network.response.ResponseWikiList
import com.iamshekhargh.wikidhundoo.repository.RepoEvents
import com.iamshekhargh.wikidhundoo.repository.WikiDhundooRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentSearchViewModel @Inject constructor(
    private val repo: WikiDhundooRepo
) : ViewModel() {
    private val event = Channel<SearchEvents>()
    val eventsFlow = event.consumeAsFlow()

    fun findOut(query: String) = viewModelScope.launch {
        if (query.isNotBlank() && query.isNotEmpty()) {
            event.send(SearchEvents.ShowEmpty(false))
            val results = repo.getSearchResults(query)
        } else {
            event.send(SearchEvents.ShowEmpty(true))
        }
    }

    fun collectResponseFromRepo() = viewModelScope.launch {
        repo.responseEventsFlow.collect { responseEvent ->
            when (responseEvent) {
                is RepoEvents.ListResponseArrived -> {
                    event.send(SearchEvents.ListResponseArrived(responseEvent.responseWikiList))
                }
            }

        }
    }
}

sealed class SearchEvents {
    data class ShowSnackbar(val text: String) : SearchEvents()
    data class ShowEmpty(val listState: Boolean) : SearchEvents()
    data class ListResponseArrived(val responseWikiList: ResponseWikiList) : SearchEvents()
}