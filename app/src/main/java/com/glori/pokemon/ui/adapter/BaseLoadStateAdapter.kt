package com.glori.pokemon.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.glori.pokemon.databinding.ItemLoadStateBinding

class BaseLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<BaseLoaderStateViewHolder>() {
    override fun onBindViewHolder(holder: BaseLoaderStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): BaseLoaderStateViewHolder {
        return BaseLoaderStateViewHolder.create(parent, retry)
    }
}

class BaseLoaderStateViewHolder(
    private val binding: ItemLoadStateBinding,
    private val retry: () -> Unit
) :
    RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener {
            retry.invoke()
        }
    }

    fun bind(loadState: LoadState) {
        binding.loadState = loadState
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): BaseLoaderStateViewHolder {
            return BaseLoaderStateViewHolder(
                ItemLoadStateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                retry
            )
        }
    }
}