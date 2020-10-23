package com.glori.pokemon.network

import com.glori.pokemon.model.PokemonListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: String?
    ): PokemonListResponse
}