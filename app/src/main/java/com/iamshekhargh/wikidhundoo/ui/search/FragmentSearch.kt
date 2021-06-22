package com.iamshekhargh.wikidhundoo.ui.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.iamshekhargh.wikidhundoo.R
import com.iamshekhargh.wikidhundoo.databinding.FragmentSearchResultBinding
import com.iamshekhargh.wikidhundoo.network.response.ResponseWikiList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentSearch : Fragment(R.layout.fragment_search_result) {

    private val viewModel: FragmentSearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchResultBinding
    lateinit var adapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchResultBinding.bind(view)
        viewModel.collectResponseFromRepo()
        adapter = SearchAdapter()

        binding.apply {
            fragmentSearchTiedittext.doAfterTextChanged {
                Toast.makeText(requireContext(), "This :: $it", Toast.LENGTH_SHORT).show()
                viewModel.findOut(it.toString())
            }
            fragmentSearchRv.adapter = adapter
            fragmentSearchRv.layoutManager = LinearLayoutManager(requireContext())
        }

        setupEvents()

    }

    private fun setupEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventsFlow.collect { event ->
                when (event) {
                    is SearchEvents.ShowSnackbar -> {

                    }
                    is SearchEvents.ShowEmpty -> {
                        showEmptyList(event.listState)
                    }
                    is SearchEvents.ListResponseArrived -> {
                        sendListToAdapter(event.responseWikiList)

                    }
                }
            }
        }
    }

    private fun sendListToAdapter(responseWikiList: ResponseWikiList) {
        adapter.submitList(responseWikiList.query.pages)
    }

    private fun showEmptyList(listState: Boolean) {
        binding.apply {
            if (listState) {
                fragmentSearchRv.isVisible = false
                fragmentSearchRv.visibility = View.GONE

                fragmentSearchRvEmptyTv.isVisible = true
                fragmentSearchRvEmptyTv.visibility = View.VISIBLE
            } else {
                fragmentSearchRv.isVisible = true
                fragmentSearchRv.visibility = View.VISIBLE

                fragmentSearchRvEmptyTv.isVisible = false
                fragmentSearchRvEmptyTv.visibility = View.GONE
            }
        }
    }
}