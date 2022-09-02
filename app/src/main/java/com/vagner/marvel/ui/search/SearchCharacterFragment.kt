package com.vagner.marvel.ui.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vagner.marvel.R
import com.vagner.marvel.databinding.FragmentSearchCharacterBinding
import com.vagner.marvel.ui.adapters.CharacterAdapter
import com.vagner.marvel.ui.base.BaseFragment
import com.vagner.marvel.ui.state.ResourceState
import com.vagner.marvel.util.hide
import com.vagner.marvel.util.show
import com.vagner.marvel.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchCharacterFragment :
    BaseFragment<FragmentSearchCharacterBinding, SearchCharacterViewModel>() {

    override val viewModel: SearchCharacterViewModel by viewModels()

    private val characterAdapter by lazy {
        CharacterAdapter { characterModel ->
            val action = SearchCharacterFragmentDirections
                .actionSearchCharacterFragmentToDetailsCharacterFragment3(characterModel)
            findNavController().navigate(action)
        }
    }

    var query: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        query = binding.edSearchCharacter.text.toString()

        searchInit(query)
        collectObserver()

    }


    private fun collectObserver() = lifecycleScope.launch {
        viewModel.searchCharacter.collect { result ->
            when (result) {
                is ResourceState.Success -> {
                    binding.progressbarSearch.hide()
                    result.data?.let {
                        characterAdapter.listCharacter = it.data.results.toList()
                    }
                }
                is ResourceState.Error -> {
                    binding.progressbarSearch.hide()
                    result.message?.let { message ->
                        Timber.tag("SearchCharacterFragment").e("Error -> $message")
                        toast(getString(R.string.an_error_occurred))
                    }
                }
                is ResourceState.Loading -> {
                    binding.progressbarSearch.show()
                }
                else -> {}
            }
        }
    }

    private fun searchInit(query: String) {
        binding.edSearchCharacter.apply {
            setText(query)
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    updateCharacterList()
                    true
                } else {
                    false
                }
            }

            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateCharacterList()
                    true
                } else {
                    false
                }
            }
        }

    }

    private fun updateCharacterList() {
        binding.edSearchCharacter.apply {
            editableText.trim().let {
                if (it.isNotEmpty()) {
                    searchQuery(it.toString())
                }
            }
        }
    }

    private fun searchQuery(query: String) {
        viewModel.fetch(query)
    }


    private fun setupRecyclerView() {
        binding.rvSearchCharacter.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchCharacterBinding =
        FragmentSearchCharacterBinding.inflate(inflater, container, false)

}