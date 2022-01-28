package com.te6lim.ytcviewer.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    //Test (DarkMode switch)
    private var isDarkMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        homeViewModel.navigateToDetailScreen.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_homeFragment_to_cardDetailsFragment)
                homeViewModel.setNavigateToDetailScreen(false)
            }
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.theme_selection -> {
                //Test (Light and Dark mode activation)
                if (isDarkMode) {
                    item.icon = ContextCompat.getDrawable(
                        requireContext(), R.drawable.baseline_dark_mode_24
                    )
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    item.icon = ContextCompat.getDrawable(
                        requireContext(), R.drawable.baseline_light_mode_24
                    )
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                isDarkMode = !isDarkMode
                true
            }
            else -> false
        }
    }
}