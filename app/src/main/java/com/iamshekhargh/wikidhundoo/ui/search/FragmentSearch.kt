package com.iamshekhargh.wikidhundoo.ui.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.iamshekhargh.wikidhundoo.R
import com.iamshekhargh.wikidhundoo.databinding.FragmentSearchResultBinding
import com.iamshekhargh.wikidhundoo.network.response.Page
import com.iamshekhargh.wikidhundoo.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentSearch : Fragment(R.layout.fragment_search_result), SearchAdapter.OnPageClicked {

    private val viewModel: FragmentSearchViewModel by viewModels()
    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var adapter: SearchAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchResultBinding.bind(view)
        adapter = SearchAdapter(this)

        binding.apply {
            fragmentSearchTiedittext.doAfterTextChanged {

            }

            fragmentSearchSearchIv.setOnClickListener {
                if (fragmentSearchTiedittext.text.isNullOrEmpty()) {
                    showSnackbar("Please Enter text to search")
                } else {
                    viewModel.findOut(fragmentSearchTiedittext.text.toString())
                    viewModel.triggerFetch()
                    hideKeyboard()
                }
            }
            fragmentSearchRv.adapter = adapter
            fragmentSearchRv.layoutManager = LinearLayoutManager(requireContext())
        }

//        observePages()
        setupEvents()
        collectPages()
    }

    private fun collectPages() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.pagesFlow.collect {
                val result = it ?: return@collect

                binding.fragmentSearchProgressbar.isVisible = result is Resource.Loading

                adapter.submitList(result.data)
            }
        }
    }

    private fun observePages() {
        binding.apply {
            viewModel.pages.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    adapter.submitList(response.data)
                }

                response is Resource.Loading && response.data.isNullOrEmpty()
                if (response is Resource.Error && response.data.isNullOrEmpty()) {
                    fragmentSearchRvEmptyTv.text = response.message ?: "Error"
                    showEmptyList(false)
                }
            }
        }
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
                        showSnackbar("Results have arrived.")
                        adapter.submitList(event.responseWikiList)
                    }
                    is SearchEvents.OpenDetailsFragment -> {
                        val action =
                            FragmentSearchDirections.actionFragmentSearchToFragmentSearchItem(event.p)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
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

    override fun pageItemClicked(p: Page) {
        viewModel.openDetailsPage(p)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }
}