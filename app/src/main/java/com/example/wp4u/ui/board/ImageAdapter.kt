package com.example.wp4u.ui.board

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wp4u.R
import com.example.wp4u.model.BoardImage
import java.io.File

/**
 * Grid adapter for the images inside one category. Glide loads the
 * bitmaps off the main thread and handles caching/downsampling, which
 * keeps scrolling smooth even with large photos.
 */
class ImageAdapter :
    ListAdapter<BoardImage, ImageAdapter.ImageViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<BoardImage>() {
        override fun areItemsTheSame(oldItem: BoardImage, newItem: BoardImage) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: BoardImage, newItem: BoardImage) =
            oldItem == newItem
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.boardImage)

        fun bind(image: BoardImage) {
            Glide.with(imageView)
                .load(File(image.filePath))
                .centerCrop()
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
