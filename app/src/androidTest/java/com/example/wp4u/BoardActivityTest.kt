package com.example.wp4u

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wp4u.ui.board.BoardActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BoardActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(BoardActivity::class.java)

    @Test
    fun addImageButton() {
        onView(withId(R.id.addImageFab))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    @Test
    fun logoutButton(){
        onView(withId(R.id.signOut))
            .perform(click())

        onView(withId(R.id.loginButton))
            .check(matches(isDisplayed()))
    }

}
