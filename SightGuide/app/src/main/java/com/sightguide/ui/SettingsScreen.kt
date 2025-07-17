package com.sightguide.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.* // Make sure this import is here
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Import for viewModel()
import kotlin.math.roundToInt


@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    // ViewModel is now provided here
    settingsViewModel: SettingsViewModel = viewModel() // Default parameter with viewModel()
) {
    // Collect states directly from the ViewModel
    val talkback = settingsViewModel.talkbackEnabled
    val haptic = settingsViewModel.hapticEnabled
    val highContrast = settingsViewModel.highContrastEnabled
    val selectedModel = settingsViewModel.selectedAiModel
    val sensitivity = settingsViewModel.cameraSensitivity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    heading()
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Text(
                text = "Accessibility Settings",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Accessibility Features Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Accessibility features section"
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2A2A2A),
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Audio & Feedback",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.semantics {
                        heading()
                    }
                )

                // TalkBack Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics(mergeDescendants = true) {
                            contentDescription = if (talkback) "TalkBack Audio is enabled" else "TalkBack Audio is disabled"
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TalkBack Audio",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            text = "Voice announcements for navigation",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = talkback,
                        onCheckedChange = { settingsViewModel.toggleTalkback(it) }, // Call ViewModel function
                        modifier = Modifier
                            .size(width = 64.dp, height = 40.dp)
                            .semantics {
                                contentDescription = "Toggle TalkBack Audio"
                            },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF4CAF50),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color(0xFF424242)
                        )
                    )
                }

                HorizontalDivider(color = Color(0xFF424242))

                // Haptic Feedback Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics(mergeDescendants = true) {
                            contentDescription = if (haptic) "Haptic Feedback is enabled" else "Haptic Feedback is disabled"
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Haptic Feedback",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            text = "Vibration alerts for important events",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = haptic,
                        onCheckedChange = { settingsViewModel.toggleHaptic(it) }, // Call ViewModel function
                        modifier = Modifier
                            .size(width = 64.dp, height = 40.dp)
                            .semantics {
                                contentDescription = "Toggle Haptic Feedback"
                            },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF4CAF50),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color(0xFF424242)
                        )
                    )
                }

                HorizontalDivider(color = Color(0xFF424242))

                // High Contrast Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics(mergeDescendants = true) {
                            contentDescription = if (highContrast) "High Contrast Mode is enabled" else "High Contrast Mode is disabled"
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "High Contrast Mode",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            text = "Enhanced visibility for low vision users",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                    }
                    Switch(
                        checked = highContrast,
                        onCheckedChange = { settingsViewModel.toggleHighContrast(it) }, // Call ViewModel function
                        modifier = Modifier
                            .size(width = 64.dp, height = 40.dp)
                            .semantics {
                                contentDescription = "Toggle High Contrast Mode"
                            },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF4CAF50),
                            uncheckedThumbColor = Color.Gray,
                            uncheckedTrackColor = Color(0xFF424242)
                        )
                    )
                }
            }
        }

        // Model Selection Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "AI Model selection section"
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2A2A2A),
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "AI Model Selection",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.semantics {
                        heading()
                    }
                )

                listOf(
                    "E4B-it" to "Enhanced model with better accuracy",
                    "E2B-it" to "Faster model with good performance"
                ).forEach { (model, description) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics(mergeDescendants = true) {
                                contentDescription = "$model model. $description. ${if (model == selectedModel) "Selected" else "Not selected"}"
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (model == selectedModel),
                            onClick = { settingsViewModel.selectAiModel(model) }, // Call ViewModel function
                            modifier = Modifier
                                .size(32.dp)
                                .semantics {
                                    contentDescription = "Select $model model"
                                },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF4CAF50),
                                unselectedColor = Color.Gray
                            )
                        )

                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = model,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                            Text(
                                text = description,
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        // Camera Sensitivity Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Camera sensitivity settings"
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2A2A2A),
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Camera Sensitivity",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.semantics {
                        heading()
                    }
                )

                Text(
                    text = "Current: $sensitivity FPS",
                    fontSize = 18.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.semantics {
                        contentDescription = "Current camera sensitivity is $sensitivity frames per second"
                    }
                )

                Slider(
                    value = sensitivity.toFloat(),
                    onValueChange = { settingsViewModel.changeCameraSensitivity(it.roundToInt()) }, // Call ViewModel function
                    valueRange = 10f..30f,
                    steps = 20,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = "Camera sensitivity slider. Current value $sensitivity FPS. Range 10 to 30 FPS"
                        },
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF4CAF50),
                        activeTrackColor = Color(0xFF4CAF50),
                        inactiveTrackColor = Color(0xFF424242)
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Low (10 FPS)",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "High (30 FPS)",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Back Button
        Button(
            onClick = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .semantics {
                    contentDescription = "Go back to main screen"
                },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Text(
                text = "BACK TO MAIN",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    // In a preview, we can either pass a dummy ViewModel instance
    // or use the default `viewModel()` which creates a new one for the preview scope.
    // For a simple preview like this, the default is fine.
    // If your ViewModel requires constructor parameters, you'd provide a factory:
    // val dummyViewModel = SettingsViewModel() // Or a Mock object
    // SettingsScreen(onBack = {}, settingsViewModel = dummyViewModel)

    SettingsScreen(onBack = { /* Preview doesn't navigate */ })
}