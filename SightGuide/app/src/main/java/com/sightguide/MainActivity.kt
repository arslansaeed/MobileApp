package com.sightguide

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.* // Import all compose runtime things
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel // Correct import for viewModel()

import com.sightguide.ui.theme.SightGuideTheme
import com.sightguide.ui.MainScreen
import com.sightguide.ui.SettingsScreen
import com.sightguide.ui.MainViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private var speechRecognizer: SpeechRecognizer? = null
    private var recognizerIntent: Intent? = null

    // Using `lateinit var` for `mainViewModel` so it can be initialized in `onCreate`
    // and accessed by permission launcher callback (which is registered before setContent)
    private lateinit var mainViewModel: MainViewModel

//    val modelPath = applicationContext.filesDir
//        .resolve("llm/gemma-3n.task")
//        .absolutePath


//    val options = LlmInferenceOptions.builder()
//        .setModelPath(modelPath)
//        .setResultListener { partial, done -> /*...*/ }
//        .build()


    private val requestAudioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted. If the voice toggle button was pressed,
                // the ViewModel's _isRecordingVoice will already be true.
                // The LaunchedEffect in setContent will pick this up and start SR.
                // No direct action needed here other than potentially logging.
                println("RECORD_AUDIO permission granted.")
                // If you *strictly* want to re-toggle in VM: mainViewModel.toggleVoiceInput()
            } else {
                mainViewModel.onAudioPermissionDenied()
                Toast.makeText(this, "Audio permission denied. Voice input unavailable.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SpeechRecognizer and RecognizerIntent once
//        initSpeechRecognizer()

        setContent {
            var inSettings by remember { mutableStateOf(false) }

            // Obtain MainViewModel instance using viewModel() in Composable scope
            // This ensures it's lifecycle-aware and correctly provided with Application context
            mainViewModel = viewModel() // This will initialize it if not already.

            // Observe the isRecordingVoice state from the ViewModel's StateFlow
            val isRecordingVoice by mainViewModel.isRecordingVoice.collectAsState()
            val appStatus by mainViewModel.appStatus.collectAsState() // Observe appStatus as well


            // --- Lifecycle Observer for SpeechRecognizer ---
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner, mainViewModel) {
                val observer = LifecycleEventObserver { _, event ->
                    when (event) {
                        Lifecycle.Event.ON_START -> { /* No specific SR start here, LaunchedEffect handles it */ }
                        Lifecycle.Event.ON_STOP -> {
                            // When the app goes to background or Activity is stopped, stop listening
                            // But ONLY if SpeechRecognizer is actively listening.
                            // The ViewModel's `_isRecordingVoice` might still be true if user initiated.
                            // So, we stop SR and inform ViewModel.
                            if (isRecordingVoice) { // Observe the current state from ViewModel
                                stopSpeechRecognition()
                                // Inform ViewModel that recording has stopped due to lifecycle
                                // We need a way for ViewModel to know SR was stopped externally.
                                // It's generally better to just let the ViewModel's `toggleVoiceInput`
                                // be the primary trigger. If `isRecordingVoice` is true here,
                                // it means SR was active, and we're just stopping the native part.
                                // The ViewModel might still *think* it's recording until toggleVoiceInput is called.
                                // For simplicity for now, we won't try to force ViewModel state here on ON_STOP
                                // unless you have a specific UI requirement.
                            }
                        }
                        Lifecycle.Event.ON_DESTROY -> { // ON_DESTROY happens before onDispose for Composables
                            destroySpeechRecognizer()
                        }
                        else -> {}
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)

                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                    // If ON_DESTROY was not hit, this will ensure cleanup
                    destroySpeechRecognizer()
                }
            }


            // --- Observe ViewModel's isRecordingVoice state to start/stop SR ---
            // This `LaunchedEffect` will run whenever `isRecordingVoice` changes.
//            LaunchedEffect(isRecordingVoice) {
//                if (isRecordingVoice) {
//                    startSpeechRecognition() // Pass ViewModel implicitly via class member `mainViewModel`
//                } else {
//                    stopSpeechRecognition()
//                }
//            }


            SightGuideTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (inSettings) {
                        SettingsScreen(
                            onBack = { inSettings = false }
                        )
                    } else {
                        MainScreen(
                            onSettings = {
                                // When going to settings, stop speech recognition if active
                                if (isRecordingVoice) { // Check observed state
                                    mainViewModel.toggleVoiceInput() // This will set _isRecordingVoice to false in ViewModel
                                }
                                inSettings = true
                            },
                            mainViewModel = mainViewModel // Pass the ViewModel instance
                        )
                    }
                }
            }
        }
    }

