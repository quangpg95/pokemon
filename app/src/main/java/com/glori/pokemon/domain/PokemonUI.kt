package com.glori.pokemon.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @Glori domain pokemon class
 */
@Parcelize
data class PokemonUI(
    val name: String,
    val imageUrl: String
) : Parcelable