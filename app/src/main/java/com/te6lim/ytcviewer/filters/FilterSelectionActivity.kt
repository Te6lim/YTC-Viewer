package com.te6lim.ytcviewer.filters

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ActivitySelectionFilterBinding
import com.te6lim.ytcviewer.home.cards.CardsFragment.Companion.FILTER_CATEGORY

class FilterSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectionFilterBinding
    private lateinit var viewModel: FilterSelectionViewModel
    private var menu: Menu? = null

    companion object {
        const val FILTER_LIST_RESULT_KEY = "result key"
        const val CATEGORY_RESULT_KEY = "category result key"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_selection_filter)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationIcon(R.drawable.ic_close)

        viewModel = ViewModelProvider(
            this, FilterSelectionViewModelFactory(
                intent.getStringExtra(FILTER_CATEGORY)!!
            )
        )[FilterSelectionViewModel::class.java]

        intent.removeExtra(FILTER_CATEGORY)

        val adapter = FilterSelectionAdapter(object : CardFilterCallback() {
            override fun getColor(filter: CardFilter): Int {
                return getFilterColorResource(filter)
            }

            override fun setSelectedCardFilter(filter: CardFilter) {
                if (!viewModel.selectedFilters().contains(filter)) {
                    viewModel.addFilterToSelected(filter)
                }
                if (viewModel.selectedFilters().size == 1) {
                    setMenuVisibility(R.id.done, true)
                    menu?.findItem(R.id.done)?.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                }
            }

            override fun unSelectCardFilter(filter: CardFilter) {
                viewModel.removeFilterFromSelected(filter)
                if (viewModel.selectedFilters().isEmpty()) setMenuVisibility(R.id.done, false)
            }

        })

        binding.filterList.adapter = adapter

        viewModel.filters.observe(this) { adapter.submit(it) }

        viewModel.filterCategory.observe(this) {
            setSupportActionBar(binding.toolbar.apply { title = it })
        }

    }

    private fun setMenuVisibility(itemId: Int, makeVisible: Boolean) {
        menu?.findItem(itemId)?.isVisible = makeVisible
    }

    private fun getFilterColorResource(filter: CardFilter) = ContextCompat.getColor(
        this@FilterSelectionActivity, viewModel.getBackgroundsForFilters()[filter.name]!!
    )

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.filter_selection_menu, menu)
        if (viewModel.selectedFilters().isNotEmpty()) {
            menu?.findItem(R.id.done)?.apply {
                isVisible = true
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            }

        }
        this.menu = menu!!
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent()
        return when (item.itemId) {
            android.R.id.home -> {
                intent.putExtra(FILTER_LIST_RESULT_KEY, viewModel.filterCategory.value)
                setResult(RESULT_CANCELED, intent)
                onBackPressed()
                true
            }

            R.id.done -> {
                with(viewModel.selectedFilters()) {
                    if (size > 0) {
                        intent.apply {
                            putExtra(FILTER_LIST_RESULT_KEY, viewModel.selectedFilters().toTypedArray())
                            putExtra(CATEGORY_RESULT_KEY, viewModel.filterCategory.value)
                        }
                        setResult(RESULT_OK, intent)
                    } else {
                        intent.putExtra(FILTER_LIST_RESULT_KEY, viewModel.filterCategory.value)
                        setResult(RESULT_CANCELED, intent)
                    }
                }
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}