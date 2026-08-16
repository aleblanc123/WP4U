package com.example.wp4u.ui.categories

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wp4u.R
import com.example.wp4u.ui.board.BoardActivity

/**
 * Browse Categories screen - the entry point to the vision boards.
 *
 * Corresponds to the "Browse categories" use case: every image action
 * starts here, which is why the four image use cases <<include>> it
 * in the Demo 2 Use Case diagram.
 *
 * Reached after a successful sign-in or registration. It lists the five WP4U
 * categories supplied by [CategoriesViewModel] and opens the matching
 * [BoardActivity] when one is tapped.
 */
class CategoriesActivity : AppCompatActivity() {

    /**
     * Supplies the category list. `by viewModels()` returns the same instance across
     * configuration changes, so rotating the screen does not re-query the database.
     */
    private val viewModel: CategoriesViewModel by viewModels()

    /**
     * Builds the categories list: creates the adapter, attaches it to the
     * RecyclerView, and observes the ViewModel for data.
     *
     * @param savedInstanceState state saved by a previous instance of this activity,
     *                           or `null` when the activity is created for the first time.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        // On tap, open that category's board. The id and name are passed as intent
        // extras so BoardActivity can query the right images and set its own title.
        val adapter = CategoryAdapter { category ->
            startActivity(
                Intent(this, BoardActivity::class.java).apply {
                    putExtra(BoardActivity.EXTRA_CATEGORY_ID, category.categoryPK)
                    putExtra(BoardActivity.EXTRA_CATEGORY_NAME, category.categoryName)
                }
            )
        }

        // Categories are shown as a simple vertical list.
        findViewById<RecyclerView>(R.id.categoriesRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@CategoriesActivity)
            this.adapter = adapter
        }

        // Observing LiveData means the list is populated as soon as the database
        // returns, and redraws automatically if the category table ever changes.
        // Passing `this` as the lifecycle owner stops observation when the activity ends.
        viewModel.categories.observe(this) { categories ->
            adapter.submitList(categories)
        }
    }
}