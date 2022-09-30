package com.fimoney.practical.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fimoney.practical.R
import com.fimoney.practical.databinding.ListItemDataBinding
import com.fimoney.practical.extension.executeAfter
import com.fimoney.practical.extension.setOnClickListenerPushDown
import com.fimoney.practical.model.DataModel

class DataAdapter(
    private val context: Context,
    private val onItemClick: (DataModel?, position: Int) -> Unit,
    private val onBookmarkClick: (position: Int, isSelected: Boolean) -> Unit
) : ListAdapter<DataModel, DataAdapter.DataHolder>(DIFF_CALLBACK) {
    private var lastPosition = -1

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataModel>() {
            override fun areItemsTheSame(
                oldItem: DataModel,
                newItem: DataModel
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DataModel,
                newItem: DataModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    class DataHolder private constructor(val binding: ListItemDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(parent: ViewGroup): DataHolder {
                val inflater = LayoutInflater.from(parent.context)
                return DataHolder(
                    binding = ListItemDataBinding.inflate(
                        inflater,
                        parent,
                        false
                    )
                )
            }
        }

        fun onBind(
            item: DataModel, onItemClick: (view: DataModel, position: Int) -> Unit,
            onBookmarkClick: (position: Int, isSelected: Boolean) -> Unit
        ) {
            binding.executeAfter {
                model = item
            }

            itemView.setOnClickListenerPushDown {
                onItemClick(item, bindingAdapterPosition)
            }

            binding.imageViewBookmark.setOnClickListener {
                binding.imageViewBookmark.isSelected = !binding.imageViewBookmark.isSelected
                onBookmarkClick(bindingAdapterPosition, binding.imageViewBookmark.isSelected)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        return DataHolder.create(parent)
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        holder.onBind(getItem(position), onItemClick, onBookmarkClick)
        setAnimation(holder.itemView, position)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > lastPosition) {
            val animation =
                AnimationUtils.loadAnimation(context, R.anim.slide_up)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }
}