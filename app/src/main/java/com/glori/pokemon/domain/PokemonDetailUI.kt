package com.glori.pokemon.domain

import com.glori.pokemon.database.PokemonDetailDB
import com.glori.pokemon.model.PokemonDetailResponse

/**
 * @Glori detail domain class
 */
data class PokemonDetailUI(
    val id: Int,
    val baseExperience: Int,
    val height: Int,
    val name: String,
    val types: List<PokemonDetailResponse.TypeResponse>,
    val weight: Int
)

fun PokemonDetailDB.mapToUI(): PokemonDetailUI {
    return PokemonDetailUI(
        id = this.id,
        baseExperience = this.baseExperience,
        height = this.height,
        name = this.name,
        types = this.types,
        weight = this.weight
    )
}