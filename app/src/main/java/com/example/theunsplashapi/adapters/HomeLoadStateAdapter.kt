package com.example.theunsplashapi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.theunsplashapi.R
import kotlinx.android.synthetic.main.layout_load_state.view.*

class HomeLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<HomeLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_load_state, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)

        holder.itemView.btnLoadStateRetry.setOnClickListener {
            retry.invoke()
        }
    }

    inner class LoadStateViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(loadState: LoadState) {

            itemView.apply {
                btnLoadStateRetry.isVisible = loadState !is LoadState.Loading
                tvLoadStateErrorMessage.isVisible = loadState !is LoadState.Loading

//                if (loadState is LoadState.Error) {
//                    tvLoadStateErrorMessage.text = loadState.error.localizedMessage
//                }
            }
        }
    }
}