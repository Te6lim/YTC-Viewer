package com.te6lim.ytcviewer.home.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

        (requireActivity() as AppCompatActivity).setSupportActionBar(
            binding.filterToolbar.apply { setupWithNavController(findNavController()) }
        )

        val adapter = FilterSelectionAdapter(CardFilterCallBack {
            ContextCompat.getColor(
                requireContext(),
                HomeViewModel.getFilterBackgrounds(HomeViewModel.CardFilter.Type)[it]!!
            )
        }).apply {

            submitList(HomeViewModel.getFilerSelections(HomeViewModel.CardFilter.Type))

        }

        binding.filterList?.adapter = adapter

        return binding.root
    }
}