package com.te6lim.ytcviewer.home.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFilterOptionsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_filter_options, container, false
        )

        val category = FilterSelectionFragmentArgs.fromBundle(requireArguments()).categoryName
        val type = FilterSelectionFragmentArgs.fromBundle(requireArguments()).typeName

        val viewModel = ViewModelProvider(
            this, FilterSelectionViewModelFactory(category, type)
        )[FilterSelectionViewModel::class.java]

        (requireActivity() as AppCompatActivity).setSupportActionBar(
            (binding.filterToolbar as Toolbar).apply {
                setupWithNavController(findNavController())
                title = category
            }
        )

        val adapter = FilterSelectionAdapter(object : CardFilterCallback() {
            override fun getColor(filter: CardFilter): Int = ContextCompat.getColor(
                requireContext(),
                viewModel.getBackgroundsForFilters()[filter.name]!!
            )

            override fun setSelectedCardFilter(filter: CardFilter?) {
                viewModel.selectedFilter = filter

                with(findNavController()) {
                    previousBackStackEntry?.savedStateHandle?.set("K", filter?.name)
                    requireActivity().onBackPressed()
                }
            }

        })

        binding.filterList.adapter = adapter

        viewModel.filterList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        return binding.root
    }
}