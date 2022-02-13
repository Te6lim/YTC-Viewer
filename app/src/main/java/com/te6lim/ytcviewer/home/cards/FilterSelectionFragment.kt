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
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentFilterOptionsBinding
import com.te6lim.ytcviewer.home.HomeViewModel

class FilterSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentFilterOptionsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_filter_options, container, false
        )

        val category = FilterSelectionFragmentArgs.fromBundle(requireArguments()).categoryName
        val type = HomeViewModel.CardFilterType.valueOf(
            FilterSelectionFragmentArgs.fromBundle(requireArguments()).typeName
        )

        (requireActivity() as AppCompatActivity).setSupportActionBar(
            (binding.filterToolbar as Toolbar).apply {
                setupWithNavController(findNavController())
                title = category
            }
        )

        val adapter = FilterSelectionAdapter(CardFilterCallback {
            if (type == HomeViewModel.CardFilterType.Monster)
                ContextCompat.getColor(
                    requireContext(),
                    HomeViewModel.getMonsterFilterBackgrounds(
                        HomeViewModel.CardFilterCategory.valueOf(category)
                    )[it.name]!!
                )
            else ContextCompat.getColor(
                requireContext(),
                HomeViewModel.getNonMonsterFilterBackgrounds(
                    HomeViewModel.NonMonsterCardFilterCategory.valueOf(category)
                )[it.name]!!
            )
        }).apply {

            if (type == HomeViewModel.CardFilterType.Monster) submitList(
                HomeViewModel.getMonsterFilter(
                    HomeViewModel.CardFilterCategory.valueOf(
                        category
                    )
                )
            )
            else submitList(
                HomeViewModel.getNonMonsterFilter(
                    HomeViewModel.NonMonsterCardFilterCategory.valueOf(
                        category
                    )
                )
            )

        }

        binding.filterList.adapter = adapter

        return binding.root
    }
}