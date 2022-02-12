package com.te6lim.ytcviewer.home.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentCardsBinding
import com.te6lim.ytcviewer.home.HomeViewModel

class CardsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, sacedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        val binding = DataBindingUtil
            .inflate<FragmentCardsBinding>(
                inflater, R.layout.fragment_cards, container, false
            )

        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        val cardsViewModel = ViewModelProvider(this)[CardsViewModel::class.java]

        binding.viewModel = cardsViewModel
        binding.lifecycleOwner = this

        with(homeViewModel) {

            val chipInflater = LayoutInflater.from(binding.cardFilter.context)
            HomeViewModel.CardFilter.values().forEach { filter ->
                val chip = chipInflater
                    .inflate(R.layout.filter_selection, binding.monsterFilter, false)
                    .apply {
                        this as Chip
                        tag = filter.name
                        text = filter.name

                        setOnCheckedChangeListener { chip, isChecked ->
                            if (isChecked) {
                                setChipChecked(filter.name)
                                storeCheckedChipId(chip.id)
                            } else {
                                storeCheckedChipId(null)
                            }
                        }
                    }

                binding.monsterFilter.addView(chip)
            }

            HomeViewModel.NonMonsterCardFilter.values().forEach {
                val chip = chipInflater
                    .inflate(
                        R.layout.filter_selection, binding.cardFilter, false
                    )
                    .apply {
                        this as Chip
                        tag = it.name
                        text = it.name
                    }

                binding.cardFilter.addView(chip)
            }

            searchBarClicked.observe(viewLifecycleOwner) { isClicked ->
                with(binding.cardFilter) {
                    visibility = if (isClicked) View.VISIBLE
                    else View.GONE
                }
            }
        }

        return binding.root
    }
}