//    private fun initSpeechRecognizer() {
//        if (SpeechRecognizer.isRecognitionAvailable(this)) {
//            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
//            recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
//                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)
//                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
//            }
//        } else {
//            Toast.makeText(this, "Speech recognition not available on this device.", Toast.LENGTH_LONG).show()
//            // If mainViewModel is initialized, you can update its status
//            if (::mainViewModel.isInitialized) {
//                mainViewModel.appStatus.value = "Status: SR Not Available" // Direct access to underlying MutableStateFlow
//                mainViewModel.sceneDescription.value = "Error: Speech recognition not supported."
//            }
//        }
//    }

//    private fun startSpeechRecognition() { // Removed mainViewModel parameter, now a class member
//        // First, check permission. If not granted, request it.
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            requestAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//            // Do NOT start recognition yet. LaunchedEffect will re-evaluate after permission.
//            // Also, make sure ViewModel's state is correctly reflecting 'not recording' if permission is denied.
//            // mainViewModel.isRecordingVoice.value = false // Not needed if onAudioPermissionDenied() handles it
//            return
//        }
//
//        speechRecognizer?.let { sr ->
//            sr.setRecognitionListener(object : RecognitionListener {
//                override fun onReadyForSpeech(params: Bundle?) { println("onReadyForSpeech") }
//                override fun onBeginningOfSpeech() { println("onBeginningOfSpeech") }
//                override fun onRmsChanged(rmsdB: Float) {}
//                override fun onBufferReceived(buffer: ByteArray?) {}
//                override fun onEndOfSpeech() { println("onEndOfSpeech") }
//
//                override fun onError(error: Int) {
//                    val errorMessage = when (error) {
//                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
//                        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
//                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
//                        SpeechRecognizer.ERROR_NETWORK -> "Network error"
//                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
//                        SpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized"
//                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
//                        SpeechRecognizer.ERROR_SERVER -> "Server error"
//                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
//                        else -> "Unknown speech recognition error"
//                    }
//                    println("SpeechRecognizer onError: $errorMessage ($error)")
//                    Toast.makeText(this@MainActivity, "Voice input error: $errorMessage", Toast.LENGTH_SHORT).show()
//                    // Inform ViewModel about the error, so it can update its state
//                    if (::mainViewModel.isInitialized) {
//                        mainViewModel.processVoiceInput("") // Pass empty string on error to stop recording in VM
//                        mainViewModel.appStatus.value = "Status: Voice Error"
//                    }
//                }
//
//                override fun onResults(results: Bundle?) {
//                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//                    val recognizedText = matches?.get(0) ?: ""
//                    println("SpeechRecognizer onResults: $recognizedText")
//                    if (::mainViewModel.isInitialized) {
//                        mainViewModel.processVoiceInput(recognizedText)
//                    }
//                }
//
//                override fun onPartialResults(partialResults: Bundle?) {
//                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//                    val partialText = matches?.get(0) ?: ""
//                    println("SpeechRecognizer onPartialResults: $partialText")
//                    // If you want to show partial results in UI, you'd need a separate exposed state in ViewModel
//                    // mainViewModel.updatePartialVoiceInput(partialText)
//                }
//
//                override fun onEvent(eventType: Int, params: Bundle?) {}
//            })
//            recognizerIntent?.let { sr.startListening(it) }
//            println("SpeechRecognizer started listening.")
//        } ?: run {
//            Toast.makeText(this, "Speech recognizer not initialized.", Toast.LENGTH_SHORT).show()
//            if (::mainViewModel.isInitialized) {
//                mainViewModel.processVoiceInput("") // Signal ViewModel to stop recording state
//            }
//        }
//    }

    private fun stopSpeechRecognition() {
        speechRecognizer?.stopListening()
        println("SpeechRecognizer stopped listening.")
    }

    private fun destroySpeechRecognizer() {
        speechRecognizer?.destroy()
        speechRecognizer = null
        println("SpeechRecognizer destroyed.")
    }

    // Removed the problematic `viewModels` delegate. ViewModel is obtained in setContent.
}