package com.example.wp4u.ui.board

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.wp4u.R
import com.example.wp4u.data.AuthRepository
import com.example.wp4u.database.WP4UDatabase
import com.example.wp4u.ui.accounts.CreateAccount
import com.example.wp4u.ui.accounts.Login

/**
 * Shows the images inside one category and lets the user add more.
 *
 * Upload uses the Android Photo Picker (PickVisualMedia), which needs NO
 * storage permissions on any Android version - the system shows its own
 * picker UI and grants temporary access to just the chosen image.
 */
class BoardActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CATEGORY_ID = "extra_category_id"
        const val EXTRA_CATEGORY_NAME = "extra_category_name"
    }

    private val viewModel: BoardViewModel by viewModels {
        BoardViewModel.Factory(
            application,
            intent.getLongExtra(EXTRA_CATEGORY_ID, -1L)
        )
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { viewModel.addImage(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        findViewById<TextView>(R.id.boardTitle).text =
            intent.getStringExtra(EXTRA_CATEGORY_NAME)

        val adapter = ImageAdapter()
        val emptyText = findViewById<TextView>(R.id.emptyText)

        findViewById<RecyclerView>(R.id.imagesRecyclerView).apply {
            layoutManager = GridLayoutManager(this@BoardActivity, 2)
            this.adapter = adapter
        }

        viewModel.images.observe(this) { images ->
            adapter.submitList(images)
            emptyText.visibility =
                if (images.isEmpty()) TextView.VISIBLE else TextView.GONE
        }

        viewModel.uploadError.observe(this) { failed ->
            if (failed) {
                Toast.makeText(
                    this, R.string.upload_failed, Toast.LENGTH_SHORT
                ).show()
                viewModel.onErrorShown()
            }
        }

        findViewById<FloatingActionButton>(R.id.addImageFab).setOnClickListener {
            pickImage.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }


        val database = WP4UDatabase.getInstance(this)
        val authRepository = AuthRepository(database.UserDAO())

        val signoutBtn = findViewById<Button>(R.id.signOut)

        signoutBtn.setOnClickListener {
            authRepository.signOut()
            val intent = Intent(this@BoardActivity, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}
