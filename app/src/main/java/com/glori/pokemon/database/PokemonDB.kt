package com.glori.pokemon.database

import androidx.core.net.toUri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glori.pokemon.model.Pokemon

@Entity(tableName = "pokemon")
data class PokemonDB(
    val name: String,
    @PrimaryKey
    val url: String,
    val nextOffset: String?
)


fun List<Pokemon>.mapToDB(offset: String?): List<PokemonDB> {
    return map {
        val index = it.url.toUri().lastPathSegment ?: "25"
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
        PokemonDB(name = it.name, url = imageUrl, nextOffset = offset)
    }
}