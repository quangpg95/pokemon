package com.glori.pokemon.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.glori.pokemon.database.PokemonDetailDB
import com.glori.pokemon.model.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PokemonDetailViewModel @ViewModelInject constructor(
    private val repository: PokemonDetailRepository
) : ViewModel() {
    private val _pokemon = MutableLiveData<State<PokemonDetailDB>>()

    val pokemon: LiveData<State<PokemonDetailDB>> get() = _pokemon

    fun getPokemon(name: String) {
        viewModelScope.launch {
            repository.getPokemon(name).collect {
                _pokemon.value = it
            }
        }

    }
}