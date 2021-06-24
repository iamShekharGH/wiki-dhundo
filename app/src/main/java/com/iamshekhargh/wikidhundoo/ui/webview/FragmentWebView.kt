package com.iamshekhargh.wikidhundoo.ui.webview

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.iamshekhargh.wikidhundoo.R
import com.iamshekhargh.wikidhundoo.databinding.FragmentWebviewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by <<-- iamShekharGH -->>
 * on 25 June 2021, Friday
 * at 12:45 AM
 */
@AndroidEntryPoint
class FragmentWebView @Inject constructor() : Fragment(R.layout.fragment_webview) {
    val viewModel: FragmentWebviewViewModel by viewModels()
    lateinit var binding: FragmentWebviewBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWebviewBinding.bind(view)

        binding.apply {
            fragmentWebview.webViewClient = WebViewClient()
            fragmentWebview.apply {
                loadUrl("https://en.wikipedia.org/?curid=${viewModel.url}" ?: "www.google.com")
                settings.javaScriptEnabled = true
            }

        }

    }
}