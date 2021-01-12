package com.glori.pokemon.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "base_experience")
    val baseExperience: Int,
    @Json(name = "height")
    val height: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "types")
    val types: List<TypeResponse>,
    @Json(name = "weight")
    val weight: Int
) {
    @JsonClass(generateAdapter = true)
    data class TypeResponse(
        @Json(name = "type")
        val type: Type
    ) {
        @JsonClass(generateAdapter = true)
        data class Type(
            @Json(name = "name")
            val name: String,
        )
    }
}