package com.glori.pokemon.data

import com.glori.pokemon.model.State
import kotlinx.coroutines.flow.*
import retrofit2.Response

/**
 * A repository which provides resource from local database as well as remote end point.
 *
 * [RESULT] represents the type for database.
 * [REQUEST] represents the type for network.
 */
abstract class NetworkBoundRepository<RESULT, REQUEST> {

    fun asFlow() = flow<State<RESULT>> {
        // Emit Loading State
        emit(State.Loading)

        // Emit Database content first
        emit(State.Success(fetchFromLocal().first()))

        // Fetch latest posts from remote
        val apiResponse = fetchFromRemote()

        // Parse body
        val remotePosts = apiResponse.body()

        // Check for response validation
        if (apiResponse.isSuccessful && remotePosts != null) {
            // Save data into the persistence storage
            saveRemoteData(remotePosts)
        } else {
            // Something went wrong! Emit Error state.
            emit(State.Error(apiResponse.message()))
        }

        // Retrieve posts from persistence storage and emit
        emitAll(
            fetchFromLocal().map {
                State.Success(it)
            }
        )

    }.catch { e ->
        // Exception occurred! Emit error
        emit(State.Error(e.message ?: ""))
    }

    protected abstract suspend fun saveRemoteData(response: REQUEST)

    protected abstract fun fetchFromLocal(): Flow<RESULT>

    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>

}