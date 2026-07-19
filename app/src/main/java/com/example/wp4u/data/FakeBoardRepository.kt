package com.example.wp4u.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wp4u.model.BoardImage
import com.example.wp4u.model.Category

/**
 * In-memory stand-in for the Room-backed repository.
 *
 * Lets the categories / board / upload screens be built and demoed before
 * the database layer is finished. Data lives only as long as the process.
 *
 * DELETE this class once the Room implementation exists, and change the
 * single line in [ServiceLocator] to point at the real repository.
 */
class FakeBoardRepository : BoardRepository {

    // Seed categories - names must match the Demo 2 design document.
    private val categories = listOf(
        Category(id = 1, name = "Wedding Dresses"),
        Category(id = 2, name = "Churches & Venues"),
        Category(id = 3, name = "Food & Catering"),
        Category(id = 4, name = "Flowers & Décor"),
        Category(id = 5, name = "Invitations")
    )

    private val categoriesLive = MutableLiveData(categories)

    // categoryId -> images in that category
    private val imagesByCategory = mutableMapOf<Long, MutableList<BoardImage>>()
    private val imagesLiveByCategory = mutableMapOf<Long, MutableLiveData<List<BoardImage>>>()
    private var nextImageId = 1L

    override fun getCategories(): LiveData<List<Category>> = categoriesLive

    override fun getImagesForCategory(categoryId: Long): LiveData<List<BoardImage>> =
        imagesLiveByCategory.getOrPut(categoryId) {
            MutableLiveData(imagesByCategory.getOrPut(categoryId) { mutableListOf() }.toList())
        }

    override suspend fun addImage(categoryId: Long, filePath: String) {
        val list = imagesByCategory.getOrPut(categoryId) { mutableListOf() }
        list.add(
            BoardImage(
                id = nextImageId++,
                categoryId = categoryId,
                filePath = filePath,
                position = list.size
            )
        )
        // postValue because addImage is called from a background coroutine
        imagesLiveByCategory.getOrPut(categoryId) { MutableLiveData() }
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
