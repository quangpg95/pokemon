package com.glori.pokemon.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PokemonDB::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokemonDao(): PokemonDao
}