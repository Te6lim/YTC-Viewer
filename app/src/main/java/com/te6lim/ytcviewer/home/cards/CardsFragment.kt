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
import com.te6lim.ytcviewer.filters.FilterSelectionViewModel
import com.te6lim.ytcviewer.home.HomeViewModel
import com.te6lim.ytcviewer.network.NetworkStatus

class CardsFragment : Fragment() {

    private lateinit var cardsViewModel: CardsViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentCardsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_cards, container, false)

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        cardsViewModel = ViewModelProvider(this)[CardsViewModel::class.java]

        binding.viewModel = cardsViewModel
        binding.lifecycleOwner = this

        val adapter = CardListAdapter()
        binding.cards.adapter = adapter

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
                                    unMarkAllChips()
                                } else {
                                    checkedCategories.value?.let {
                                        if (it.containsKey(
                                                FilterSelectionViewModel
                                                    .CardFilterCategory.Spell.name
                                            ) ||
                                            it.containsKey(
                                                FilterSelectionViewModel
                                                    .CardFilterCategory.Trap.name
                                            )
                                        ) {
                                            unMarkAllChips()
                                        }
                                    }
                                }
                                addCategoryToChecked(category.name)
                                homeViewModel.setChipChecked(category.name)
                            } else unMarkChip(category.name)
                        }
                    }

                binding.cardFilter.addView(chip)
            }

            networkStatus.observe(viewLifecycleOwner) { status ->
                when (status) {
                    NetworkStatus.DONE -> {
                        with(binding) {
                            searchDescription.visibility = View.GONE
                            networkErrorScreen.visibility = View.GONE
                            unknownQueryScreen.visibility = View.GONE
                            loadingScreen.visibility = View.GONE
                            infoScreen.visibility = View.GONE
                            cards.visibility = View.VISIBLE
                        }
                    }

                    NetworkStatus.LOADING -> {
                        with(binding) {
                            searchDescription.visibility = View.GONE
                            networkErrorScreen.visibility = View.GONE
                            unknownQueryScreen.visibility = View.GONE
                            loadingScreen.visibility = View.VISIBLE
                            infoScreen.visibility = View.VISIBLE
                            cards.visibility = View.GONE
                        }
                    }

                    NetworkStatus.ERROR -> {
                        with(binding) {
                            searchDescription.visibility = View.GONE
                            networkErrorScreen.visibility = View.VISIBLE
                            unknownQueryScreen.visibility = View.GONE
                            loadingScreen.visibility = View.GONE
                            infoScreen.visibility = View.VISIBLE
                            cards.visibility = View.GONE
                        }
                    }
                    else -> {
                    }
                }
            }

            cards.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }
        }

        with(homeViewModel) {
            searchBarClicked.observe(viewLifecycleOwner) { isClicked ->
                with(binding.cardFilter) {
                    visibility = if (isClicked) View.VISIBLE else View.GONE
                }
            }

            filterList.observe(viewLifecycleOwner) { list ->
                list?.let { filterList ->
                    if (list.isNotEmpty()) {
                        cardsViewModel.setSelectedFilter(filterList)
                        cardsViewModel.getProperties()
                        setFilterList(listOf())
                    }
                } ?: unMarkChip(cardsViewModel.lastChecked!!)
            }

            searchKey.observe(viewLifecycleOwner) {
                cardsViewModel.getPropertiesWithSearch(it)
            }
        }

        return binding.root
    }

    private fun unMarkChip(category: String) {
        cardsViewModel.removeCategoryFromChecked(category)
        binding.cardFilter.findViewWithTag<Chip>(category).isChecked = false
    }

    private fun unMarkAllChips() {
        cardsViewModel.removeAllCheckedCategory()
        binding.cardFilter.clearCheck()

    }
}