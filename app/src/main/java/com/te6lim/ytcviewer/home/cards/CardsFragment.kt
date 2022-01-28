package com.te6lim.ytcviewer.home.cards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.home.HomeViewModel

class CardsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, sacedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        val root = inflater.inflate(R.layout.fragment_cards, container, false)

        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        val testButton = root.findViewById<AppCompatButton>(R.id.test_button)
        testButton.setOnClickListener {
            homeViewModel.setNavigateToDetailScreen(true)
        }
        return root
    }
}