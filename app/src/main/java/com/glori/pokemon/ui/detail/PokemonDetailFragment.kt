package com.glori.pokemon.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.paging.ExperimentalPagingApi
import com.glori.pokemon.data.State
import com.glori.pokemon.databinding.FragmentPokemonDetailBinding
import com.glori.pokemon.domain.mapToUI
import com.glori.pokemon.ui.custom_view.OnRetryListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class PokemonDetailFragment : Fragment(), OnRetryListener {
    private val viewModel: PokemonDetailViewModel by viewModels()
    private lateinit var binding: FragmentPokemonDetailBinding
    private lateinit var pokemonName: String
    private lateinit var pokemonImageUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pokemonName = PokemonDetailFragmentArgs.fromBundle(requireArguments()).pokemonName
        pokemonImageUrl = PokemonDetailFragmentArgs.fromBundle(requireArguments()).imageUrl

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initData()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageUrl = pokemonImageUrl
        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.viewError.onRetryListener = this
    }

    private fun initData() {
        viewModel.pokemon.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Loading -> {
                    showLoadingState()
                }
                is State.Success -> {
                    if (state.data != null) {
                        isHasData = true
                        showSuccessState()
                        binding.pokemonDetail = state.data.mapToUI()

                    }
                }
                is State.Error -> {
                    if (!isHasData) {
                        showErrorState(state.exception)
                    }
                }
            }
        })
        if (viewModel.pokemon.value !is State.Success) {
            fetchData()
        }
    }

    private fun showLoadingState() {
        binding.progressLoading.isVisible = true
        binding.viewError.isVisible = false
    }

    private fun showSuccessState() {
        binding.progressLoading.isVisible = false
        binding.viewError.isVisible = false
    }

    private fun showErrorState(exception: String) {
        binding.progressLoading.isVisible = false
        binding.viewError.isVisible = true
        binding.viewError.tvMessage.text = exception
    }

    private var isHasData = false
    private fun fetchData() {
        isHasData = false
        viewModel.getPokemon(pokemonName)
    }

    override fun onRetry() {
        fetchData()
    }


}