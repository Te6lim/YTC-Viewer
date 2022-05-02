package com.te6lim.ytcviewer.filters

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

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_selection_filter)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationIcon(R.drawable.ic_close)

        val viewModel = ViewModelProvider(
            this, FilterSelectionViewModelFactory(
                intent.getStringExtra(FILTER_CATEGORY) ?: ""
            )
        )[FilterSelectionViewModel::class.java]

        intent.removeExtra(FILTER_CATEGORY)

        val adapter = FilterSelectionAdapter(object : CardFilterCallback() {
            override fun getColor(filter: CardFilter): Int {
                return ContextCompat.getColor(
                    this@FilterSelectionActivity,
                    viewModel.getBackgroundsForFilters()[filter.name]!!
                )
            }

            override fun setSelectedCardFilter(filter: CardFilter) {

            }

            override fun unSelectCardFilter(filter: CardFilter) {

            }

        })

        binding.filterList.adapter = adapter

        viewModel.filters.observe(this) { adapter.submitList(it) }

        viewModel.filterCategory.observe(this) {
            setSupportActionBar(binding.toolbar.apply { title = it })
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.filter_selection_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}