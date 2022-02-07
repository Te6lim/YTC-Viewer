package com.te6lim.ytcviewer.home.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentCardsBinding
import com.te6lim.ytcviewer.home.HomeViewModel

class CardsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, sacedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)

        val binding = DataBindingUtil
            .inflate<FragmentCardsBinding>(
                inflater, R.layout.fragment_cards, container, false
            )

        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        val cardsViewModel = ViewModelProvider(this)[CardsViewModel::class.java]

        binding.viewModel = cardsViewModel
        binding.lifecycleOwner = this

        homeViewModel.searchBarClicked.observe(viewLifecycleOwner) { isClicked ->
            with(binding.cardFilter) {
                visibility = if (isClicked) View.VISIBLE
                else View.GONE
            }
        }

        return binding.root
    }
}