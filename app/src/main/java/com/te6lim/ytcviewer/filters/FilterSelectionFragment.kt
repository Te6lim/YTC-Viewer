package com.te6lim.ytcviewer.filters

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentFilterOptionsBinding

class FilterSelectionFragment : Fragment() {

    companion object {
        const val FILTER_LIST_KEY = "F"
    }

    private lateinit var viewModel: FilterSelectionViewModel
    private var menuItem: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFilterOptionsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_filter_options, container, false
        )

        setHasOptionsMenu(true)

        val category = FilterSelectionFragmentArgs.fromBundle(
            requireArguments()
        ).categoryName

        viewModel = ViewModelProvider(
            this, FilterSelectionViewModelFactory(category)
        )[FilterSelectionViewModel::class.java]

        (requireActivity() as AppCompatActivity).setSupportActionBar(
            (binding.filterToolbar as Toolbar).apply {
                setupWithNavController(findNavController())
                title = category
                navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_close)
                navigationContentDescription = context.getString(R.string.close)
            }
        )

        val adapter = FilterSelectionAdapter(object : CardFilterCallback() {
            override fun getColor(filter: CardFilter): Int = ContextCompat.getColor(
                requireContext(),
                viewModel.getBackgroundsForFilters()[filter.name]!!
            )

            override fun setSelectedCardFilter(filter: CardFilter) {
                viewModel.selectedFilters.add(filter.name)
                menuItem?.let {
                    if (!it.isVisible) it.isVisible = true
                }
            }

            override fun unSelectCardFilter(filter: CardFilter) {
                viewModel.selectedFilters.remove(filter.name)
                if (viewModel.selectedFilters.isEmpty()) {
                    menuItem?.let {
                        if (it.isVisible) it.isVisible = false
                    }
                }
            }

        })

        binding.filterList.adapter = adapter

        viewModel.filterList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_selection_menu, menu)
        menuItem = menu.findItem(R.id.done)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.done -> {
                with(findNavController()) {
                    previousBackStackEntry?.savedStateHandle?.set(
                        FILTER_LIST_KEY, viewModel.selectedFilters
                    )
                    requireActivity().onBackPressed()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}