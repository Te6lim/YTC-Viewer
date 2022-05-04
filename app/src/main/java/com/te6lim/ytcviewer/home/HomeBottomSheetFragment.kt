package com.te6lim.ytcviewer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentSheetBottomBinding

class HomeBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSheetBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sheet_bottom, container, false
        )
        return binding.root
    }
}