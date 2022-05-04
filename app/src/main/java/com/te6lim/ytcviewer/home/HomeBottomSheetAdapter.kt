package com.te6lim.ytcviewer.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.te6lim.ytcviewer.R
import com.te6lim.ytcviewer.databinding.ItemSortBinding

class HomeBottomSheetAdapter() : RecyclerView.Adapter<SortItemViewHolder>() {

    var list = listOf<SortItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): SortItemViewHolder {
        return SortItemViewHolder.create(container)
    }

    override fun onBindViewHolder(holder: SortItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

}

class SortItemViewHolder(private val binding: ItemSortBinding) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun create(parent: ViewGroup): SortItemViewHolder {
            val binding = DataBindingUtil.inflate<ItemSortBinding>(
                LayoutInflater.from(parent.context), R.layout.item_sort, parent, false
            )
            return SortItemViewHolder(binding)
        }
    }

    fun bind(itemSort: SortItem) {
        binding.sortTypeText.text = itemSort.name
        binding.selectedImage.visibility = if (itemSort.isSelected) View.VISIBLE else View.GONE
    }
}