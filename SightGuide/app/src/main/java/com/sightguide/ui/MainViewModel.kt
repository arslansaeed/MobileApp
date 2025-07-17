package com.sightguide.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
//import com.google.mediapipe.tasks.genai.llminference.LlmInference
//import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions
//import com.google.mediapipe.tasks.genai.llm.LlmInference
//import com.google.mediapipe.tasks.genai.llm.LlmInference.LlmInferenceOptions
//import com.google.mediapipe.tasks.genai.llm.LlmInference.ResponseListener // Make sure this is also correct



class MainViewModel(application: Application) : AndroidViewModel(application) {

//    private var gemmaModel: LlmInference? = null

    // For streaming responses from Gemma (if applicable)
//    private val _gemmaResponseFlow = MutableSharedFlow<String>()
//    val gemmaResponseFlow = _gemmaResponseFlow.asSharedFlow()

    // UI State exposed as StateFlows
    private val _sceneDescription = MutableStateFlow("Initializing...")
    val sceneDescription = _sceneDescription.asStateFlow()

    private val _appStatus = MutableStateFlow("Status: Offline")
    val appStatus = _appStatus.asStateFlow()

    private val _queryInput = MutableStateFlow("")
    val queryInput = _queryInput.asStateFlow()

    private val _isRecordingVoice = MutableStateFlow(false)
    val isRecordingVoice = _isRecordingVoice.asStateFlow() // This is now publicly readable


//    init {
//        viewModelScope.launch {
//            _appStatus.value = "Status: Online"
//            _sceneDescription.value = "Scanning surroundings..."
//
//            // Obtain the model path dynamically
//            val modelPath = getApplication<Application>().filesDir
//                .resolve("llm/gemma-3n.task")
//                .absolutePath
//
//            // Initialize the Gemma model
//            gemmaModel = createGemmaModel(getApplication(), modelPath)
//        }
//    }

    init {
        viewModelScope.launch {
            _appStatus.value = "Status: Online"
            _sceneDescription.value = "Scanning surroundings..."

            try {
                // Obtain the model path dynamically
                // Ensure 'llm/gemma-3n.task' is correct path within app's filesDir
                val modelPath = application.filesDir
                    .resolve("llm/gemma-3n.task")
                    .absolutePath
                // Or if it's in assets: "gemma-3n-E4B-it.task" directly

                // Initialize the Gemma model
                //gemmaModel = createGemmaModel(application.applicationContext, modelPath)
                println("Gemma model initialized successfully at $modelPath")
                _appStatus.value = "Status: Online (Model Ready)"
            } catch (e: Exception) {
                println("Error initializing Gemma model: ${e.message}")
                _appStatus.value = "Status: Model Load Failed"
                _sceneDescription.value = "Error: Could not load AI model. ${e.message}"
            }
        }
    }


//    override fun onCleared() {
//        super.onCleared()
//        gemmaModel?.close()
//        println("Gemma model closed on ViewModel clear.")
//    }

    fun updateQueryInput(newQuery: String) {
        _queryInput.value = newQuery
    }

//    fun submitTextQuery(query: String) {
//        if (query.isBlank() || gemmaModel == null) {
//            println("Gemma model not initialized or query is blank. Cannot submit.")
//            return
//        }
//
//        val currentQuery = query
//        _queryInput.value = "" // Clear input immediately
//        viewModelScope.launch {
//            _appStatus.value = "Processing..."
//            println("Submitting text query to Gemma-3n: $currentQuery")
//
//            try { // Moved try-catch block to here to encompass the suspendCancellableCoroutine
//                val fullResponse = suspendCancellableCoroutine<String> { continuation ->
//                    val responseListener = object : LlmInference.ResponseListener {
//                        private val stringBuilder = StringBuilder()
//
//                        override fun onResponse(response: String) {
//                            stringBuilder.append(response)
//                            viewModelScope.launch { _gemmaResponseFlow.emit(stringBuilder.toString()) }
//                        }
//
//                        override fun onDone() {
//                            continuation.resume(stringBuilder.toString())
//                        }
//
//                        override fun onError(error: Throwable) {
//                            continuation.resumeWith(Result.failure(error))
//                        }
//                    }
//                    gemmaModel?.generateResponse(currentQuery, responseListener)
//                    continuation.invokeOnCancellation {
//                        println("Gemma generation cancelled for query: $currentQuery")
//                    }
//                }
//                _sceneDescription.value = fullResponse
//                _appStatus.value = "Status: Online"
//                println("Gemma response received: $fullResponse")
//            } catch (e: Exception) {
//                println("Error during Gemma inference: ${e.message}")
//                _sceneDescription.value = "Error processing: ${e.message}"
//                _appStatus.value = "Status: Error"
//            }
//        }
//    }


//    fun submitTextQuery(query: String) {
//        if (query.isBlank() || gemmaModel == null) {
//            println("Gemma model not initialized or query is blank. Cannot submit.")
//            return
//        }
//
//        _queryInput.value = "" // Clear input immediately
//
//        viewModelScope.launch {
//            _appStatus.value = "Processing..."
//            println("Submitting text query to Gemma-3n: $query")
//
//            try {
//                gemmaModel?.generateResponseAsync(query)
//                // The result listener set in createGemmaModel will emit partial and final outputs
//            } catch (e: Exception) {
//                println("Error during Gemma inference async: ${e.message}")
//                _sceneDescription.value = "Error processing: ${e.message}"
//                _appStatus.value = "Status: Error"
//            }
//        }
//    }

