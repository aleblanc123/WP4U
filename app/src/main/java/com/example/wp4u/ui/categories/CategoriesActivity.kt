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
 */
class CategoriesActivity : AppCompatActivity() {

    private val viewModel: CategoriesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        val adapter = CategoryAdapter { category ->
            startActivity(
                Intent(this, BoardActivity::class.java).apply {
                    putExtra(BoardActivity.EXTRA_CATEGORY_ID, category.id)
                    putExtra(BoardActivity.EXTRA_CATEGORY_NAME, category.name)
                }
            )
        }

        findViewById<RecyclerView>(R.id.categoriesRecyclerView).apply {
            layoutManager = LinearLayoutManager(this@CategoriesActivity)
            this.adapter = adapter
        }

        viewModel.categories.observe(this) { categories ->
            adapter.submitList(categories)
        }
    }
}
