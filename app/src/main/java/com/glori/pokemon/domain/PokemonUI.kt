package com.glori.pokemon.domain

import androidx.core.net.toUri
import com.glori.pokemon.database.PokemonDB
import com.glori.pokemon.model.Pokemon

data class PokemonUI(
    val name: String,
    val imageUrl: String
)

fun List<Pokemon>.mapToUI(): List<PokemonUI> {
    return map {
        val index = it.url.toUri().lastPathSegment ?: "25"
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
        PokemonUI(
            name = it.name, imageUrl = imageUrl
        )
    }
}

fun List<PokemonDB>.mapToDomain(): List<PokemonUI> {
    return map {
        PokemonUI(
            name = it.name, imageUrl = it.url
        )
    }
}