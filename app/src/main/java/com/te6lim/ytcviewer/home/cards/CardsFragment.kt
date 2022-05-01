package com.te6lim.ytcviewer.home.cards

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.chip.Chip
import com.te6lim.ytcviewer.MainActivity
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

        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbar)

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
        buildChipsIntoChipGroup(chipInflater)

        binding.searchBar.setClickListener {
            binding.cardFilter.visibility = View.VISIBLE
        }

        cardsViewModel.selectedChips.observe(viewLifecycleOwner) {
            for (k in it.keys) {
                binding.cardFilter.findViewWithTag<Chip>(k).isChecked = it[k]!!
            }
        }

        return binding.root
    }

    private fun SearchView.setClickListener(action: () -> Unit) {
        setOnClickListener { (it as SearchView).isIconified = false }

        setOnSearchClickListener { action() }

        setOnCloseListener {
            binding.cardFilter.visibility = View.GONE
            false
        }
    }

    private fun buildChipsIntoChipGroup(chipInflater: LayoutInflater) {
        CardFilterCategory.values().forEach { category ->
            val chip = chipInflater
                .inflate(R.layout.filter_selection, binding.cardFilter, false)
                .apply {
                    this as Chip
                    tag = category.name
                    text = category.name
                    setOnClickListener {
                        cardsViewModel.toggleChip(category.name)
                    }
                }

            binding.cardFilter.addView(chip)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}