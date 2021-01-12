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
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.glori.pokemon.databinding.FragmentPokemonListBinding
import com.glori.pokemon.ui.adapter.BaseLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
@ExperimentalPagingApi
class PokemonListFragment : Fragment() {

    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var binding: FragmentPokemonListBinding

    private val pokemonAdapter: PokemonAdapter =
        PokemonAdapter(OnPokemonClickListener { pokemonUI ->
//            Toast.makeText(requireContext(), pokemonUI.name, Toast.LENGTH_SHORT).show()
            findNavController().navigate(
                PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailFragment(
                    pokemonUI.name, pokemonUI.imageUrl
                )
            )
        })

    init {
        lifecycleScope.launchWhenCreated {
            viewModel.pokemonList.collectLatest {
                pokemonAdapter.submitData(it)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(
            inflater,
            container, false
        )
        binding.adapter = pokemonAdapter
            .withLoadStateFooter(
                footer = BaseLoadStateAdapter(pokemonAdapter)
            )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        // Glori: resize span when loading state to progress bar in the center screen
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (pokemonAdapter.getItemViewType(position) == HEADER_FOOTER_TYPE) {
                    1
                } else {
                    2
                }
            }
        }
        binding.recyclerView.layoutManager = layoutManager
        binding.btnRetry.setOnClickListener {
            pokemonAdapter.retry()
        }
        binding.refreshBar.setOnRefreshListener {
            pokemonAdapter.refresh()
        }
        // Glori: handle load state
        pokemonAdapter.addLoadStateListener { loadState ->
            binding.apply {
                progressLoading.isVisible = loadState.refresh is LoadState.Loading
                tvErrorMessage.isVisible =
                    (loadState.refresh is LoadState.Error) && pokemonAdapter.itemCount == 0
                btnRetry.isVisible =
                    (loadState.refresh is LoadState.Error) && pokemonAdapter.itemCount == 0
                refreshBar.isRefreshing = loadState.refresh is LoadState.Loading

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