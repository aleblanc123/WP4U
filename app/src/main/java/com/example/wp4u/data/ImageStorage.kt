package com.example.wp4u.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.util.UUID

/**
 * Copies user-picked images into the app's private storage.
 *
 * Why copy at all: the Uri returned by the photo picker is a temporary
 * grant - the app may lose access to it after the process dies. Copying
 * the bytes into filesDir gives WP4U a file it owns permanently, and the
 * database then only stores the path (Demo 2 design decision).
 */
object ImageStorage {

    /**
     * Copies [source] into internal storage and returns the absolute path
     * of the new file, or null if the copy failed.
     */
    suspend fun copyToInternalStorage(context: Context, source: Uri): String? =
        withContext(Dispatchers.IO) {
            try {
                val destination = newImageFile(context, "jpg")
                context.contentResolver.openInputStream(source)?.use { input ->
                    destination.outputStream().use { output ->
                        input.copyTo(output)
                    }
                } ?: return@withContext null
                destination.absolutePath
            } catch (e: IOException) {
                null
            }
        }

    /**
     * Copies a bundled asset (e.g. a curated sample image under
     * assets/seed/) into internal storage and returns the absolute path
     * of the new file, or null if the copy failed. Demo 4: used by
     * SampleImageSeeder when a new account is created.
     */
    suspend fun copyAssetToInternalStorage(context: Context, assetPath: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val extension = assetPath.substringAfterLast('.', "jpg")
                val destination = newImageFile(context, extension)
                context.assets.open(assetPath).use { input ->
                    destination.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                destination.absolutePath
            } catch (e: IOException) {
                null
            }
        }

    private fun newImageFile(context: Context, extension: String): File {
        val imagesDir = File(context.filesDir, "images").apply { mkdirs() }
        return File(imagesDir, "${UUID.randomUUID()}.$extension")
    }
}
