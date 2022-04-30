package com.te6lim.ytcviewer.home.collections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.te6lim.ytcviewer.MainActivity
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentCollectionBinding
import com.te6lim.ytcviewer.home.HomeFragmentDirections

class CollectionFragment : Fragment() {

    private lateinit var binding: FragmentCollectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_collection, container, false
        )

        (requireActivity() as MainActivity).setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(findNavController())

        binding.button.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToCardDetailsFragment()
            )
        }

        return binding.root
    }
}