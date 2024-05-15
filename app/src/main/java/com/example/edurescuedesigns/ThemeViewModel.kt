package com.example.edurescuedesigns

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Holds the variables for returning dark mode
data class ThemeState(
    // initially set this variable with isSystemDarkMode()
    val isDarkMode: Boolean = false
)

// Pass  into Settings to use the toggleDarkMode
class ThemeViewModel: ViewModel() {

    private val _themeState = MutableStateFlow(ThemeState())
    val uiState: StateFlow<ThemeState> = _themeState.asStateFlow()

    fun toggleDarkMode() {
        _themeState.update { currentState ->
            currentState.copy(
                isDarkMode = !currentState.isDarkMode
            )
        }
    }
}