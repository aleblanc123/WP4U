package com.example.wp4u

import com.example.wp4u.database.*
import com.example.wp4u.database.dao.*

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider
import android.content.Context
import androidx.room3.Room
import com.example.wp4u.database.model.*
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat

import org.junit.runner.RunWith
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull

/**
 * Test class for database functionality
 *
 * Tests the WP4U Database instantiation/fetch & each DAO function.
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var wp4uDB: WP4UDatabase
    private lateinit var categoryDAO: CategoryDAO
    private lateinit var imageDAO: ImageDAO
    private lateinit var userDAO: UserDAO

    @Before
    fun beforeTest(){
        wp4uDB = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WP4UDatabase::class.java
        ).build()
        categoryDAO = wp4uDB.CategoryDAO()
        imageDAO = wp4uDB.ImageDAO()
        userDAO = wp4uDB.UserDAO()
    }

    /**
     * Singleton instance test
     *
     * Tests that the getInstance() function successfully returns a singleton instance of the
     * WP4U database.
     */
    @Test
    fun getDBInstance_test(){
        val context = ApplicationProvider.getApplicationContext<Context>()
        val testInstance = WP4UDatabase.getInstance(context)
        assertNotNull("wp4u singleton instance returns null.", testInstance)
    }

    //----------------- CATEGORY DAO TESTS -----------------//
    /**
     * Test insert category
     *
     * Tests inserting a new category to the CATEGORY table.
     */
    @Test
    fun insertCategory_test() = runBlocking {
        categoryDAO.insertCategory(
            Category(categoryName = "Flowers",
            description = "Category for pictures of flowers."
        ))

        val categories = categoryDAO.getAllCategories()
        assertEquals(1, categories.size) // tests successful insertion
        assertEquals("Flowers", categories[0].categoryName) // tests expected inserted values
    }

    /**
     * Test get all categories
     *
     * Tests selecting every category listed in the CATEGORY table.
     */
    @Test
    fun getAllCategories_test() = runBlocking {
        val categories = categoryDAO.getAllCategories()
        assertNotNull("Array of categories returns null.", categories)
    }

    /**
     * Test get all live categories (by PK)
     *
     * Tests getting every category listed in the CATEGORY table ordered by primary key.
     * List is of type LiveData for the browsing screen.
     */
    @Test
    fun getCategoriesLive_test() = runBlocking {
        val categoriesLive = categoryDAO.getCategoriesLive()
        assertNotNull("Live data list of categories returns null.", categoriesLive)
    }

    //----------------- IMAGE DAO TESTS -----------------//
    /**
     * Test insert image
     *
     * Tests inserting a new image to the IMAGE table.
     */
    @Test
    fun insertImage_test() = runBlocking {
        // sample data
        userDAO.insertUser(User(100000, "d", "d@email.com", "pw", 12345))
        categoryDAO.insertCategory(Category(200000, "New Images", "Test"))

        imageDAO.insertImage(
            Image(9999, 100000, 200000, "/test/path", 1, 999))

        val testImage = imageDAO.getImageById(9999)
        assertNotNull("test new image insertion", testImage) // tests successful insertion
        assertEquals("New image was not successfully inserted.",
            "/test/path", testImage?.filePath) // tests expected inserted values
    }

    /**
     * Test update image
     *
     * Tests updating a pre-existing image in the IMAGE table.
     */
    @Test
    fun updateImage_test() = runBlocking {
        // sample data
        userDAO.insertUser(User(100000, "d", "d@email.com", "pw", 12345))
        categoryDAO.insertCategory(Category(200000, "New Images", "Test"))
        val testImage = Image(9999, 100000, 200000, "/test/path", 1, 999)

        imageDAO.insertImage(testImage)

        imageDAO.updateImage(
            testImage.copy(
            filePath = "/test/path2",
            uploadedAt = System.currentTimeMillis()
            )
        )

       val updatedTestImage = imageDAO.getImageById(9999) // image with updated file path and upload time
        assertEquals("Image was not successfully updated.",
            "/test/path2", updatedTestImage?.filePath)
    }

    /**
     * Test bulk update images (re-ordering)
     *
     * Tests updating the order of all pre-existing images in the IMAGE table.
     */
    @Test
    fun bulkUpdateImages_test() = runBlocking {
        // sample data
        userDAO.insertUser(User(100000, "d", "d@email.com", "pw", 12345))
        categoryDAO.insertCategory(Category(200000, "New Images", "Test"))
        val testImage1 = Image(8888, 100000, 200000, "/test/path1", 100, 999)
        val testImage2 = Image(9999, 100000, 200000, "/test/path2", 200, 999)
        imageDAO.insertImage(testImage1) // displayOrder set to 100
        imageDAO.insertImage(testImage2) // displayOrder set to 200

        val imageList: List<Image> = listOf(testImage1, testImage2)

        imageDAO.updateImages(
            imageList.mapIndexed { // Sets displayOrder to index order (0 & 1)
                    updatedOrder, image -> image.copy(displayOrder = updatedOrder)
            })

        val updatedTestImage1 = imageDAO.getImageById(8888)
        assertEquals("Display order does not match new display order.",
            0, updatedTestImage1?.displayOrder)
    }

    /**
     * Test delete image
     *
     * Tests deleting a pre-existing image in the IMAGE table.
     */
    @Test
    fun deleteImage_test() = runBlocking {
        // sample data
        userDAO.insertUser(User(100000, "d", "d@email.com", "pw", 12345))
        categoryDAO.insertCategory(Category(200000, "New Images", "Test"))
        imageDAO.insertImage(Image(9999, 100000, 200000, "/test/path", 1, 999))

        val testImage = imageDAO.getImageById(9999)
        imageDAO.deleteImage(testImage!!)

        val deletedImage = imageDAO.getImageById(9999) // attempt to get image that should no longer exist
        assertNull("Image was not deleted.", deletedImage)
    }

    /**
     * Test get image (by ID)
     *
     * Tests selecting a pre-existing image from the IMAGE table (by imagePK).
     */
    @Test
    fun getImageById_test() = runBlocking {
        // sample data
        userDAO.insertUser(User(100000, "d", "d@email.com", "pw", 12345))
        categoryDAO.insertCategory(Category(200000, "New Images", "Test"))
        imageDAO.insertImage(Image(9999, 100000, 200000, "/test/path", 1, 999))

        val testImage = imageDAO.getImageById(9999)

        assertEquals("File path does not match test file path.",
            "/test/path", testImage?.filePath)
    }

    /**
     * Test get live images in category (by categoryId, userId)
     *
     * Tests selecting a pre-existing images in a single category from the IMAGE table
     * (by categoryId, userId pair).
     * List is of type LiveData for the browsing screen.
     */
    @Test
    fun getImagesForCategory_test() = runBlocking {
        // sample data
        userDAO.insertUser(User(100000, "d", "d@email.com", "pw", 12345))
        categoryDAO.insertCategory(Category(200000, "Cat 1", "Test"))
        categoryDAO.insertCategory(Category(300000, "Cat 2", "Test"))
        imageDAO.insertImage(Image(7777, 100000, 200000, "/test/path", 1, 999))
        imageDAO.insertImage(Image(8888, 100000, 200000, "/test/path2", 2, 999))
        imageDAO.insertImage(Image(9999, 100000, 300000, "/test/path3", 3, 999))

       val imageList = imageDAO.getImagesForCategory(200000, 100000) // list only has images in Cat 1

        val imageListContents = imageList.toString()
        assertThat("Tests image list only contains images in cat1.",
            !imageListContents.contains("300000")) // checks if list contains categoryPK for Cat 2 (returns false if so)
    }

    /**
     * Test get the highest display order (by categoryId, userId)
     *
     * Tests selecting the highest displayed image in a single category from the IMAGE table
     * (by categoryId, userId pair).
     */
    @Test
    fun getMaxDisplayOrder_test() = runBlocking {
        // sample data
        userDAO.insertUser(User(100000, "d", "d@email.com", "pw", 12345))
        categoryDAO.insertCategory(Category(200000, "New Images", "Test"))
        imageDAO.insertImage(Image(7777, 100000, 200000, "/test/path", 100, 999))
        imageDAO.insertImage(Image(8888, 100000, 200000, "/test/path2", 300, 999))
        imageDAO.insertImage(Image(9999, 100000, 200000, "/test/path3", 200, 999))

        val maxDisplayOrder = // largest integer value for displayOrder in table
            imageDAO.getMaxDisplayOrder(200000, 100000)

        assertEquals("Highest display order does not match expected.",
            300, maxDisplayOrder)
    }

    //----------------- USER DAO TESTS -----------------//
    @Test
    fun insertUser_test() {
        TODO("Not yet implemented")
    }

    @Test
    fun getUserByUserPass_test() {
        TODO("Not yet implemented")
    }

    @Test
    fun getUserByEmailPass_test() {
        TODO("Not yet implemented")
    }

    @Test
    fun checkIfEmailExists_test() {
        TODO("Not yet implemented")
    }

    @After
    fun afterTest(){
        wp4uDB.close() //closes the WP4U database
    }
}