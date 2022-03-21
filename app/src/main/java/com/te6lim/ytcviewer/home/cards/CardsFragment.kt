package com.te6lim.ytcviewer.home.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.chip.Chip
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.databinding.FragmentCardsBinding
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.home.HomeViewModel
import com.te6lim.ytcviewer.network.NetworkStatus
import kotlinx.coroutines.launch

class CardsFragment : Fragment() {

    private lateinit var cardsViewModel: CardsViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentCardsBinding

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_cards, container, false)

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        cardsViewModel = ViewModelProvider(
            this, CardsViewModelFactory(CardDatabase.getInstance(requireContext()))
        )[CardsViewModel::class.java]

        binding.viewModel = cardsViewModel
        binding.lifecycleOwner = this

        val adapter = CardListAdapter()
        binding.cards.adapter = adapter

        binding.networkErrorScreen.findViewById<Button>(R.id.retry_button)
            .setOnClickListener {
                cardsViewModel.lastSearchQuery?.let {
                    //cardsViewModel.getCardsWithSearch(it)
                } ?: cardsViewModel.getCards()
            }

        with(cardsViewModel) {
            selectedCategories.observe(viewLifecycleOwner) {
                it.forEach { item ->
                    binding.cardFilter.findViewWithTag<Chip>(item.key).isChecked = true
                }
            }

            val chipInflater = LayoutInflater.from(binding.cardFilter.context)
            CardFilterCategory.values().forEach { category ->
                val chip = chipInflater
                    .inflate(R.layout.filter_selection, binding.cardFilter, false)
                    .apply {
                        this as Chip
                        tag = category.name
                        text = category.name

                        setOnClickListener { _ ->

                            if (isChecked) {

                                if (tag == CardFilterCategory.Spell.name ||
                                    tag == CardFilterCategory.Trap.name
                                ) {
                                    unMarkAllChips()
                                } else {
                                    selectedCategories.value?.let {
                                        if (
                                            it.containsKey(CardFilterCategory.Spell.name) ||
                                            it.containsKey(CardFilterCategory.Trap.name)
                                        ) {
                                            unMarkAllChips()
                                        }
                                    }
                                }
                                addToSelectedCategories(category.name)
                                homeViewModel.setChipChecked(category.name)
                            } else unMarkChip(category.name)
                        }
                    }

                binding.cardFilter.addView(chip)
            }

            networkStatus.observe(viewLifecycleOwner) { status ->
                when (status) {
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
                it?.let {
                    /*with(binding) {
                        searchDescription.visibility = View.GONE
                        networkErrorScreen.visibility = View.GONE
                        unknownQueryScreen.visibility = View.GONE
                        loadingScreen.visibility = View.GONE
                        infoScreen.visibility = View.GONE
                        cards.visibility = View.VISIBLE
                    }*/

                    lifecycleScope.launch {
                        adapter.submitData(it)
                    }

                } /*?: run {
                    with(binding) {
                        searchDescription.visibility = View.VISIBLE
                        networkErrorScreen.visibility = View.GONE
                        unknownQueryScreen.visibility = View.GONE
                        loadingScreen.visibility = View.GONE
                        infoScreen.visibility = View.VISIBLE
                        cards.visibility = View.GONE
                    }
                }*/
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
                        setFilterList(listOf())
                    }
                } ?: unMarkChip(cardsViewModel.lastChecked!!)
            }

            searchKey.observe(viewLifecycleOwner) {
                it?.let { searchKey ->
                    unMarkAllChips()
                    //cardsViewModel.getCardsWithSearch(searchKey)
                    setSearchKey(null)
                }
            }
        }

        return binding.root
    }

    private fun unMarkChip(category: String) {
        cardsViewModel.removeCategoryFromSelected(category)
        binding.cardFilter.findViewWithTag<Chip>(category).isChecked = false
    }

    private fun unMarkAllChips() {
        cardsViewModel.removeAllSelectedCategory()
        binding.cardFilter.clearCheck()

    }
}