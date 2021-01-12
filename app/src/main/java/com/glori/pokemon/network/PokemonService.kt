package com.glori.pokemon.network

import com.glori.pokemon.model.PokemonDetailResponse
import com.glori.pokemon.model.PokemonListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {
    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: String?
    ): PokemonListResponse

    @GET("pokemon/{name}")
    suspend fun getPokemon(@Path("name") name: String): Response<PokemonDetailResponse>
}