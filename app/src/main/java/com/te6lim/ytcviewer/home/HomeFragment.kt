package com.te6lim.ytcviewer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentHomeBinding
import com.te6lim.ytcviewer.home.cards.CardsFragment
import com.te6lim.ytcviewer.home.collections.CollectionFragment

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val cardsFragment = CardsFragment()
        val collectionFragment = CollectionFragment()

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        setFragment(cardsFragment)

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.cardsFragment -> {
                    setFragment(cardsFragment)
                }
                else -> {
                    setFragment(collectionFragment)
                }
            }
            true
        }
        return binding.root
    }

    private fun setFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer, fragment
        ).commit()
    }
}