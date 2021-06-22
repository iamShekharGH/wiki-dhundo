package com.iamshekhargh.wikidhundoo.ui.first

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.iamshekhargh.wikidhundoo.R
import com.iamshekhargh.wikidhundoo.databinding.FragmentFirstBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class FragmentFirst : Fragment(R.layout.fragment_first) {

    private val viewModel: FragmentFirstViewModel by viewModels()
    lateinit var binding: FragmentFirstBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFirstBinding.bind(view)

        binding.apply {


        }

        setupEvents()
    }

    private fun setupEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventAsFlow.collect { e ->
                when (e) {
                    FirstFragEvent.TestOne -> {

                    }
                }

            }
        }
    }


}