    fun submitTextQuery(query: String) {
//        if (query.isBlank() || gemmaModel == null) {
//            println("Gemma model not initialized or query is blank. Cannot submit.")
//            return
//        }

        _queryInput.value = "" // Clear input immediately
        _appStatus.value = "Processing..." // Update status before submitting

        println("Submitting text query to Gemma-3n: $query")

        try {
            // Call the async method. The listener defined in createGemmaModel will handle responses.
            //gemmaModel?.generateResponseAsync(query)
        } catch (e: Exception) {
            println("Error during Gemma inference async: ${e.message}")
            _sceneDescription.value = "Error processing: ${e.message}"
            _appStatus.value = "Status: Error"
        }
    }

//    fun toggleVoiceInput() {
//        // ViewModel only updates its internal recording state.
//        // The actual SpeechRecognizer start/stop logic will be in MainActivity,
//        // driven by observing this _isRecordingVoice state.
//        val newState = !_isRecordingVoice.value
//        _isRecordingVoice.value = newState
//        if (newState) {
//            println("ViewModel signals voice input START.")
//            // Simulate receiving voice input after a delay for demonstration
//            // This part will be replaced by actual SpeechRecognizer callbacks from MainActivity
//            viewModelScope.launch {
//                //delay(3000)
//                kotlinx.coroutines.delay(3000)
//                if (_isRecordingVoice.value) { // Check if still recording before processing simulated input
//                    val simulatedVoiceInput = "What's the current scene description?"
//                    println("Simulated voice input received: '$simulatedVoiceInput'")
//                    processVoiceInput(simulatedVoiceInput)
//                    // _isRecordingVoice.value = false // This will be handled by MainActivity's onResults/onError
//                }
//            }
//        } else {
//            println("ViewModel signals voice input STOP.")
//            // No need to set _isRecordingVoice.value = false here if it's already off.
//            // If it was ON, it means the user manually stopped it. MainActivity will then stop SR.
//        }
//    }

    fun toggleVoiceInput() {
        val newState = !_isRecordingVoice.value
        _isRecordingVoice.value = newState

        if (newState) {
            viewModelScope.launch {
                kotlinx.coroutines.delay(3000)
                if (_isRecordingVoice.value) {
                    processVoiceInput("What's around me?")
                }
            }
        }
    }

    fun processVoiceInput(text: String) {
        if (text.isNotBlank()) {
            _queryInput.value = text
            submitTextQuery(text)
        }
        _isRecordingVoice.value = false // Crucial: Stop recording state after processing
    }

    fun onAudioPermissionDenied() {
        _appStatus.value = "Status: Audio Denied"
        _sceneDescription.value = "Please grant audio permission for voice input."
        _isRecordingVoice.value = false // Ensure recording state is off
    }

//    private fun createGemmaModel(context: Context): LlmInference {
//        val options = LlmInferenceOptions.builder()
//            //.setModelPath("gemma-3n-E4B-it.task")
//            .setModelPath("/data/local/tmp/llm/gemma-3n.task")
//            //.setModelPath(modelPath)
//            .setEnableVisionModality(true)
//            .build()
//        return LlmInference.createFromOptions(getApplication(), options)
//    }

    // Corrected createGemmaModel to set the ResponseListener when creating the LlmInference instance
//    private fun createGemmaModel(context: Context, modelPath: String): LlmInference {
//        // Define the ResponseListener for LlmInference
//        val responseListener = object : ResponseListener {
//            private val stringBuilder = StringBuilder()
//
//            override fun onResponse(partialResult: String) {
//                stringBuilder.append(partialResult)
//                viewModelScope.launch {
//                    _gemmaResponseFlow.emit(stringBuilder.toString()) // Emit current partial/full result
//                }
//            }
//
//            override fun onDone() {
//                // When done, the full response is in stringBuilder.toString()
//                // Update sceneDescription with the final response
//                _sceneDescription.value = stringBuilder.toString()
//                _appStatus.value = "Status: Online" // Set status back to online
//                stringBuilder.clear() // Clear for next query
//                println("Gemma response (DONE): ${_sceneDescription.value}")
//            }
//
//            override fun onError(e: Throwable) {
//                println("Gemma Inference Error: ${e.message}")
//                _sceneDescription.value = "Error with AI: ${e.message}"
//                _appStatus.value = "Status: AI Error"
//                stringBuilder.clear() // Clear on error
//            }
//        }
//
//        val options = LlmInferenceOptions.builder()
//            .setModelPath(modelPath)
//            .build() // Build options without setResultListener here
//
//        // LlmInference.createFromOptions takes the context, options, AND the ResponseListener
//        return LlmInference.createFromOptions(context, options, responseListener)
//    }


    fun onSettingsClicked() {
        println("Settings button clicked from MainScreen ViewModel.")
    }
}