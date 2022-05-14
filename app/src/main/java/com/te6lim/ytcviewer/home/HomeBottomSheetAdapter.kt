package com.te6lim.ytcviewer.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ItemSortBinding

class HomeBottomSheetAdapter(val callback: SortItemViewHolder.Callback) :
    RecyclerView.Adapter<SortItemViewHolder>() {

    private val sortItems = SortItem.getItems()

    init {
        callback.currentlySelected()?.let {
            sortItems.find { item -> item.name == it.name }?.isSelected = true
        }
    }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): SortItemViewHolder {
        return SortItemViewHolder.create(container, callback)
    }

    override fun onBindViewHolder(holder: SortItemViewHolder, position: Int) {
        holder.bind(sortItems[position], position)
    }

    override fun getItemCount() = sortItems.size

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

        var lastSelection = Pair<Int?, SortItem?>(null, null)
    }

    fun bind(itemSort: SortItem, position: Int) {
        binding.sortTypeText.text = itemSort.name
        changeAppearanceOfSelection(itemSort)
        if (itemSort.isSelected) lastSelection = Pair(position, itemSort)
        binding.root.setOnClickListener {
            if (!itemSort.isSelected) {
                itemSort.isSelected = !itemSort.isSelected
                changeAppearanceOfPreviouslySelectedRelativeToCurrentlySelected(itemSort, position)
                changeAppearanceOfSelection(itemSort)
            }
            callback.onClick(
                SortItem(
                    name = itemSort.name, query = itemSort.query, isSelected = itemSort.isSelected,
                    isAsc = itemSort.isAsc
                )
            )
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
        binding.sortTypeText.setTextColor(
            callback.getSelectedColor(
                SortItem(
                    name = itemSort.name, query = itemSort.query, isSelected = itemSort.isSelected,
                    isAsc = itemSort.isAsc
                )
            )
        )
    }

    interface Callback {
        fun getSelectedColor(sortItem: SortItem): Int

        fun onClick(sortItem: SortItem)

        fun currentlySelected(): SortItem?
    }
}