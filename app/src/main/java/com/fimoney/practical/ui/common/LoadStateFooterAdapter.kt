package com.fimoney.practical.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.fimoney.practical.databinding.ItemLoadStateBinding

class LoadStateFooterAdapter :
    RecyclerView.Adapter<LoadStateFooterAdapter.LoadStateViewHolder>() {

    var loadMoreState: LoadMoreState = LoadMoreState.NotLoading
        set(loadState) {
            if (field != loadState) {
                val oldItem = displayLoadStateAsItem(field)
                val newItem = displayLoadStateAsItem(loadState)

                if (oldItem && !newItem) {
                    notifyItemRemoved(0)
                } else if (newItem && !oldItem) {
                    notifyItemInserted(0)
                } else if (oldItem && newItem) {
                    notifyItemChanged(0)
                }
                field = loadState
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadStateViewHolder {
        return LoadStateViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, position: Int) {
        holder.binding.layoutProgressLoader.isVisible = true
    }

    override fun getItemCount(): Int = if (displayLoadStateAsItem(loadMoreState)) 1 else 0

    private fun displayLoadStateAsItem(loadMoreState: LoadMoreState): Boolean {
        return loadMoreState is LoadMoreState.Loading
    }

    override fun getItemViewType(position: Int): Int {
        return 1
    }

    class LoadStateViewHolder private constructor(val binding: ItemLoadStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup): LoadStateViewHolder {
                return LoadStateViewHolder(
                    binding = ItemLoadStateBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }
}
