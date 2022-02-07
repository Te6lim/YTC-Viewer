package com.te6lim.ytcviewer.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.te6lim.ytcviewer.MainActivityViewModel
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        mainActivityViewModel = ViewModelProvider(
            requireActivity()
        )[MainActivityViewModel::class.java]

        with(binding) {

            with(homeViewModel) {
                navigateToDetailScreen.observe(viewLifecycleOwner) {
                    if (it) {
                        findNavController()
                            .navigate(R.id.action_homeFragment_to_cardDetailsFragment)
                        setNavigateToDetailScreen(false)
                    }
                }

                with(searchBar) {
                    setOnClickListener {
                        (it as SearchView).isIconified = false
                        if (searchBarClicked.value == null || !searchBarClicked.value!!)
                            setSearchBarClicked(true)
                    }

                    setOnCloseListener {
                        setSearchBarClicked(false)
                        false
                    }

                    setOnSearchClickListener {
                        if (searchBarClicked.value == null || !searchBarClicked.value!!)
                            setSearchBarClicked(true)
                    }
                }
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
                val isActive = mainActivityViewModel.isDarkThemeActive.value!!
                if (isActive) mainActivityViewModel.setDarkThemeActive(false)
                else mainActivityViewModel.setDarkThemeActive(true)

                true
            }
            else -> false
        }
    }
}