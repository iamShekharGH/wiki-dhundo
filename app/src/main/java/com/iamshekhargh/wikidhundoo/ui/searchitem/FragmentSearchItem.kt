package com.iamshekhargh.wikidhundoo.ui.searchitem

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.iamshekhargh.wikidhundoo.R
import com.iamshekhargh.wikidhundoo.databinding.FragmentSearchItemBinding
import com.iamshekhargh.wikidhundoo.network.response.itemResponse.ResponseSearchItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

/**
 * Created by <<-- iamShekharGH -->>
 * on 24 June 2021, Thursday
 * at 12:42 AM
 */
@AndroidEntryPoint
class FragmentSearchItem : Fragment(R.layout.fragment_search_item) {

    val viewModel: FragmentSearchItemViewModel by viewModels()
    lateinit var binding: FragmentSearchItemBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchItemBinding.bind(view)

        binding.apply {
            Glide.with(fragmentItemImage).load(viewModel.page?.thumbnail?.source).centerCrop()
                .placeholder(R.drawable.image).into(fragmentItemImage)
            fragmentItemDescription.text = viewModel.page?.terms?.description.toString()

            fragmentItemFab.setOnClickListener {
                viewModel.openWebView()
            }
        }

        viewModel.getCompletePageInfo()
        setupEvents()

        setHasOptionsMenu(true)
    }

    private fun setupEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsFlow.collect { event ->
                when (event) {
                    is SearchItemEvents.ShowSnackbar -> {
                        showSnackbar(event.text)
                    }
                    is SearchItemEvents.SendPageInfo -> {
                        loadInformation(event.page)
                    }
                    is SearchItemEvents.OpenWebView -> {
                        val action =
                            FragmentSearchItemDirections.actionFragmentSearchItemToFragmentWebView(
                                event.url
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun loadInformation(page: ResponseSearchItem) {
        binding.apply {
            val p = page.query.pages[0]
            Glide.with(fragmentItemImage).load(p.thumbnail.source).centerCrop()
                .placeholder(R.drawable.image).into(fragmentItemImage)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fragmentItemDescription.text = Html.fromHtml(p.extract, Html.FROM_HTML_MODE_COMPACT)
            } else {
                fragmentItemDescription.text = Html.fromHtml(p.extract)
            }
            activity?.title = p.title
            fragmentItemToolbar.title = p.title
        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }
}