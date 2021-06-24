package com.iamshekhargh.wikidhundoo.ui.search

import androidx.lifecycle.*
import com.iamshekhargh.wikidhundoo.network.response.Page
import com.iamshekhargh.wikidhundoo.repository.WikiRepository
import com.iamshekhargh.wikidhundoo.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FragmentSearchViewModel @Inject constructor(
    private val repo: WikiRepository
) : ViewModel() {
    private val event = Channel<SearchEvents>()
    val eventsFlow = event.receiveAsFlow()

    private val refreshTriggerChannel = Channel<Refresh>()
    private val refreshTrigger = refreshTriggerChannel.receiveAsFlow()

    val pagesFlow = refreshTrigger.flatMapLatest { refresh ->
        repo.getQueriedPageList(
            refresh == Refresh.FORCE
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val pages = pagesFlow.asLiveData()

    fun findOut(query: String) = viewModelScope.launch {

        if (query.isNotBlank() && query.isNotEmpty()) {
            repo.submitSearchQuery(query)
            event.send(SearchEvents.ShowEmpty(false))
        } else {
            event.send(SearchEvents.ShowEmpty(true))
        }
    }

    fun onStart() {
        if (pagesFlow.value !is Resource.Loading) {
            viewModelScope.launch {
                refreshTriggerChannel.send(Refresh.NORMAL)
            }
        }
    }

    fun triggerFetch()  = viewModelScope.launch {
        refreshTriggerChannel.send(Refresh.FORCE)
    }

    fun openDetailsPage(p: Page) = viewModelScope.launch {
        event.send(SearchEvents.OpenDetailsFragment(p))
    }

}

sealed class SearchEvents {
    data class ShowSnackbar(val text: String) : SearchEvents()
    data class ShowEmpty(val listState: Boolean) : SearchEvents()
    data class ListResponseArrived(val responseWikiList: List<Page>) : SearchEvents()
    data class OpenDetailsFragment(val p: Page) : SearchEvents()
}

enum class Refresh {
    FORCE, NORMAL
}