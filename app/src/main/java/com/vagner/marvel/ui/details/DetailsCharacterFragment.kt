package com.vagner.marvel.ui.details

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.vagner.marvel.R
import com.vagner.marvel.data.model.character.CharacterModel
import com.vagner.marvel.databinding.FragmentDetailsCharacterBinding
import com.vagner.marvel.ui.adapters.ComicAdapter
import com.vagner.marvel.ui.base.BaseFragment
import com.vagner.marvel.ui.state.ResourceState
import com.vagner.marvel.util.hide
import com.vagner.marvel.util.show
import com.vagner.marvel.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class DetailsCharacterFragment :
    BaseFragment<FragmentDetailsCharacterBinding, DetailsCharacterViewModel>(), MenuProvider {

    override val viewModel: DetailsCharacterViewModel by viewModels()
    private val args: DetailsCharacterFragmentArgs by navArgs()
    private lateinit var characterModel: CharacterModel
    private val comicAdapter by lazy { ComicAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        characterModel = args.character
        setupRecyclerView()
        onLoadedCharacter(characterModel)
        collectObserver()
        viewModel.fetch(characterModel.id)
        onShowDialog()
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_details, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.favorite -> {
                viewModel.insert(characterModel)
                toast(getString(R.string.saved_successfully))
                true
            }
            else -> {
                false
            }
        }
    }

    private fun onShowDialog() {
        binding.tvDescriptionCharacterDetails.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(characterModel.name)
                .setMessage(characterModel.description)
                .setNegativeButton(getString((R.string.close_dialog))) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.details.collect { result ->
            when (result) {
                is ResourceState.Success -> {
                    binding.progressBarDetail.hide()
                    result.data?.let { values ->
                        if (values.data.results.isNotEmpty()) {
                            comicAdapter.listComic = values.data.results.toList()
                        } else {
                            toast(getString(R.string.empty_list_comics))
                        }
                    }
                }
                is ResourceState.Error -> {
                    binding.progressBarDetail.hide()
                    result.message?.let { message ->
                        Timber.tag("DetailsCharacter").e("Error -> $message")
                        toast(getString(R.string.an_error_occurred))

                    }
                }
                is ResourceState.Loading -> {
                    binding.progressBarDetail.show()
                }
                else -> {}
            }
        }
    }

    private fun onLoadedCharacter(characterModel: CharacterModel) {
        binding.apply {
            tvNameCharacterDetails.text = characterModel.name

            if (characterModel.description.isEmpty()) {
                tvDescriptionCharacterDetails.text =
                    requireContext().getString(R.string.text_description_empty)
            } else {
                tvDescriptionCharacterDetails.text = characterModel.description
            }

            Glide.with(requireContext())
                .load(characterModel.thumbnail.path + "." + characterModel.thumbnail.extension)
                .into(imgCharacterDetails)
        }
    }

    private fun setupRecyclerView() {
        binding.rvComics.apply {
            adapter = comicAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDetailsCharacterBinding =
        FragmentDetailsCharacterBinding.inflate(inflater, container, false)

}