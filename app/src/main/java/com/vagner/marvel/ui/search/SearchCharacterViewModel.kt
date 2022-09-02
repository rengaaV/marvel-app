package com.vagner.marvel.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vagner.marvel.data.model.character.CharacterModelResponse
import com.vagner.marvel.repository.MarvelRepository
import com.vagner.marvel.ui.state.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchCharacterViewModel @Inject constructor(private val repository: MarvelRepository) :
    ViewModel() {

    private val _searchCharacter =
        MutableStateFlow<ResourceState<CharacterModelResponse>>(ResourceState.Empty())
    val searchCharacter: StateFlow<ResourceState<CharacterModelResponse>> = _searchCharacter

    fun fetch(nameStarsWith: String) = viewModelScope.launch {
        safeFetch(nameStarsWith)
    }

    private suspend fun safeFetch(nameStarsWith: String) {
        _searchCharacter.value = ResourceState.Loading()
        try {
            val response = repository.list(nameStarsWith)
            _searchCharacter.value = handleResponse(response)
        }catch (t : Throwable){
            when(t){
                is IOException -> _searchCharacter.value = ResourceState.Error("Erro de conexão com a iternet")
                else -> _searchCharacter.value = ResourceState.Error("Falha na conversão de dados")
            }
        }
    }

    private fun handleResponse(response: Response<CharacterModelResponse>): ResourceState<CharacterModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return ResourceState.Success(values)
            }
        }
        return ResourceState.Error(response.message())
    }

}