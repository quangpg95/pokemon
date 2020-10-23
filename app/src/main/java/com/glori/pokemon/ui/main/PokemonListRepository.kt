package com.glori.pokemon.ui.main

import androidx.core.net.toUri
import androidx.paging.*
import androidx.room.withTransaction
import com.glori.pokemon.database.AppDatabase
import com.glori.pokemon.database.PokemonDB
import com.glori.pokemon.database.mapToDB
import com.glori.pokemon.domain.PokemonUI
import com.glori.pokemon.domain.mapToUI
import com.glori.pokemon.network.PokemonClient
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class PokemonListRepository @Inject constructor(
    private val appDatabase: AppDatabase,
    private val pokemonClient: PokemonClient
) {



    fun fetPokemonList(): Flow<PagingData<PokemonUI>> {
        return Pager(
            PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = {
                PokemonPagingSource(pokemonClient)
            }
        ).flow
    }

    fun fetPokemonListFromDB(): Flow<PagingData<PokemonDB>> {
        return Pager(
            PagingConfig(
                pageSize = NETWORK_PAGE_SIZE
            ),
            remoteMediator = PokemonMediator(appDatabase, pokemonClient)
        ) {
            appDatabase.pokemonDao().getPokemonList()
        }.flow

    }
}

private const val NETWORK_PAGE_SIZE = 20
private const val STARTING_PAGE_INDEX = 0


@ExperimentalPagingApi
class PokemonMediator constructor(
    val database: AppDatabase,
    private val pokemonClient: PokemonClient
) : RemoteMediator<Int, PokemonDB>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonDB>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> "0"
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    lastItem.nextOffset
                }
            }
            val response = pokemonClient.fetchPokemonList(state.config.pageSize, offset)
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

class PokemonPagingSource(private val pokemonClient: PokemonClient) :
    PagingSource<Int, PokemonUI>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonUI> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = pokemonClient.fetchPokemonList(params.loadSize, position.toString())
            val pokemonList = response.results.mapToUI()
            LoadResult.Page(
                pokemonList,
                if (position == 0) null else position - params.loadSize,
                if (pokemonList.isEmpty()) null else position + params.loadSize
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}

