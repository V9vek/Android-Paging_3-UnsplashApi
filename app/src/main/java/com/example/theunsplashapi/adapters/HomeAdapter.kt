package com.example.theunsplashapi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.theunsplashapi.R
import com.example.theunsplashapi.models.UnsplashPhoto
import kotlinx.android.synthetic.main.item_photo.view.*

class HomeAdapter : PagingDataAdapter<UnsplashPhoto, HomeAdapter.HomeViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<UnsplashPhoto>() {
        override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let { holder.bind(it) }
    }

    inner class HomeViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(photo: UnsplashPhoto) {
            itemView.apply {

                Glide.with(context).load(photo.imageUrl.regularImageUrl).into(ivPhoto)
                Glide.with(context).load(photo.user.profileImage.mediumImageUrl).into(ivUserPhoto)
                tvUsername.text = photo.user.name
            }
        }
    }
}







