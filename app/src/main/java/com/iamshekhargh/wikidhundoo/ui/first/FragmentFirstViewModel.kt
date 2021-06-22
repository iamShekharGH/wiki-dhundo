package com.iamshekhargh.wikidhundoo.ui.first

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import javax.inject.Inject

@HiltViewModel
class FragmentFirstViewModel @Inject constructor() : ViewModel() {

    private val event = Channel<FirstFragEvent>()
    val eventAsFlow = event.consumeAsFlow()
}

sealed class FirstFragEvent {
    object TestOne : FirstFragEvent()
}