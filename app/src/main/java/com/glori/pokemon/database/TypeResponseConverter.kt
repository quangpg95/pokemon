package com.glori.pokemon.database

import androidx.room.TypeConverter
import com.glori.pokemon.model.PokemonDetailResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

object TypeResponseConverter {
    private val moshi = Moshi.Builder().build()

    @JvmStatic
    @TypeConverter
    fun fromString(value: String): List<PokemonDetailResponse.TypeResponse>? {
        val listType = Types.newParameterizedType(
            List::class.java,
            PokemonDetailResponse.TypeResponse::class.java
        )
        val adapter: JsonAdapter<List<PokemonDetailResponse.TypeResponse>> = moshi.adapter(listType)
        return adapter.fromJson(value)
    }

    @JvmStatic
    @TypeConverter
    fun fromInfoType(type: List<PokemonDetailResponse.TypeResponse>?): String {
        val listType = Types.newParameterizedType(
            List::class.java,
            PokemonDetailResponse.TypeResponse::class.java
        )
        val adapter: JsonAdapter<List<PokemonDetailResponse.TypeResponse>> = moshi.adapter(listType)
        return adapter.toJson(type)
    }
}