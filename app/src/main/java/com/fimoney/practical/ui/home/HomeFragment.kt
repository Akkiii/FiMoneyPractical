package com.fimoney.practical.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.fimoney.practical.R
import com.fimoney.practical.databinding.FragmentHomeBinding
import com.fimoney.practical.extension.*
import com.fimoney.practical.ui.base.BaseFragment
import com.fimoney.practical.ui.common.EndlessRecyclerViewScrollListener
import com.fimoney.practical.ui.common.LoadMoreState
import com.fimoney.practical.ui.common.LoadStateFooterAdapter
import com.fimoney.practical.ui.dialog.MovieDetailDialog
import com.fimoney.practical.ui.home.adapter.DataAdapter
import com.fimoney.practical.utils.Constant
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var adapter: DataAdapter
    private lateinit var footerAdapter: LoadStateFooterAdapter
    private var type = Constant.TYPE_ALL

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding)
        {
            viewModel = homeViewModel

            editTextSearch.setDebounce {
                runOnUiThread {
                    imageViewClear.isSelected = homeViewModel.queryField.flow.value?.isNotEmpty()!!
                    homeViewModel.setEvent(HomeUIEvent.LoadData(type))
                    editTextSearch.dismissKeyboard()
                }
            }

            imageViewClear.setOnClickListener {
                if (imageViewClear.isSelected) {
                    editTextSearch.setText("")
                    homeViewModel.queryField.flow.value = ""
                }
            }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            type = Constant.TYPE_ALL
                            homeViewModel.setEvent(HomeUIEvent.LoadData(type))
                        }
                        1 -> {
                            type = Constant.TYPE_MOVIE
                            homeViewModel.setEvent(HomeUIEvent.LoadData(type))
                        }
                        2 -> {
                            type = Constant.TYPE_SERIES
                            homeViewModel.setEvent(HomeUIEvent.LoadData(type))
                        }
                        3 -> {
                            type = Constant.TYPE_EPISODE
                            homeViewModel.setEvent(HomeUIEvent.LoadData(type))
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabReselected(tab: TabLayout.Tab?) {

                }
            })

            imageViewBookmark.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.toBookmark())
            }
        }

        initAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homeViewModel.uiState.collect { uiState ->
                        handleUIState(uiState)
                    }
                }
                launch {
                    homeViewModel.loadMoreState.collect { state ->
                        footerAdapter.loadMoreState = state
                        if (state is LoadMoreState.Loading) {
                            binding.recyclerViewData.scrollToEnd()
                        }
                    }
                }
                launch {
                    homeViewModel.effect.collect { uiEffect ->
                        handleUIEffect(uiEffect)
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = DataAdapter(requireContext(), onItemClick = { it, pos ->
            homeViewModel.setEvent(HomeUIEvent.LoadMovieDetail(it?.imdbID!!))
        }, onBookmarkClick = { it, isSelected ->
            if (isSelected) {
                toast("${adapter.currentList[it].title} bookmarked successfully!")
                homeViewModel.setEvent(HomeUIEvent.AddToBookMark(it))
            } else {
                toast("${adapter.currentList[it].title} remove bookmarked successfully!")
                homeViewModel.setEvent(HomeUIEvent.RemoveFromBookMark(it))
            }
        })

        footerAdapter = LoadStateFooterAdapter()

        binding.apply {
            recyclerViewData.itemAnimator = null
            val concatAdapter = ConcatAdapter(adapter, footerAdapter)
            val layoutManager = GridLayoutManager(requireContext(), 3)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (concatAdapter.getItemViewType(position)) {
                        1 -> 3
                        0 -> 1 //number of columns of the grid
                        else -> -1
                    }
                }
            }
            recyclerViewData.layoutManager = layoutManager
            recyclerViewData.adapter = concatAdapter
            recyclerViewData.addOnScrollListener(
                EndlessRecyclerViewScrollListener {
                    if (homeViewModel.loadMoreState.value !is LoadMoreState.Loading && homeViewModel.hasMore) {
                        homeViewModel.setEvent(HomeUIEvent.LoadMoreData(type))
                    }
                }
            )
        }
    }

    private fun handleUIState(uiState: HomeUIState) {
        when (uiState) {
            HomeUIState.Initial -> {
            }
            is HomeUIState.Loaded -> {
                binding.recyclerViewData.isVisible = true
                adapter.submitList(uiState.dataList)
            }
            is HomeUIState.Loading -> {
                if (uiState.isLoading) {
                    binding.progress.isVisible = true
                    binding.mainContainer.isVisible = false
                    binding.layoutNoData.isVisible = false
                    binding.layoutNoInternet.isVisible = false
                } else {
                    binding.progress.isVisible = false
                    binding.mainContainer.isVisible = true
                }
            }

            is HomeUIState.ShowProgress -> {
                if (uiState.isLoading)
                    showProgress()
                else
                    hideProgress()
            }
        }
    }

    private fun handleUIEffect(uiEffect: HomeUIEffect) {
        when (uiEffect) {
            HomeUIEffect.ShowNetworkError -> {
                binding.layoutNoInternet.isVisible = true
                binding.recyclerViewData.isVisible = false
            }

            is HomeUIEffect.ShowError -> {
                binding.layoutNoData.isVisible = true
                binding.textViewNoResult.text = uiEffect.message
                binding.recyclerViewData.isVisible = false
            }

            is HomeUIEffect.MovieDetailError -> {
                toast(uiEffect.message)
            }

            is HomeUIEffect.ShowMovieNetworkError -> {
                toast(getString(R.string.check_internet))
            }

            is HomeUIEffect.MovieDetail -> {
                val dialog = MovieDetailDialog(uiEffect.detail)
                dialog.show(childFragmentManager, "MovieDetailDialog")
            }
        }
    }
}