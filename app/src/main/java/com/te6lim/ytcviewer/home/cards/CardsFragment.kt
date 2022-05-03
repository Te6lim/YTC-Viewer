package com.te6lim.ytcviewer.home.cards

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import com.google.android.material.chip.Chip
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.YTCApplication
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.databinding.FragmentCardsBinding
import com.te6lim.ytcviewer.filters.CardFilterCategory
import com.te6lim.ytcviewer.filters.FilterSelectionActivity
import com.te6lim.ytcviewer.filters.FilterSelectionActivity.Companion.RESULT_KEY
import com.te6lim.ytcviewer.home.MainActivity

class CardsFragment : Fragment() {

    private lateinit var cardsViewModel: CardsViewModel
    private lateinit var binding: FragmentCardsBinding

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    companion object {
        const val FILTER_CATEGORY = "filter_category"
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cards, container, false
        )

        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbar)

        resultLauncher = getActivityResultLauncher()

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

            cardsViewModel.selectedChips.observe(viewLifecycleOwner) {
                for (k in it.keys) {
                    cardFilter.findViewWithTag<Chip>(k).isChecked = it[k]!!
                }
            }

            return root
        }
    }

    private fun getActivityResultLauncher() = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Toast.makeText(
                requireContext(),
                "RESULT OK: ${result.data?.getStringArrayExtra(RESULT_KEY)?.size}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val data = result.data!!.getStringExtra(RESULT_KEY)!!
            if (cardsViewModel.selectedChips.value!![data]!!) cardsViewModel.switchChip(data, false)
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
                        cardsViewModel.toggleChip(category.name)
                        if (cardsViewModel.selectedChips.value!![category.name]!!)
                            navigateToActivityForResult(FilterSelectionActivity::class.java, category.name)
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
}