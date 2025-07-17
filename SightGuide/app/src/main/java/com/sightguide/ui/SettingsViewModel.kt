package com.sightguide.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {

    // State for accessibility features
    var talkbackEnabled by mutableStateOf(true)
        private set // Only allow modifications within the ViewModel

    var hapticEnabled by mutableStateOf(true)
        private set

    var highContrastEnabled by mutableStateOf(false)
        private set

    // State for AI Model Selection
    var selectedAiModel by mutableStateOf("E4B-it")
        private set

    // State for Camera Sensitivity
    var cameraSensitivity by mutableIntStateOf(20) // Use mutableIntStateOf for Int values
        private set

    // Functions to update the state
    fun toggleTalkback(enabled: Boolean) {
        talkbackEnabled = enabled
    }

    fun toggleHaptic(enabled: Boolean) {
        hapticEnabled = enabled
    }

    fun toggleHighContrast(enabled: Boolean) {
        highContrastEnabled = enabled
    }

    fun selectAiModel(model: String) {
        selectedAiModel = model
    }

    fun changeCameraSensitivity(sensitivity: Int) {
        cameraSensitivity = sensitivity
    }

    // You can add logic here to save/load settings to DataStore/SharedPreferences
    // For now, these are just in-memory states.
}