package com.glori.pokemon.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glori.pokemon.databinding.ItemLoadStateBinding

class BaseLoadStateAdapter(val adapter: PagingDataAdapter<*, *>) :
    LoadStateAdapter<BaseLoaderStateViewHolder>() {
    override fun onBindViewHolder(holder: BaseLoaderStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): BaseLoaderStateViewHolder {
        return BaseLoaderStateViewHolder.create(parent, adapter)
    }
}

class BaseLoaderStateViewHolder(
    private val binding: ItemLoadStateBinding,
    private val adapter: PagingDataAdapter<*, *>
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener {
            adapter.retry()
        }
    }

    fun bind(loadState: LoadState) {
        binding.loadState = loadState
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup, adapter: PagingDataAdapter<*, *>): BaseLoaderStateViewHolder {
            return BaseLoaderStateViewHolder(
                ItemLoadStateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                adapter
            )
        }
    }
}