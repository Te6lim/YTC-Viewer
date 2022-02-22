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

            checkedCategories.observe(viewLifecycleOwner) {
                it.forEach { item ->
                    binding.cardFilter.findViewWithTag<Chip>(item.key).isChecked = true
                }
            }

            val chipInflater = LayoutInflater.from(binding.cardFilter.context)
            FilterSelectionViewModel.CardFilterCategory.values().forEach { category ->
                val chip = chipInflater
                    .inflate(R.layout.filter_selection, binding.cardFilter, false)
                    .apply {
                        this as Chip
                        tag = category.name
                        text = category.name

                        setOnClickListener { _ ->

                            if (isChecked) {

                                if (
                                    tag == FilterSelectionViewModel.CardFilterCategory.Spell.name ||
                                    tag == FilterSelectionViewModel.CardFilterCategory.Trap.name
                                ) {
                                    removeAllCheckedCategory()
                                    binding.cardFilter.clearCheck()
                                } else {
                                    checkedCategories.value?.let {
                                        if (it.containsKey(
                                                FilterSelectionViewModel
                                                    .CardFilterCategory.Spell.name
                                            )
                                            || it.containsKey(
                                                FilterSelectionViewModel
                                                    .CardFilterCategory.Trap.name
                                            )
                                        ) {
                                            removeAllCheckedCategory()
                                            binding.cardFilter.clearCheck()
                                        }
                                    }
                                }
                                addCategoryToChecked(category.name)

                                homeViewModel.setChipChecked(category.name)
                            } else removeCategoryFromChecked(category.name)
                        }
                    }

                binding.cardFilter.addView(chip)
            }
        }

        return binding.root
    }
}