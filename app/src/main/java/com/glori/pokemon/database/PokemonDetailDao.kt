package com.glori.pokemon.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemon(pokemonDetailDB: PokemonDetailDB)

    @Query("select * from PokemonDetail where name = :name")
    fun getPokemon(name: String): Flow<PokemonDetailDB>
}