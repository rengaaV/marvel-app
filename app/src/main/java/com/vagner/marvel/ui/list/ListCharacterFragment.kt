package com.vagner.marvel.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.vagner.marvel.R
import com.vagner.marvel.databinding.FragmentListCharacterBinding
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
class ListCharacterFragment : BaseFragment<FragmentListCharacterBinding, ListCharacterViewModel>() {

    override val viewModel: ListCharacterViewModel by viewModels()

    private val characterAdapter by lazy {
        CharacterAdapter { characterModel ->
            val action = ListCharacterFragmentDirections
                .actionListCharacterFragmentToDetailsCharacterFragment(characterModel)
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        collectObserver()
    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.list.collect { result ->
            when (result) {
                is ResourceState.Success -> {
                    result.data?.let { values ->
                        binding.progressCircular.hide()
                        characterAdapter.listCharacter = values.data.results.toList()
                    }
                }
                is ResourceState.Error -> {
                    binding.progressCircular.hide()
                    result.message?.let { message ->
                        toast(getString(R.string.an_error_occurred))
                        Timber.tag("ListCharacterFragment").e("Error -> $message")
                    }
                }
                is ResourceState.Loading -> {
                    binding.progressCircular.show()
                }
                else -> {}
            }

        }
    }

    private fun setupRecyclerView() {
        binding.rvCharacters.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListCharacterBinding =
        FragmentListCharacterBinding.inflate(inflater, container, false)


}