package com.example.wp4u.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wp4u.database.model.*

/**
 * In-memory stand-in for the Room-backed repository.
 *
 * Lets the categories / board / upload screens be built and demoed before
 * the database layer is finished. Data lives only as Int as the process.
 *
 * DELETE this class once the Room implementation exists, and change the
 * single line in [ServiceLocator] to point at the real repository.
 */
class FakeBoardRepository : BoardRepository {

    // Seed categories - names must match the Demo 2 design document.
    // temporary note: pk/id is auto generated
    private val categories = listOf(
        Category(categoryName = "Wedding Dresses", description = ""),
        Category(categoryName = "Churches & Venues", description = ""),
        Category(categoryName = "Food & Catering", description = ""),
        Category(categoryName = "Flowers & Decor", description = ""),
        Category(categoryName = "Invitations", description = "")
    )

    private val categoriesLive = MutableLiveData(categories)

    // categoryPK -> images in that Category
    private val imagesByCategory = mutableMapOf<Int, MutableList<Image>>()
    private val imagesLiveByCategory = mutableMapOf<Int, MutableLiveData<List<Image>>>()
    private var nextImageId = 1L

    override fun getCategories(): LiveData<List<Category>> = categoriesLive

    override fun getImagesForCategory(categoryPK: Int): LiveData<List<Image>> =
        imagesLiveByCategory.getOrPut(categoryPK) {
            MutableLiveData(imagesByCategory.getOrPut(categoryPK) { mutableListOf() }.toList())
        }

    override suspend fun addImage(categoryFK: Int, filePath: String) {
        val list = imagesByCategory.getOrPut(categoryFK) { mutableListOf() }
        list.add(
            Image(
                imagePK = nextImageId++.toInt(),
                categoryFK = categoryFK,
                filePath = filePath,
                displayOrder = list.size,
                userFK = TODO(),
                uploadedAt = TODO()
            )
        )
        // postValue because addImage is called from a background coroutine
        imagesLiveByCategory.getOrPut(categoryFK) { MutableLiveData() }
            .postValue(list.toList())
    }
}

/**
 * Minimal service locator so every screen shares one repository instance.
 * When the Room layer is ready, this is the ONLY line that changes:
 *   val repository: BoardRepository = RoomBoardRepository(...)
 */
object ServiceLocator {
    val repository: BoardRepository by lazy { FakeBoardRepository() }
}
