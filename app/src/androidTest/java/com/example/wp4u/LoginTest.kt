package com.example.wp4u


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.wp4u.ui.accounts.Login
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(Login::class.java)

    @Test
    fun loginScreen(){
        onView(withId(R.id.inputUsername))
            .check(matches(isDisplayed()))

        onView(withId(R.id.inputPassword))
            .check(matches(isDisplayed()))

        onView(withId(R.id.loginButton))
            .check(matches(isDisplayed()))
    }

    @Test
    fun loginPage(){
        onView(withId(R.id.signUp))
            .perform(click())

        onView(withId(R.id.welcome))
            .check(matches(isDisplayed()))
    }
}

