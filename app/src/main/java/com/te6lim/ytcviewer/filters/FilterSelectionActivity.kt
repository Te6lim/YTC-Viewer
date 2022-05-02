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

    companion object {
        const val RESULT_KEY = "result_key"
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_selection_filter)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationIcon(R.drawable.ic_close)

        viewModel = ViewModelProvider(
            this, FilterSelectionViewModelFactory(
                intent.getStringExtra(FILTER_CATEGORY) ?: ""
            )
        )[FilterSelectionViewModel::class.java]

        intent.removeExtra(FILTER_CATEGORY)

        val adapter = FilterSelectionAdapter(object : CardFilterCallback() {
            override fun getColor(filter: CardFilter): Int {
                return getFilterColorResource(filter)
            }

            override fun setSelectedCardFilter(filter: CardFilter) {
                viewModel.addFilterToSelected(filter)
            }

            override fun unSelectCardFilter(filter: CardFilter) {
                viewModel.removeFilterFromSelected(filter)
            }

        })

        binding.filterList.adapter = adapter

        viewModel.filters.observe(this) { adapter.submitList(it) }

        viewModel.filterCategory.observe(this) {
            setSupportActionBar(binding.toolbar.apply { title = it })
        }

    }

    private fun getFilterColorResource(filter: CardFilter) = ContextCompat.getColor(
        this@FilterSelectionActivity,
        viewModel.getBackgroundsForFilters()[filter.name]!!
    )

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.filter_selection_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (viewModel.selectedFilters().isNotEmpty()) {
                    with(viewModel.selectedFilters()) {
                        if (size > 0) {
                            val intent = Intent().apply {
                                putExtra(RESULT_KEY, viewModel.selectedFilters().toTypedArray())
                            }
                            setResult(RESULT_OK, intent)
                        }
                    }
                }
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}