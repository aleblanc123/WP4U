package com.example.wp4u.ui.board

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wp4u.R
import com.example.wp4u.database.model.Image
import java.io.File
import java.util.Collections

/**
 * Grid adapter for the images inside one category. Glide loads the
 * bitmaps off the main thread and handles caching/downsampling, which
 * keeps scrolling smooth even with large photos.
 *
 * Demo 4: this adapter manages its own list instead of extending
 * ListAdapter. During a drag, positions must change with
 * notifyItemMoved(); ListAdapter's submitList() runs an asynchronous
 * diff that rebinds the dragged tile and cancels the drag immediately.
 * Database updates still animate through a synchronous DiffUtil pass
 * in setItems().
 */
class ImageAdapter(
    private val onImageLongPress: (Image) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val items = mutableListOf<Image>()

    /** The images in their current on-screen order (persisted after a drag). */
    fun currentItems(): List<Image> = items.toList()

    /** Replaces the displayed list, animating changes via DiffUtil. */
    fun setItems(newItems: List<Image>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize() = items.size
            override fun getNewListSize() = newItems.size
            override fun areItemsTheSame(oldPos: Int, newPos: Int) =
                items[oldPos].imagePK == newItems[newPos].imagePK
            override fun areContentsTheSame(oldPos: Int, newPos: Int) =
                items[oldPos] == newItems[newPos]
        })
        items.clear()
        items.addAll(newItems)
        diff.dispatchUpdatesTo(this)
    }

    /**
     * Applies one drag step. ItemTouchHelper calls this for each adjacent
     * position change while the tile is held; the final order is written
     * to the database once, when the tile is dropped.
     */
    fun moveItem(from: Int, to: Int) {
        if (from == to || from < 0 || to < 0) return
        Collections.swap(items, from, to)
        notifyItemMoved(from, to)
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView = itemView.findViewById(R.id.boardImage)
        private val dragHandle: View = itemView.findViewById(R.id.dragHandle)

        @SuppressLint("ClickableViewAccessibility")
        fun bind(image: Image) {
            Glide.with(imageView)
                .load(File(image.filePath))
                .centerCrop()
                .into(imageView)

            itemView.setOnLongClickListener {
                onImageLongPress(image)
                true
            }

            dragHandle.setOnTouchListener { _, event ->
                if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                    onStartDrag(this)
                }
                false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
