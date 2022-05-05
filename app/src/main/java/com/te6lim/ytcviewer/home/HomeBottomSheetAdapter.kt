package com.te6lim.ytcviewer.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ItemSortBinding

class HomeBottomSheetAdapter(val callback: SortItemViewHolder.Callback) :
    RecyclerView.Adapter<SortItemViewHolder>() {

    var list = listOf<SortItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): SortItemViewHolder {
        return SortItemViewHolder.create(container, callback)
    }

    override fun onBindViewHolder(holder: SortItemViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount() = list.size

}

class SortItemViewHolder(private val binding: ItemSortBinding, val callback: Callback) :
    RecyclerView.ViewHolder
        (binding.root) {
    companion object {
        fun create(parent: ViewGroup, callback: Callback): SortItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemSortBinding>(
                LayoutInflater.from(parent.context), R.layout.item_sort, parent, false
            )
            return SortItemViewHolder(binding, callback)
        }

        private var lastSelection = Pair<Int?, SortItem?>(null, null)
    }

    fun bind(itemSort: SortItem, position: Int) {
        binding.sortTypeText.text = itemSort.name
        changeAppearanceOfSelection(itemSort)
        binding.root.setOnClickListener {
            itemSort.isSelected = !itemSort.isSelected
            changeAppearanceOfPreviouslySelectedRelativeToCurrentlySelected(itemSort, position)
            callback.onClick(itemSort.isSelected)

            changeAppearanceOfSelection(itemSort)
        }
    }

    private fun changeAppearanceOfPreviouslySelectedRelativeToCurrentlySelected(
        itemSort: SortItem,
        position: Int
    ) {
        if (itemSort.isSelected) {

            with(lastSelection) {
                first?.let {
                    second?.isSelected = false
                    bindingAdapter?.notifyItemChanged(it)
                }
            }
            lastSelection = Pair(position, itemSort)
        }
    }

    private fun changeAppearanceOfSelection(itemSort: SortItem) {
        binding.selectedImage.visibility = if (itemSort.isSelected) View.VISIBLE else View.GONE
        binding.sortTypeText.setTextColor(callback.getSelectedColor(itemSort.isSelected))
    }

    interface Callback {
        fun getSelectedColor(isSelected: Boolean): Int

        fun onClick(isSelected: Boolean)
    }
}