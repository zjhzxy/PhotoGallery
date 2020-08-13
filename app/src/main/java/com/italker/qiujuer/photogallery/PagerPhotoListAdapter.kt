package com.italker.qiujuer.photogallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.pager_photo_view.view.*

class PagerPhotoListAdapter : ListAdapter<PhotoItem, MyPhotoViewHolder>(DiffCallback) {


    object DiffCallback : DiffUtil.ItemCallback<PhotoItem>() {
        override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
            return oldItem.photoId == newItem.photoId
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPhotoViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.pager_photo_view, parent, false).apply {
            return MyPhotoViewHolder(this)
        }

    }

    override fun onBindViewHolder(holder: MyPhotoViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(getItem(position).fullUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.itemView.pagerPhoto)
    }

}


class MyPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)