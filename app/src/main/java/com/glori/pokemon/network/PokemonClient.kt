package com.glori.pokemon.network

import javax.inject.Inject

class PokemonClient @Inject constructor(private val pokemonService: PokemonService) {
    suspend fun fetchPokemonList(limit: Int, offset: String?) =
        pokemonService.fetchPokemonList(limit, offset)
}