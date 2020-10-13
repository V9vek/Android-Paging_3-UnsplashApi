package com.example.theunsplashapi.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.theunsplashapi.R
import com.example.theunsplashapi.adapters.HomeAdapter
import com.example.theunsplashapi.adapters.HomeLoadStateAdapter
import com.example.theunsplashapi.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var homeAdapter: HomeAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnRetry.setOnClickListener {
            homeAdapter.retry()
        }

        setUpRecyclerView()

        homeAdapter.setOnItemClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }

        viewModel.latestAndSearchPhoto.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                homeAdapter.submitData(it)
            }
        })
    }

    private fun setUpRecyclerView() {
        rvLatestPhotos.apply {
            itemAnimator =
                null                     // DiffUtil shows prev data for a second before showing next data
            homeAdapter = HomeAdapter()
            adapter = homeAdapter.withLoadStateFooter(
                footer = HomeLoadStateAdapter { homeAdapter.retry() }
            )

            homeAdapter.addLoadStateListener { loadState ->
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvLatestPhotos.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error

                // No results found
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    homeAdapter.itemCount < 1
                ) {
                    rvLatestPhotos.isVisible = false
                    tvNoSearchResult.isVisible = true
                } else {
                    tvNoSearchResult.isVisible = false
                }
            }

            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.action_bar_menu, menu)

        val searchItem = menu.findItem(R.id.actionSearch)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            queryHint = "Search Photos..."

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.setQuery(query ?: "")
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            })

            setOnCloseListener {                // Not working
                viewModel.setQuery("")
                true
            }
        }

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                viewModel.setQuery("")
                return true
            }
        })
    }

}