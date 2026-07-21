package com.example.wp4u.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wp4u.R
import com.example.wp4u.database.model.Category

/**
 * Adapter for the Category browsing list. Uses ListAdapter + DiffUtil so
 * the RecyclerView animates and updates efficiently when data changes.
 */
class CategoryAdapter(
    private val onCategoryClick: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(Diff) {

    object Diff : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.categoryPK == newItem.categoryPK

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }

    inner class CategoryViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {

        private val nameText: TextView = itemView.findViewById(R.id.categoryName)

        fun bind(Category: Category) {
            nameText.text = Category.categoryName
            itemView.setOnClickListener { onCategoryClick(Category) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_Category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
