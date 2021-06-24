package com.iamshekhargh.wikidhundoo.ui.webview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by <<-- iamShekharGH -->>
 * on 25 June 2021, Friday
 * at 12:46 AM
 */
@HiltViewModel
class FragmentWebviewViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel() {
    val url = state.get<String>("url")
}