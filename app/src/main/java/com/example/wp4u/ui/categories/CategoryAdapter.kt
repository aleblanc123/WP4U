package com.example.wp4u.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wp4u.R
import com.example.wp4u.model.Category

/**
 * Adapter for the category browsing list. Uses ListAdapter + DiffUtil so
 * the RecyclerView animates and updates efficiently when data changes.
 */
class CategoryAdapter(
    private val onCategoryClick: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }

    inner class CategoryViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {

        private val nameText: TextView = itemView.findViewById(R.id.categoryName)

        fun bind(category: Category) {
            nameText.text = category.name
            itemView.setOnClickListener { onCategoryClick(category) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
