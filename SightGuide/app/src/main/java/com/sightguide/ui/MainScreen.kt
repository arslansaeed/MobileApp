@file:OptIn(ExperimentalMaterial3Api::class)

package com.sightguide.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel // Import for viewModel()
import com.sightguide.ui.theme.SightGuideTheme

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.semantics.Role



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onSettings: () -> Unit, // Navigation callback remains here
    mainViewModel: MainViewModel = viewModel() // Inject ViewModel
) {
    // Observe state from ViewModel
    val sceneDesc by mainViewModel.sceneDescription.collectAsState()
    val status by mainViewModel.appStatus.collectAsState()
    val query by mainViewModel.queryInput.collectAsState()
    val isRecordingVoice by mainViewModel.isRecordingVoice.collectAsState() // IMPORTANT: If using Flow in ViewModel

    // If mainViewModel.isRecordingVoice is a mutableStateOf, use this:
    // val isRecordingVoice = mainViewModel.isRecordingVoice

    // Remove LocalContext and audioPermissionLauncher from here; they are in MainActivity
    // val context = LocalContext.current
    // val audioPermissionLauncher = rememberLauncherForActivityResult(...) { ... }


//    val context = LocalContext.current
//    val audioPermissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { granted ->
//        if (granted) {
//            mainViewModel.startSpeechRecognition()  // or directly start recording //toggleVoiceInput()
//        } else {
//            mainViewModel.onAudioPermissionDenied()
//        }
//    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // High contrast background
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        // Scene Description - Large, prominent text
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    heading()
                    contentDescription = "Current scene description: $sceneDesc"
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = sceneDesc,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )
        }

        // Voice Input Section (for text)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Text input section for asking questions" // Changed contentDescription
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2A2A2A),
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Ask About Your Surroundings (Text)", // Changed text
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.semantics {
                        heading()
                    }
                )

                // Text Input Field
                OutlinedTextField(
                    value = query,
                    onValueChange = { mainViewModel.updateQueryInput(it) }, // Call ViewModel
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp, max = 160.dp)
                        .semantics {
                            contentDescription = "Text input field. Type your question here."
                        },
                    placeholder = {
                        Text(
                            "e.g., What's in front of me?",
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4CAF50),
                        unfocusedBorderColor = Color(0xFF616161),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF4CAF50),
                        focusedLabelColor = Color(0xFF4CAF50),
                        unfocusedLabelColor = Color.Gray
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 18.sp,
                        color = Color.White
                    ),
                    singleLine = false,
//                    minLines = 2,
//                    maxLines = 4
                )

                // Submit Button - Large and prominent
                Button(
                    //onClick = { mainViewModel.submitTextQuery(query) }, // Call ViewModel
                    onClick = { mainViewModel.submitTextQuery("") }, // Call ViewModel
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(68.dp)
                        .semantics {
                            contentDescription = "Submit typed question"
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = "SUBMIT QUESTION",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Voice Input Toggle Button
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Voice command input section. Tap the button to speak your question."
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2A2A2A), // Same as other input card
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Speak Your Question",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    //color = Color(0xFF8BC34A),
                    modifier = Modifier.semantics {
                        heading()
                    }
                )
                // Big Voice Input Toggle Button
                Button(
                    onClick = {
//                        if (ContextCompat.checkSelfPermission(
//                                context, Manifest.permission.RECORD_AUDIO
//                            ) == PackageManager.PERMISSION_GRANTED
//                        ) {
                        mainViewModel.toggleVoiceInput()
                    },
//                        } else {
//                            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//                        }
//                    },
                    modifier = Modifier
                        .size(140.dp) // Make it a big circle
                        .clip(RoundedCornerShape(70.dp)) // Make it circular
                        .semantics {
                            contentDescription = if (isRecordingVoice) "Stop voice input. Recording active." else "Start voice input. Tap to record."
                        },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRecordingVoice) Color(0xFFFF5722) else Color(0xFF2196F3), // Red when recording, Blue when idle
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
                ) {
                    Text(
                        text = if (isRecordingVoice) "STOP" else "SPEAK",
                        fontSize = 26.sp,
                        //fontWeight = FontWeight.Bold
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Text(
                    text = if (isRecordingVoice) "Listening for your command..." else "Tap to speak your question",
                    fontSize = 18.sp,
                    color = if (isRecordingVoice) Color(0xFFFF5722) else Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ... (Status and Settings Row, Emergency/Help Section remain the same) ...

        // IMPORTANT: If content overflows, you will need to make the outer Column scrollable.
        // Add verticalScroll modifier to the top-level Column in MainScreen if needed.
        // As per the previous discussion, if this screen becomes taller than the viewport,
        // you'll need to apply verticalScroll to the main Column.
        // Example: modifier = Modifier.verticalScroll(rememberScrollState())
        // Status and Settings Row


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
                //.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
            //verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Indicator
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .semantics {
                        contentDescription = "Application status: $status"
                    },
                colors = CardDefaults.cardColors(
                    containerColor = if (status.contains("Online")) Color(0xFF4CAF50) else Color(0xFFFF5722),
                    contentColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box( // Use Box for centering text inside Card
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center // Center text vertically and horizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Status indicator circle
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(6.dp) // Creates a circle with radius half the size
                                )
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = status,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            //Spacer(Modifier.width(16.dp))

            // Settings Button
            Button(
                onClick = onSettings,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .semantics {
                        contentDescription = "Open settings menu"
                        role = Role.Button
                    },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "SETTINGS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        // No extra end padding here, contentPadding handles it
                    )
                }
            }
        }

        // Quick Help Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Quick Help section. Provides common voice commands."
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF3F51B5),
                contentColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Quick Help Commands",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.semantics {
                        heading()
                    }
                )

                Text(
                    text = "• Say 'Where am I?' for your location.\n• Say 'What's around me?' for surroundings.\n• Say 'Find exit' for navigation assistance.",
                    fontSize = 17.sp, // Slightly larger body text
                    color = Color.White.copy(alpha = 0.9f), // Slightly less bright for softer look
                    lineHeight = 26.sp, // Improved line height
                    modifier = Modifier.semantics {
                        contentDescription = "List of quick help commands: Say Where am I for your location, Say What's around me for surroundings, Say Find exit for navigation assistance."
                    }
                )
            }
        }

        // IMPORTANT: If content overflows, you will need to make the outer Column scrollable.
        // Add verticalScroll modifier to the top-level Column in MainScreen if needed.
        // As per the previous discussion, if this screen becomes taller than the viewport,
        // you'll need to apply verticalScroll to the main Column.
        // Example: modifier = Modifier.verticalScroll(rememberScrollState())
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    SightGuideTheme {
        MainScreen(
            onSettings = {} // No longer passing status, sceneDesc, onSubmit as parameters
        )
    }
}