package com.te6lim.ytcviewer.home.cards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.YTCApplication
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.databinding.FragmentCardsBinding
import com.te6lim.ytcviewer.filters.CardFilter
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.filters.FilterSelectionActivity
import com.te6lim.ytcviewer.filters.FilterSelectionActivity.Companion.FILTER_LIST_RESULT_KEY
import com.te6lim.ytcviewer.home.HomeBottomSheetFragment
import com.te6lim.ytcviewer.home.MainActivity
import com.te6lim.ytcviewer.home.SortItem
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CardsFragment : Fragment() {

    private lateinit var cardsViewModel: CardsViewModel
    private lateinit var binding: FragmentCardsBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    companion object {
        const val FILTER_CATEGORY = "filter_category"
        private const val CHIP_GROUP_VISIBILITY = "chip group visibility"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cards, container, false
        )

        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbar)

        savedInstanceState?.getInt(CHIP_GROUP_VISIBILITY)?.let {
            binding.cardFilter.visibility = it
        }

        resultLauncher = getActivityResultLauncher(object : Callback {

            override fun onResultOK(filterCategory: CardFilterCategory, list: List<CardFilter>) {
                cardsViewModel.addFiltersToSelected(filterCategory, list)
            }

        })

        cardsViewModel = ViewModelProvider(
            this, CardsViewModelFactory(CardDatabase.getInstance(requireContext()))
        )[CardsViewModel::class.java]

        with(binding) {
            viewModel = cardsViewModel
            lifecycleOwner = this@CardsFragment

            val adapter = CardListAdapter()
            cards.adapter = adapter

            val chipInflater = LayoutInflater.from(cardFilter.context)
            buildChipsIntoChipGroup(chipInflater)

            searchBar.setClickListener { cardFilter.visibility = View.VISIBLE }

            searchBar.setOnCloseListener {
                binding.cardFilter.visibility = View.GONE
                false
            }

            with(cardsViewModel) {
                selectedChips.observe(viewLifecycleOwner) {
                    for (k in it.keys) {
                        cardFilter.findViewWithTag<Chip>(k).isChecked = it[k]!!
                    }
                }

                filters.observe(viewLifecycleOwner) { pagingDataFlow ->
                    lifecycleScope.launch { pagingDataFlow.collectLatest { adapter.submitData(it) } }
                }
            }

            return root
        }
    }

    private fun getActivityResultLauncher(callback: Callback) = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            callback.onResultOK(
                CardFilterCategory.get(
                    result.data?.getStringExtra(FilterSelectionActivity.CATEGORY_RESULT_KEY)!!
                ), result.data?.getParcelableArrayExtra(FILTER_LIST_RESULT_KEY)!!.toList().map {
                    it as CardFilter
                }
            )
        } else {
            val chipName = result.data!!.getStringExtra(FILTER_LIST_RESULT_KEY)!!
            cardsViewModel.switchChip(chipName, false)
        }
    }

    private fun SearchView.setClickListener(action: () -> Unit) {
        setOnClickListener {
            isIconified = false
        }
        setOnSearchClickListener {
            action()
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
                        if (cardsViewModel.toggleChip(category.name))
                            navigateToActivityForResult(FilterSelectionActivity::class.java, category.name)
                        else cardsViewModel.switchChip(category.name, false)
                    }
                }

            binding.cardFilter.addView(chip)
        }
    }

    private fun navigateToActivityForResult(activityClass: Class<out Activity>, filterCategory: String) {
        val intent = Intent(this.context, activityClass)
        intent.putExtra(FILTER_CATEGORY, filterCategory)
        resultLauncher.launch(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.theme_selection -> {
                switchTheme()
                true
            }

            R.id.sort -> {
                val bottomSheet = HomeBottomSheetFragment(object : HomeBottomSheetFragment.Communicator {
                    override fun setSortMethod(sort: SortItem) {
                        cardsViewModel.sortMethod = sort
                    }

                    override fun getSortMethod(): SortItem? {
                        return cardsViewModel.sortMethod
                    }
                })
                bottomSheet.show(requireActivity().supportFragmentManager, HomeBottomSheetFragment.TAG)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun switchTheme() {
        val app = (requireActivity().application as YTCApplication)
        if (app.toDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            app.toDarkMode = false
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            app.toDarkMode = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CHIP_GROUP_VISIBILITY, binding.cardFilter.visibility)
    }

    private interface Callback {
        fun onResultOK(filterCategory: CardFilterCategory, list: List<CardFilter>)
    }
}