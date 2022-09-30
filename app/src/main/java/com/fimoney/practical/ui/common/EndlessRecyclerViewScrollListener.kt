package com.fimoney.practical.ui.common

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class EndlessRecyclerViewScrollListener(private val onLoadMore: () -> Unit) :
    RecyclerView.OnScrollListener() {

    private var lastVisibleItemPosition = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = checkNotNull(recyclerView.layoutManager) {
            "Set LayoutManager first"
        }

        lastVisibleItemPosition = when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                layoutManager.findLastVisibleItemPositions(null).maxOrNull() ?: 0
            }
            is LinearLayoutManager -> {
                layoutManager.findLastVisibleItemPosition()
            }
            else -> {
                throw IllegalArgumentException(
                    "Unsupported LayoutManager: $layoutManager, " +
                        "Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager"
                )
            }
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        val layoutManager = checkNotNull(recyclerView.layoutManager) {
            "Set LayoutManager first"
        }

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        if ((visibleItemCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE) &&
            lastVisibleItemPosition >= totalItemCount - 1
        ) {
            onLoadMore()
        }
    }
}
