package com.example.wp4u.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.wp4u.data.ServiceLocator
import com.example.wp4u.database.model.Category

/**
 * Exposes the list of vision board categories to [CategoriesActivity].
 * Talks only to the repository interface, never to the database directly.
 *
 * Holding the data here rather than in the activity means it survives configuration
 * changes such as screen rotation — the activity is recreated, the ViewModel is not.
 * Depending on the `BoardRepository` interface (resolved through
 * [com.example.wp4u.data.ServiceLocator]) rather than on Room keeps the UI layer
 * independent of how the data is actually stored.
 *
 * @property categories the five WP4U categories, wrapped in [LiveData] so the activity
 *                      is notified and redraws whenever the underlying table changes.
 */
class CategoriesViewModel : ViewModel() {

    val categories: LiveData<List<Category>> =
        ServiceLocator.repository.getCategories()
}