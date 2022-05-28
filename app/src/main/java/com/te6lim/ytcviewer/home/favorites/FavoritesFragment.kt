package com.te6lim.ytcviewer.home.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentCollectionBinding
import com.te6lim.ytcviewer.home.MainActivity

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentCollectionBinding

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

        binding.button.setOnClickListener {

        }

        return binding.root
    }
}