package com.te6lim.ytcviewer.cardList

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.chip.Chip
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.YTCApplication
import com.te6lim.ytcviewer.cardDetails.CardDetailsActivity
import com.te6lim.ytcviewer.cardFilters.CardFilter
import com.te6lim.ytcviewer.cardFilters.FilterSelectionActivity
import com.te6lim.ytcviewer.cardFilters.FilterSelectionActivity.Companion.FILTER_LIST_RESULT_KEY
import com.te6lim.ytcviewer.databinding.FragmentCardsBinding
import com.te6lim.ytcviewer.model.SortType
import com.te6lim.ytcviewer.model.UiItem
import com.te6lim.ytcviewer.network.NetworkStatus
import com.te6lim.ytcviewer.repository.CardRepository
import com.te6lim.ytcviewer.resources.CardFilterCategory
import com.te6lim.ytcviewer.resources.cardDetailsActivityIntentCardKey
import com.te6lim.ytcviewer.sort.HomeBottomSheetFragment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CardListFragment : Fragment() {

    private lateinit var cardsViewModel: CardsViewModel
    private lateinit var binding: FragmentCardsBinding
    private lateinit var adapter: CardListAdapter

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    private lateinit var loadState: LoadState

    private lateinit var repository: CardRepository

    private lateinit var animator: ObjectAnimator

    companion object {
        const val FILTER_CATEGORY = "filter_category"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_cards, container, false
        )

        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbar)

        animator = ObjectAnimator.ofFloat(
            binding.networkStatusIndicator, View.ALPHA, 1f
        ).apply {
            repeatMode = ObjectAnimator.REVERSE
            duration = 800
        }

        resultLauncher = getActivityResultLauncher(object : Callback {
            override fun onResultOK(filterCategory: CardFilterCategory, list: List<CardFilter>) {
                cardsViewModel.addFiltersToSelected(filterCategory, list)
            }
        })

        repository = (requireActivity().application as YTCApplication).repository

        cardsViewModel = ViewModelProvider(
            this, CardsViewModelFactory(repository)
        )[CardsViewModel::class.java]

        if (cardsViewModel.cardFilterIsVisible) binding.cardFilter.visibility = View.VISIBLE
        else binding.cardFilter.visibility = View.GONE

        with(binding) {
            lifecycleOwner = this@CardListFragment

            adapter = CardListAdapter {
                cardsViewModel.setSelectedCard(it)
            }
            cards.adapter = adapter.withLoadStateFooter(CardDataLoadStateAdapter {
                repository.resetLoadCount()
                adapter.retry()
            })

            loadState = LoadState.NotLoading(false)

            (cards.layoutManager as GridLayoutManager).changeSpanSizeOnPagingState(loadState)

            buildChipsIntoChipGroup(LayoutInflater.from(cardFilter.context))

            setListeners()

            setupViewModelObservers()

            return root
        }
    }

    private fun GridLayoutManager.changeSpanSizeOnPagingState(state: LoadState) {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                when (position) {
                    adapter.itemCount -> {
                        if (state is LoadState.Error || state is LoadState.NotLoading) return spanCount
                        return 1
                    }
                    else -> return 1
                }
            }
        }
    }

    private fun FragmentCardsBinding.setListeners() {
        searchBar.setClickListener {
            cardFilter.visibility = View.VISIBLE
            cardsViewModel.cardFilterIsVisible = true
        }

        searchBar.setOnCloseListener {
            cardFilter.visibility = View.GONE
            cardsViewModel.cardFilterIsVisible = false
            false
        }

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(string: String?): Boolean {
                cardsViewModel.deselectAllSelectedCategories()
                string?.let { if (it.isNotEmpty()) cardsViewModel.setSearchKey(it) }
                searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(string: String?): Boolean {
                return true
            }

        })

        reconnectButton.setOnClickListener {
            repository.resetLoadCount()
            adapter.retry()
        }

        adapter.addLoadStateListener { loadStates ->
            loadState = loadStates.source.append
        }
    }

    private fun ObjectAnimator.startAnimation() {
        end()
        start()
    }

    private fun ObjectAnimator.reverseAnimation() {
        end()
        reverse()
    }

    private fun setupViewModelObservers() {
        with(cardsViewModel) {
            categories.observe(viewLifecycleOwner) {
                for (k in it.keys) {
                    binding.cardFilter.findViewWithTag<Chip>(k).isChecked = it[k]!!
                }
            }

            cardsViewModel.cards.observe(viewLifecycleOwner) { pagingDataFlow ->
                lifecycleScope.launch { submitDataFlow(pagingDataFlow) }
            }

            repository.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
                if (isEmpty) {
                    binding.searchDescription.visibility = View.VISIBLE
                    binding.cards.visibility = View.GONE
                } else {
                    binding.searchDescription.visibility = View.GONE
                    binding.cards.visibility = View.VISIBLE
                }
            }

            selectedCard.observe(viewLifecycleOwner) {
                it?.let {
                    val intent = Intent(this@CardListFragment.context, CardDetailsActivity::class.java)
                    intent.putExtra(cardDetailsActivityIntentCardKey, it)
                    startActivity(intent)
                    setSelectedCard(null)
                }
            }

            repository.connectionStatus.observe(viewLifecycleOwner) {
                when (it) {
                    NetworkStatus.LOADING -> {
                        if (binding.searchDescription.isVisible) {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.reconnectButton.visibility = View.GONE
                        }
                        binding.networkStatusImage.setImageResource(R.drawable.ic_connecting)
                        binding.networkStatusText.text = "connecting..."
                        animator.startAnimation()
                    }

                    NetworkStatus.ERROR -> {
                        if (binding.searchDescription.isVisible) {
                            binding.progressBar.visibility = View.GONE
                            binding.reconnectButton.visibility = View.VISIBLE
                        }
                        binding.networkStatusImage.setImageResource(R.drawable.ic_connection_error)
                        binding.networkStatusText.text = "connection error"
                        animator.apply { repeatCount = 0 }.startAnimation()
                    }

                    NetworkStatus.DONE -> {
                        if (binding.networkStatusIndicator.alpha == 1f)
                            animator.apply { repeatCount = 0 }.reverseAnimation()
                    }
                    else -> {
                    }
                }
            }

        }
    }

    private suspend fun submitDataFlow(pagingDataFlow: Flow<PagingData<UiItem>>) {
        lifecycleScope.launch {
            pagingDataFlow.collectLatest { adapter.submitData(it) }
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
            val chipName = result.data?.getStringExtra(FILTER_LIST_RESULT_KEY)!!
            cardsViewModel.switchChip(chipName, false)
        }
    }

    private fun SearchView.setClickListener(action: () -> Unit) {
        setOnClickListener { isIconified = false }
        setOnSearchClickListener { action() }
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
                        if (cardsViewModel.toggleCategory(category.name))
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
                    override fun setSortMethod(sort: SortType) {
                        cardsViewModel.setSortType(sort)
                    }

                    override fun getSortMethod(): SortType {
                        return cardsViewModel.getSortType()
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

    private interface Callback {
        fun onResultOK(filterCategory: CardFilterCategory, list: List<CardFilter>)
    }
}