package com.glori.pokemon.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.glori.pokemon.model.PokemonDetailResponse

@Entity(tableName = "PokemonDetail")
data class PokemonDetailDB(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "base_experience")
    val baseExperience: Int,
    val height: Int,
    val name: String,
    val types: List<PokemonDetailResponse.TypeResponse>,
    val weight: Int
)


fun PokemonDetailResponse.mapToDB(): PokemonDetailDB {
    return PokemonDetailDB(
        id = this.id,
        baseExperience = this.baseExperience,
        height = this.height,
        name = this.name,
        types = this.types,
        weight = this.weight
    )
}