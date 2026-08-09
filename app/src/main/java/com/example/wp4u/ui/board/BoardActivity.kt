package com.example.wp4u.ui.board

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.wp4u.R
import com.example.wp4u.data.ServiceLocator
import com.example.wp4u.database.model.Image
import com.example.wp4u.ui.accounts.Login

/**
 * Shows the images inside one category and supports all four image
 * use cases from the Demo 2 design:
 *  - Add: floating action button -> Android Photo Picker
 *  - Replace / Delete: long-press a tile -> options menu
 *  - Reorder: drag a tile by its handle; the new order is saved when
 *    the drag ends
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
            intent.getIntExtra(EXTRA_CATEGORY_ID, -1)
        )
    }

    /** The image whose picture is being replaced, while the picker is open. */
    private var pendingReplace: Image? = null

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { viewModel.addImage(it) }
        }

    private val pickReplacement =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            val target = pendingReplace
            pendingReplace = null
            if (uri != null && target != null) {
                viewModel.replaceImage(target, uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board)

        findViewById<TextView>(R.id.boardTitle).text =
            intent.getStringExtra(EXTRA_CATEGORY_NAME)

        lateinit var touchHelper: ItemTouchHelper

        val adapter = ImageAdapter(
            onImageLongPress = { image -> showImageOptions(image) },
            onStartDrag = { holder -> touchHelper.startDrag(holder) }
        )
        val emptyText = findViewById<TextView>(R.id.emptyText)
        val recycler = findViewById<RecyclerView>(R.id.imagesRecyclerView).apply {
            layoutManager = GridLayoutManager(this@BoardActivity, 2)
            this.adapter = adapter
        }

        // Drag-and-drop reorder. Drags start from the tile's handle (not
        // long-press, which opens the options menu instead); the new order
        // is written to the database once, when the tile is dropped.
        touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.START or ItemTouchHelper.END,
            0
        ) {
            override fun isLongPressDragEnabled() = false

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.moveItem(
                    viewHolder.bindingAdapterPosition,
                    target.bindingAdapterPosition
                )
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // Swipe-to-dismiss not used; delete goes through the menu.
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                // Drag finished - persist the order the user can now see.
                viewModel.persistOrder(adapter.currentItems())
            }
        })
        touchHelper.attachToRecyclerView(recycler)

        viewModel.images.observe(this) { images ->
            adapter.setItems(images)
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

        // Demo 4 fix: the shared AuthRepository from the ServiceLocator is
        // the instance that actually signed the user in. (Constructing a
        // new AuthRepository here, as before, always had currentUser null.)
        findViewById<Button>(R.id.signOut).setOnClickListener {
            ServiceLocator.authRepository.signOut()
            val intent = Intent(this@BoardActivity, Login::class.java)
            startActivity(intent)
            finish()
        }
    }

    /** Long-press menu: replace or delete the chosen image. */
    private fun showImageOptions(image: Image) {
        AlertDialog.Builder(this)
            .setTitle(R.string.image_options)
            .setItems(
                arrayOf(
                    getString(R.string.replace_image),
                    getString(R.string.delete_image)
                )
            ) { _, which ->
                when (which) {
                    0 -> {
                        pendingReplace = image
                        pickReplacement.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                    1 -> confirmDelete(image)
                }
            }
            .show()
    }

    /** Deleting is destructive, so ask once before doing it. */
    private fun confirmDelete(image: Image) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_image)
            .setMessage(R.string.delete_image_confirm)
            .setPositiveButton(R.string.delete_image) { _, _ ->
                viewModel.deleteImage(image)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }
}
