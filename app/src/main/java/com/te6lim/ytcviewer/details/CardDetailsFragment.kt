package com.te6lim.ytcviewer.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.te6lim.ytcviewer.R

class CardDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_details_card, container, false)

        val toolBar = rootView.findViewById<MaterialToolbar>(R.id.cardDetailsToolBar)

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)

        toolBar.setupWithNavController(findNavController())

        return rootView
    }
}