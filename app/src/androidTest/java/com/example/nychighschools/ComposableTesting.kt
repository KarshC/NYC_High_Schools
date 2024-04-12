package com.example.nychighschools

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.nychighschools.composables.LoadError
import com.example.nychighschools.composables.SchoolItem
import com.example.nychighschools.data.model.NYCHighSchools
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposableTesting {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadError_displays_correct_text() {
        composeTestRule.setContent {
            LoadError()
        }
        composeTestRule.onNodeWithText("Failed to load schools").assertIsDisplayed()
    }

    @Test
    fun schoolItemExpandAndCollapseOnTap() {
        val mockSchoolData = NYCHighSchools(
            1,
            "School Name",
            "45",
            "97",
            "102",
            "80"
        )
        composeTestRule.setContent {
            SchoolItem(school = mockSchoolData)
        }

        // Initially, details should not be visible
        composeTestRule.onNodeWithText("Math Score:").assertDoesNotExist()
        composeTestRule.onNodeWithText("Reading Score:").assertDoesNotExist()
        composeTestRule.onNodeWithText("Writing Score:").assertDoesNotExist()
        // Simulate tap to expand
        composeTestRule.onNodeWithContentDescription("Drop-Down Arrow").performClick()
        // Now details should be visible
        composeTestRule.onNodeWithText("Math Score: ${mockSchoolData.satMathAvgScore}").assertIsDisplayed()
        composeTestRule.onNodeWithText("Reading Score: ${mockSchoolData.satCriticalReadingAvgScore}").assertIsDisplayed()
        composeTestRule.onNodeWithText("Writing Score: ${mockSchoolData.satWritingAvgScore}").assertIsDisplayed()
        // Simulate tap to collapse
        composeTestRule.onNodeWithContentDescription("Drop-Down Arrow").performClick()
        // Details should not be visible again
        composeTestRule.onNodeWithText("Math Score:").assertDoesNotExist()
        composeTestRule.onNodeWithText("Reading Score:").assertDoesNotExist()
        composeTestRule.onNodeWithText("Writing Score:").assertDoesNotExist()
    }
}