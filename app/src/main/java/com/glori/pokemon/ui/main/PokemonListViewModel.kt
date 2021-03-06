package com.glori.pokemon.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import androidx.paging.map
import com.glori.pokemon.domain.PokemonUI
import com.glori.pokemon.repository.PokemonListRepository
import kotlinx.coroutines.flow.*

@ExperimentalPagingApi
class PokemonListViewModel @ViewModelInject constructor(
    repository: PokemonListRepository
) : ViewModel() {

    val pokemonList = repository.fetPokemonList().cachedIn(viewModelScope).map {
        it.map { pokemonDB ->
            PokemonUI(name = pokemonDB.name, imageUrl = pokemonDB.url)
        }
    }
}