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
            HomeViewModel.CardFilterCategory.values().forEach { filter ->
                val chip = chipInflater
                    .inflate(R.layout.filter_selection, binding.monsterFilter, false)
                    .apply {
                        this as Chip
                        tag = filter.name
                        text = filter.name

                        setOnClickListener {
                            (it as Chip)
                            if (isChecked) {
                                setChipChecked(
                                    Pair(HomeViewModel.CardFilterType.Monster.name, filter.name)
                                )

                                addCategoryToChecked(filter.name)
                            } else removeCategoryFromChecked(filter.name)
                        }
                    }

                binding.monsterFilter.addView(chip)
            }

            HomeViewModel.NonMonsterCardFilterCategory.values().forEach {
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

            checkedCategories.observe(viewLifecycleOwner) {
                it.forEach { item ->
                    binding.monsterFilter.findViewWithTag<Chip>(item.key).isChecked = true
                }
            }
        }

        return binding.root
    }
}