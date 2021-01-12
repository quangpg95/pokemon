package com.glori.pokemon.ui.main

import androidx.core.net.toUri
import androidx.paging.*
import androidx.room.withTransaction
import com.glori.pokemon.database.AppDatabase
import com.glori.pokemon.database.PokemonDB
import com.glori.pokemon.database.mapToDB
import com.glori.pokemon.network.PokemonClient
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val NETWORK_PAGE_SIZE = 20
private const val NETWORK_INIT_LOAD_SIZE = 50

@ExperimentalPagingApi
class PokemonListRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val pokemonClient: PokemonClient
) {

    // Glori: pokemon list flow from pager
    fun fetPokemonList(): Flow<PagingData<PokemonDB>> {
        return Pager(
            PagingConfig(
                enablePlaceholders = false,
                initialLoadSize = NETWORK_INIT_LOAD_SIZE,
                pageSize = NETWORK_PAGE_SIZE

            ),
            remoteMediator = PokemonMediator(appDatabase, pokemonClient)
        ) {
            appDatabase.pokemonDao().getPokemonList()
        }.flow

    }
}

@ExperimentalPagingApi
// Glori: get data from the server and then dump it into the database
class PokemonMediator constructor(
    val database: AppDatabase,
    private val pokemonClient: PokemonClient
) : RemoteMediator<Int, PokemonDB>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonDB>
    ): MediatorResult {
        return try {
            // Glori: key to load init page or next page.
            val offset = when (loadType) {
                LoadType.REFRESH -> "0"
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {// Glori: Get the key to load the next page from the database or notify end of the list
                    val lastItem = state.lastItemOrNull()
                    //End of the list
                    if (lastItem?.nextOffset == null) {
                        return MediatorResult.Success(true)
                    }
                    lastItem.nextOffset
                }
            }
            val response = pokemonClient.fetchPokemonList(
                when (loadType) {
                    LoadType.REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }, offset
            )
            // Glori: key is saved in the database for data to load the next page.
            val nextOffset = response.next?.toUri()?.getQueryParameter("offset")
            val items = response.results.mapToDB(nextOffset)
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.pokemonDao().clear()
                }
                database.pokemonDao().insertPokemonList(items)
            }
            MediatorResult.Success(
                endOfPaginationReached = items.isEmpty()
            )
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }
}