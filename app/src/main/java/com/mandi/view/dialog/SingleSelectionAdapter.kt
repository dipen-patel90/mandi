package com.mandi.view.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mandi.databinding.ItemSingleSelectionBinding
import com.mandi.model.Village

class SingleSelectionAdapter(private val onItemSelected: (item: Village) -> Unit) :
    RecyclerView.Adapter<SingleSelectionAdapter.ViewHolder>() {

    private val itemList: ArrayList<Village> = arrayListOf()

    class ViewHolder(private val binding: ItemSingleSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Village) {
            binding.itemName = item.name
            binding.itemPrice = item.price
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemSingleSelectionBinding
            .inflate(
                LayoutInflater.from(viewGroup.context),
                viewGroup,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        itemList[position].let { currentItem ->
            viewHolder.bind(currentItem)
            viewHolder.itemView.setOnClickListener {
                onItemSelected.invoke(currentItem)
            }
        }
    }

    override fun getItemCount() = itemList.size

    fun setData(newList: List<Village>) {
        val diffCallback = DiffCallback(itemList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        itemList.clear()
        itemList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    private class DiffCallback(
        private val oldList: List<Village>,
        private val newList: List<Village>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}