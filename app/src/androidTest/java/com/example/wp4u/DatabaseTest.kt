package com.example.wp4u

import com.example.wp4u.database.*
import com.example.wp4u.database.dao.*

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider
import android.content.Context
import androidx.room3.Room
import com.example.wp4u.database.model.*
import kotlinx.coroutines.runBlocking

import org.junit.runner.RunWith
import org.junit.Before
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.Assert.assertNotNull

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
        assertNotNull("test wp4u singleton instance is not null", testInstance)
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
     * Tests getting every category listed in the CATEGORY table.
     */
    @Test
    fun getAllCategories_test() = runBlocking {
        val categories = categoryDAO.getAllCategories()
        assertNotNull("test array of categories holds all categories", categories)
    }

    /**
     * Test get all live categories (by PK)
     *
     * Tests getting every category listed in the CATEGORY table ordered by primary key.
     * List is of type LiveCategory for the browsing screen.
     */
    @Test
    fun getCategoriesLive_test() = runBlocking {
        val categoriesLive = categoryDAO.getCategoriesLive()
        assertNotNull("test live data list of categories has all categories", categoriesLive)
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
        assertEquals("/test/path", testImage?.filePath) // tests expected inserted values
    }

    @Test
    fun updateImage_test() {
        TODO("Not yet implemented")
    }

    @Test
    fun bulkUpdateImages_test() {
        TODO("Not yet implemented")
    }

    @Test
    fun deleteImage_test() {
        TODO("Not yet implemented")
    }

    @Test
    fun getImageById_test() {
        TODO("Not yet implemented")
    }

    @Test
    fun getImagesForCategory_test() {
        TODO("Not yet implemented")
    }

    @Test
    fun getMaxDisplayOrder_test() {
        TODO("Not yet implemented")
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