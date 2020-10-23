package com.glori.pokemon.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonList(pokemonList: List<PokemonDB>)

    @Query("SELECT * FROM pokemon")
    fun getPokemonList(): PagingSource<Int, PokemonDB>

    @Query("DELETE FROM pokemon")
    suspend fun clear()
}