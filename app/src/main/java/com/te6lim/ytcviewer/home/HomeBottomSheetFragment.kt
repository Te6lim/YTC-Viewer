package com.te6lim.ytcviewer.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.FragmentSheetBottomBinding

class HomeBottomSheetFragment(private val fragmentActivity: Communicator) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentSheetBottomBinding

    companion object {
        const val TAG = "homeBottomSheetFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sheet_bottom, container, false
        )

        binding.recyclerView.adapter = HomeBottomSheetAdapter(object : SortItemViewHolder.Callback {
            override fun getSelectedColor(isSelected: Boolean): Int {
                return if (isSelected) ContextCompat.getColor(requireContext(), R.color.bottomSheetTitleColor)
                else ContextCompat.getColor(requireContext(), R.color.highlight)
            }

            override fun onClick(isSelected: Boolean) {

            }

        }).apply { list = fragmentActivity.getList() }

        return binding.root
    }

    interface Communicator {
        fun getList(): List<SortItem>
    }
}