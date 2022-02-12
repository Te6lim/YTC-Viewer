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

                searchBarClicked.value?.let { isClicked ->
                    if (isClicked) {
                        toolbar.elevation = resources.getDimension(R.dimen.no_spacing)
                        chipGroupDivider.visibility = View.VISIBLE
                    }
                }

                checkedChipName.observe(viewLifecycleOwner) {
                    if (it != null) {
                        findNavController()
                            .navigate(
                                HomeFragmentDirections
                                    .actionHomeFragmentToFilterSelectionFragment(it)
                            )
                        setChipChecked(null)
                    }
                }

                with(searchBar) {
                    setOnClickListener {
                        (it as SearchView).isIconified = false
                        if (searchBarClicked.value == null || !searchBarClicked.value!!) {
                            setSearchBarClicked(true)
                            toolbar.elevation = resources.getDimension(R.dimen.no_spacing)
                            chipGroupDivider.visibility = View.VISIBLE
                        }
                    }

                    setOnCloseListener {
                        setSearchBarClicked(false)
                        toolbar.elevation = resources.getDimension(R.dimen.small_spacing)
                        chipGroupDivider.visibility = View.GONE
                        false
                    }

                    setOnSearchClickListener {
                        if (searchBarClicked.value == null || !searchBarClicked.value!!) {
                            setSearchBarClicked(true)
                            toolbar.elevation = resources.getDimension(R.dimen.no_spacing)
                            chipGroupDivider.visibility = View.VISIBLE
                        }
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