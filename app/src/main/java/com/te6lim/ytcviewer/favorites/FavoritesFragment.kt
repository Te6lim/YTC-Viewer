package com.te6lim.ytcviewer.favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.YTCApplication
import com.te6lim.ytcviewer.cardDetails.CardDetailsActivity
import com.te6lim.ytcviewer.cardList.MainActivity
import com.te6lim.ytcviewer.databinding.FragmentFavoritesBinding
import com.te6lim.ytcviewer.resources.cardDetailsActivityIntentCardKey

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val repository = (requireActivity().application as YTCApplication).repository

        val viewModel = ViewModelProvider(
            this, FavoritesViewModelProvider(repository)
        )[FavoritesViewModel::class.java]

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorites, container, false
        )

        binding.lifecycleOwner = this

        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbar)


        binding.favoritesList.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL).apply {
                this.setDrawable(
                    ContextCompat.getDrawable(
                        requireContext(), R.drawable.recycler_view_horizontal_separator
                    )!!
                )
            }
        )
        val adapter = FavoritesListAdapter {
            val intent = Intent(requireContext(), CardDetailsActivity::class.java)
            intent.putExtra(cardDetailsActivityIntentCardKey, it)
            startActivity(intent)
        }

        binding.favoritesList.adapter = adapter

        binding.searchFavorites.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.setSearchKey(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.setSearchKey(it) }
                return true
            }
        })

        viewModel.favorites.observe(viewLifecycleOwner) {
            it?.let { adapter.submitList(it) }
        }

        return binding.root
    }
}