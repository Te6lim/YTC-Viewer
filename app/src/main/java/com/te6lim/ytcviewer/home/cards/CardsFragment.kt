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
        val cardsViewModel = ViewModelProvider(requireActivity())[CardsViewModel::class.java]

        binding.viewModel = cardsViewModel
        binding.lifecycleOwner = this

        with(homeViewModel) {
            searchBarClicked.observe(viewLifecycleOwner) { isClicked ->
                with(binding.cardFilter) {
                    visibility = if (isClicked) View.VISIBLE
                    else View.GONE
                }
            }
        }

        with(cardsViewModel) {

            checkedMonsterCategories.observe(viewLifecycleOwner) {
                it.forEach { item ->
                    binding.monsterFilter.findViewWithTag<Chip>(item.key).isChecked = true
                }
            }

            checkedNonMonsterCategories.observe(viewLifecycleOwner) {
                it.forEach { item ->
                    binding.cardFilter.findViewWithTag<Chip>(item.key).isChecked = true
                }
            }

            val chipInflater = LayoutInflater.from(binding.cardFilter.context)
            FilterSelectionViewModel.CardFilterCategory.values().forEach { category ->
                val chip = chipInflater
                    .inflate(R.layout.filter_selection, binding.monsterFilter, false)
                    .apply {
                        this as Chip
                        tag = category.name
                        text = category.name

                        setOnClickListener {
                            it as Chip
                            if (isChecked) {
                                homeViewModel.setChipChecked(
                                    Pair(
                                        FilterSelectionViewModel.CardFilterType.Monster.name,
                                        category.name
                                    )
                                )

                                binding.cardFilter.clearCheck()

                                removeAllCheckedNonMonsterCategory()

                                addMonsterCategoryToChecked(category.name)
                            } else removeMonsterCategoryFromChecked(category.name)
                        }
                    }

                binding.monsterFilter.addView(chip)
            }

            FilterSelectionViewModel.NonMonsterCardFilterCategory.values().forEach { category ->
                val chip = chipInflater
                    .inflate(
                        R.layout.filter_selection, binding.cardFilter, false
                    ).apply {
                        this as Chip
                        tag = category.name
                        text = category.name

                        setOnClickListener {
                            (it as Chip)
                            if (isChecked) {
                                homeViewModel.setChipChecked(
                                    Pair(
                                        FilterSelectionViewModel.CardFilterType.NonMonster.name,
                                        category.name
                                    )
                                )

                                binding.monsterFilter.clearCheck()

                                removeAllCheckedMonsterCategory()

                                addNonMonsterCategoryToChecked(category.name)
                            } else removeNonMonsterCategoryFromChecked(category.name)
                        }
                    }

                binding.cardFilter.addView(chip)
            }
        }

        return binding.root
    }
}