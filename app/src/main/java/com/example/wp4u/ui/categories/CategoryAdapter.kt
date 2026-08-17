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
 *
 * The adapter binds each [Category] row to `res/layout/item_category.xml` and reports
 * taps back to [CategoriesActivity], which opens the matching vision board.
 *
 * @param onCategoryClick invoked with the tapped [Category]. Passing the handler in as
 *                        a constructor parameter keeps the adapter reusable — it does
 *                        not need to know what happens on a tap.
 */
class CategoryAdapter(
    private val onCategoryClick: (Category) -> Unit
) : ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(Diff) {

    /**
     * Tells [ListAdapter] how to compare two [Category] objects so it can work out the
     * minimum set of changes to redraw instead of refreshing the whole list.
     */
    object Diff : DiffUtil.ItemCallback<Category>() {

        /**
         * Decides whether two entries are the SAME row, compared by primary key.
         *
         * @return `true` if both refer to the same category.
         */
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.categoryPK == newItem.categoryPK

        /**
         * Decides whether a row that is already on screen needs redrawing, by
         * comparing every field (`data class` equality).
         *
         * @return `true` if the visible contents are unchanged.
         */
        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }

    /**
     * Holds the views for one row so they are looked up once and then reused as the
     * user scrolls, rather than being searched for on every bind.
     *
     * @param itemView the inflated `item_category` row layout.
     */
    inner class CategoryViewHolder(itemView: android.view.View) :
        RecyclerView.ViewHolder(itemView) {

        /** Label showing the category's display name, e.g. "Flowers & Decor". */
        private val nameText: TextView = itemView.findViewById(R.id.categoryName)

        /**
         * Fills this row with one category's data and makes it tappable.
         *
         * @param Category the category to display in this row.
         */
        fun bind(Category: Category) {
            nameText.text = Category.categoryName
            itemView.setOnClickListener { onCategoryClick(Category) }
        }
    }

    /**
     * Creates a new row by inflating `item_category`. Called by the RecyclerView only
     * when no recycled row is available to reuse.
     *
     * @param parent the RecyclerView the row will be added to.
     * @param viewType row type id; unused here because every row looks the same.
     * @return a holder wrapping the newly inflated row.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    /**
     * Binds the category at the given position into a recycled row.
     *
     * @param holder the row being reused.
     * @param position index of the category to display.
     */
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}