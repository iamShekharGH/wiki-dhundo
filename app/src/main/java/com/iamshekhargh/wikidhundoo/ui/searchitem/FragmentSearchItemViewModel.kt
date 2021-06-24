package com.iamshekhargh.wikidhundoo.ui.searchitem

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iamshekhargh.wikidhundoo.network.response.Page
import com.iamshekhargh.wikidhundoo.network.response.itemResponse.ResponseSearchItem
import com.iamshekhargh.wikidhundoo.repository.WikiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by <<-- iamShekharGH -->>
 * on 24 June 2021, Thursday
 * at 12:43 AM
 */
@HiltViewModel
class FragmentSearchItemViewModel @Inject constructor(
    state: SavedStateHandle,
    private val repo: WikiRepository
) : ViewModel() {
    val page = state.get<Page>("pageItem")

    private val events = Channel<SearchItemEvents>()
    val eventsFlow = events.receiveAsFlow()

    fun getCompletePageInfo() = viewModelScope.launch {
        val res = repo.getPageInfo(page?.pageid.toString())
        if (res.data != null) {
            events.send(SearchItemEvents.SendPageInfo(res.data))
        } else {
            events.send(SearchItemEvents.ShowSnackbar(res.message ?: "Could Not fetch Info"))
        }
    }

    fun openWebView() = viewModelScope.launch {
        if (page != null) {
            events.send(SearchItemEvents.OpenWebView(page.pageid.toString()))
        } else {
            events.send(SearchItemEvents.ShowSnackbar("URL link is missing. Sorry."))
        }
    }
}

sealed class SearchItemEvents {
    data class ShowSnackbar(val text: String) : SearchItemEvents()
    data class SendPageInfo(val page: ResponseSearchItem) : SearchItemEvents()
    data class OpenWebView(val url: String) : SearchItemEvents()
}