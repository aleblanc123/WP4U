package com.example.wp4u.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.wp4u.data.ServiceLocator
import com.example.wp4u.model.Category

/**
 * Exposes the list of vision board categories to [CategoriesActivity].
 * Talks only to the repository interface, never to the database directly.
 */
class CategoriesViewModel : ViewModel() {

    val categories: LiveData<List<Category>> =
        ServiceLocator.repository.getCategories()
}
