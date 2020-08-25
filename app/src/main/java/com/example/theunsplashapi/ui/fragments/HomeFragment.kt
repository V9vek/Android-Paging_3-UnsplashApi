package com.example.theunsplashapi.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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

        viewModel.latestAndSearchPhoto.observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                homeAdapter.submitData(it)
            }
        })
    }

    private fun setUpRecyclerView() {
        rvLatestPhotos.apply {
            homeAdapter = HomeAdapter()
            adapter = homeAdapter.withLoadStateFooter(
                footer = HomeLoadStateAdapter { homeAdapter.retry() }
            )

            homeAdapter.addLoadStateListener { loadState ->
                rvLatestPhotos.isVisible = loadState.refresh is LoadState.NotLoading
                progressBar.isVisible = loadState.append is LoadState.Loading || loadState.refresh is LoadState.Loading
                btnRetry.isVisible = loadState.refresh is LoadState.Error
            }

            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val searchMenu = menu.findItem(R.id.actionSearch).actionView as SearchView

        searchMenu.apply {
            queryHint = "Search Photos..."

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.setQuery(query ?: "")
                    clearFocus()
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            setOnCloseListener {
                viewModel.setQuery("")
                false
            }
        }
    }
}