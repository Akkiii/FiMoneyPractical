package com.fimoney.practical.ui.bookmark

import android.os.Bundle
import android.view.View
import com.fimoney.practical.R
import com.fimoney.practical.databinding.FragmentBookmarkBinding
import com.fimoney.practical.extension.launchAndRepeatWithViewLifecycle
import com.fimoney.practical.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookMarkFragment : BaseFragment<FragmentBookmarkBinding>(R.layout.fragment_bookmark) {
    private lateinit var adapter: BookMarkAdapter
    private val bookMarkViewModel: BookMarkViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()

        launchAndRepeatWithViewLifecycle {
            bookMarkViewModel.recentBookMark.collect { result ->
                adapter.submitList(result)
            }
        }
    }

    private fun initAdapter() {
        adapter = BookMarkAdapter(requireContext(), onItemClick = { it, pos ->

        }, onBookmarkClick = { it, isSelected ->

        })
        binding.recyclerViewData.adapter = adapter
        binding.recyclerViewData.itemAnimator = null
    }
}