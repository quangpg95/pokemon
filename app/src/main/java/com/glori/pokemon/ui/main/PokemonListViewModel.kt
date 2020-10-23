package com.glori.pokemon.ui.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.glori.pokemon.database.PokemonDB
import com.glori.pokemon.domain.PokemonUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform

@ExperimentalPagingApi
class PokemonListViewModel @ViewModelInject constructor(
    private val repository: PokemonListRepository
) : ViewModel() {

    private var currentPokemonList: Flow<PagingData<PokemonUI>>? = null
    private var currentPokemonListDB: Flow<PagingData<PokemonDB>>? = null


    fun fetchPokemonList(): Flow<PagingData<PokemonUI>> {
        val lastResult = currentPokemonList
        if (lastResult != null) {
            return lastResult
        }
        val newResult = repository.fetPokemonList().cachedIn(viewModelScope)
        currentPokemonList = newResult
        return newResult
    }

    fun fetchPokemonListFromDb(): Flow<PagingData<PokemonDB>> {
        val lastResult = currentPokemonListDB
        if (lastResult != null) {
            return lastResult
        }
        val newResult = repository.fetPokemonListFromDB().cachedIn(viewModelScope)
        currentPokemonListDB = newResult
        return newResult
    }

    val pokemonList = repository.fetPokemonListFromDB().cachedIn(viewModelScope).map {
        it.map {pokemonDB->
            PokemonUI(name = pokemonDB.name, imageUrl = pokemonDB.url)
        }
    }
}