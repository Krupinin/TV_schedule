package ru.wish.tv_schedule.presentation.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavController
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class InputScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun inputScreen_displays_all_elements() {
        val navController = mockk<NavController>()
        composeTestRule.setContent {
            InputScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("TV Schedule by Krupinin").assertIsDisplayed()
        composeTestRule.onNodeWithText("Enter Country and Date").assertIsDisplayed()
        composeTestRule.onNodeWithText("Country Code").assertIsDisplayed()
        composeTestRule.onNodeWithText("Date (YYYY-MM-DD)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Get Schedule").assertIsDisplayed()
    }

    @Test
    fun inputScreen_country_dropdown_expands_and_selects() {
        val navController = mockk<NavController>()
        composeTestRule.setContent {
            InputScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Country Code").performClick()

        composeTestRule.onNodeWithText("RU").assertIsDisplayed()
        composeTestRule.onNodeWithText("US").assertIsDisplayed()
        composeTestRule.onNodeWithText("FR").assertIsDisplayed()

        composeTestRule.onNodeWithText("US").performClick()

        composeTestRule.onNodeWithText("us").assertIsDisplayed()
    }

    @Test
    fun inputScreen_date_picker_opens_on_icon_click() {
        val navController = mockk<NavController>()
        composeTestRule.setContent {
            InputScreen(navController = navController)
        }

        composeTestRule.onNodeWithContentDescription("Select date").performClick()

        composeTestRule.onNodeWithText("OK").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun inputScreen_navigates_on_button_click_with_country_selected() {
        val navController = mockk<NavController>(relaxed = true)
        composeTestRule.setContent {
            InputScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Country Code").performClick()
        composeTestRule.onNodeWithText("US").performClick()

        composeTestRule.onNodeWithText("Get Schedule").performClick()

        verify { navController.navigate(any<String>()) }
    }

    @Test
    fun inputScreen_does_not_navigate_on_button_click_without_country() {
        val navController = mockk<NavController>(relaxed = true)
        composeTestRule.setContent {
            InputScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Get Schedule").performClick()

        verify(exactly = 0) { navController.navigate(any<String>()) }
    }
}
