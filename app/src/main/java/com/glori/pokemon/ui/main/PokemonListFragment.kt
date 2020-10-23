package com.glori.pokemon.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.flatMap
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import com.glori.pokemon.databinding.FragmentPokemonListBinding
import com.glori.pokemon.domain.PokemonUI
import com.glori.pokemon.ui.adapter.BaseLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var binding: FragmentPokemonListBinding

    private val adapter: PokemonAdapter = PokemonAdapter(OnPokemonClickListener {
        Toast.makeText(requireContext(), it.name, Toast.LENGTH_SHORT).show()
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonListBinding.inflate(
            inflater,
            container, false
        )
        binding.adapter = adapter.withLoadStateHeaderAndFooter(
            header = BaseLoadStateAdapter {
                adapter.retry()
            },
            footer = BaseLoadStateAdapter {
                adapter.retry()
            }
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            viewModel.pokemonList.collectLatest {
                adapter.submitData(it)
            }
        }
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == HEADER_FOOTER_TYPE) {
                    1
                } else {
                    2
                }
            }
        }
        binding.recyclerView.layoutManager = layoutManager
        binding.btnRetry.setOnClickListener {
            adapter.retry()
        }
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressLoading.isVisible = loadState.refresh is LoadState.Loading
                tvErrorMessage.isVisible = loadState.refresh is LoadState.Error
                btnRetry.isVisible = loadState.refresh is LoadState.Error
                // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
                val errorState = loadState.source.append as? LoadState.Error
                    ?: loadState.source.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error
                    ?: loadState.refresh as? LoadState.Error
                tvErrorMessage.text = errorState?.error?.message ?: ""
            }
        }
    }

}