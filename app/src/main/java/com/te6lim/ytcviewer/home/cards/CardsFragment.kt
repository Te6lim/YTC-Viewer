package com.te6lim.ytcviewer.home.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.chip.Chip
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.databinding.FragmentCardsBinding
import com.te6lim.ytcviewer.filters.CardFilterCategory

class CardsFragment : Fragment() {

    private lateinit var cardsViewModel: CardsViewModel
    private lateinit var binding: FragmentCardsBinding

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_cards, container, false)

        cardsViewModel = ViewModelProvider(
            this, CardsViewModelFactory(
                CardDatabase.getInstance(requireContext())
            )
        )[CardsViewModel::class.java]

        binding.viewModel = cardsViewModel
        binding.lifecycleOwner = this

        val adapter = CardListAdapter()
        binding.cards.adapter = adapter

        val chipInflater = LayoutInflater.from(binding.cardFilter.context)
        CardFilterCategory.values().forEach { category ->
            val chip = chipInflater
                .inflate(R.layout.filter_selection, binding.cardFilter, false)
                .apply {
                    this as Chip
                    tag = category.name
                    text = category.name
                }

            binding.cardFilter.addView(chip)
        }

        return binding.root
    }
}