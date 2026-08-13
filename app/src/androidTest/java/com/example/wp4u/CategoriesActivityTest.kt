package com.example.wp4u

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wp4u.ui.categories.CategoriesActivity
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(CategoriesActivity::class.java)

    @Test
    fun categoryCorrectBoard(){

        onView(withText("Wedding Dresses"))
            .perform(click())

        onView(withId(R.id.boardTitle))
            .check(matches(withText("Wedding Dresses")))
    }
}