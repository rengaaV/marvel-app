package com.vagner.marvel.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vagner.marvel.R
import com.vagner.marvel.databinding.FragmentFavoriteCharacterBinding
import com.vagner.marvel.ui.adapters.CharacterAdapter
import com.vagner.marvel.ui.base.BaseFragment
import com.vagner.marvel.ui.state.ResourceState
import com.vagner.marvel.util.hide
import com.vagner.marvel.util.show
import com.vagner.marvel.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteCharacterFragment :
    BaseFragment<FragmentFavoriteCharacterBinding, FavoriteCharacterViewModel>() {

    override val viewModel: FavoriteCharacterViewModel by viewModels()
    private val characterAdapter by lazy {
        CharacterAdapter { characterModel ->
            val action = FavoriteCharacterFragmentDirections
                .actionFavoriteCharacterFragmentToDetailsCharacterFragment2(characterModel)
            findNavController().navigate(action)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observer()
    }

    private fun observer() = lifecycleScope.launch {
        viewModel.favorites.collect { result ->
            when (result) {
                is ResourceState.Success -> {
                    result.data?.let {
                        binding.tvEmptyList.hide()
                        characterAdapter.listCharacter = it.toList()
                    }
                }
                is ResourceState.Empty -> {
                    binding.tvEmptyList.show()
                }
                else -> {}
            }

        }
    }

    private fun itemTouchHelperCallBack(): ItemTouchHelper.SimpleCallback {
        return object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val character = characterAdapter.getCharacterPosition(viewHolder.adapterPosition)
                viewModel.delete(character).also {
                    toast(getString(R.string.message_delete_character))
                }
            }

        }
    }

    private fun setupRecyclerView() {
        binding.rvFavoriteCharacter.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        ItemTouchHelper(itemTouchHelperCallBack()).attachToRecyclerView(binding.rvFavoriteCharacter)
    }


    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFavoriteCharacterBinding =
        FragmentFavoriteCharacterBinding.inflate(inflater, container, false)


}