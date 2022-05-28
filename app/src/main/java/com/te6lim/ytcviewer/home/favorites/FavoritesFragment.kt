package com.te6lim.ytcviewer.home.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.database.CardDatabase
import com.te6lim.ytcviewer.databinding.FragmentFavoritesBinding
import com.te6lim.ytcviewer.home.MainActivity

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_favorites, container, false
        )

        binding.lifecycleOwner = this

        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbar)

        val viewModel = ViewModelProvider(
            this, FavoritesViewModelFactory(CardDatabase.getInstance(requireContext()).cardDao)
        )[FavoritesViewModel::class.java]

        binding.viewModel = viewModel

        binding.favoritesList.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )
        val adapter = FavoritesListAdapter()
        binding.favoritesList.adapter = adapter



        return binding.root
    }
}