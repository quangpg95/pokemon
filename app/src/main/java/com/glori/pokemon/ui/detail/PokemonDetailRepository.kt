package com.glori.pokemon.ui.detail

import com.glori.pokemon.data.NetworkBoundRepository
import com.glori.pokemon.database.AppDatabase
import com.glori.pokemon.database.PokemonDetailDB
import com.glori.pokemon.database.mapToDB
import com.glori.pokemon.model.PokemonDetailResponse
import com.glori.pokemon.model.State
import com.glori.pokemon.network.PokemonClient
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class PokemonDetailRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val pokemonClient: PokemonClient
) {

    fun getPokemon(name: String): Flow<State<PokemonDetailDB>> {
        return object : NetworkBoundRepository<PokemonDetailDB, PokemonDetailResponse>() {
            override suspend fun saveRemoteData(response: PokemonDetailResponse) {
                appDatabase.pokemonDetailDao().insertPokemon(response.mapToDB())
            }

            override fun fetchFromLocal(): Flow<PokemonDetailDB> {
                return appDatabase.pokemonDetailDao().getPokemon(name)
            }

            override suspend fun fetchFromRemote(): Response<PokemonDetailResponse> {
                return pokemonClient.getPokemon(name)
            }
        }.asFlow()
    }

}