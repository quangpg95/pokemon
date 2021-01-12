package com.glori.pokemon.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.glori.pokemon.databinding.ItemPokemonBinding
import com.glori.pokemon.domain.PokemonUI

const val ITEM_TYPE = 1
const val HEADER_FOOTER_TYPE = 2//loading type

class PokemonAdapter(private val clickListener: OnPokemonClickListener) :
    PagingDataAdapter<PokemonUI, PokemonViewHolder>(POKEMON_COMPARATOR) {
    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder.create(parent, clickListener)
    }

    // Glori: Intended to be used to display the loading progress bar in the center of the screen
    // when loading more item. If not, loading progress bar display in left of the screen
    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) {
            ITEM_TYPE
        } else {
            HEADER_FOOTER_TYPE
        }
    }

    companion object {
        private val POKEMON_COMPARATOR = object : DiffUtil.ItemCallback<PokemonUI>() {
            override fun areContentsTheSame(oldItem: PokemonUI, newItem: PokemonUI): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: PokemonUI, newItem: PokemonUI): Boolean {
                return oldItem.imageUrl == newItem.imageUrl
            }
        }
    }
}

class OnPokemonClickListener(private val clickListener: (pokemon: PokemonUI) -> Unit) {
    fun onClick(pokemon: PokemonUI) {
        clickListener(pokemon)
    }
}

class PokemonViewHolder(
    private val binding: ItemPokemonBinding,
    clickListener: OnPokemonClickListener
) :
    RecyclerView.ViewHolder(binding.root) {
    init {
        binding.clickListener = clickListener
    }

    fun bind(pokemon: PokemonUI?) {
        pokemon?.let {
            binding.pokemon = it
            binding.executePendingBindings()
        }
    }

    companion object {
        fun create(parent: ViewGroup, clickListener: OnPokemonClickListener) =
            PokemonViewHolder(
                ItemPokemonBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                clickListener
            )
    }
}