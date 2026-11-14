package com.example.ui.viewmodel

import android.app.Application
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.FarmerRepository
import com.example.data.KrishiAiEngine
import com.example.localization.Language
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val repository = FarmerRepository()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _currentLanguage = MutableStateFlow(Language.ENGLISH)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _isTtsSpeaking = MutableStateFlow(false)
    val isTtsSpeaking: StateFlow<Boolean> = _isTtsSpeaking.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    private val _isDiagnosing = MutableStateFlow(false)
    val isDiagnosing: StateFlow<Boolean> = _isDiagnosing.asStateFlow()

    private val _latestDiagnosis = MutableStateFlow<DiseaseDiagnosis?>(null)
    val latestDiagnosis: StateFlow<DiseaseDiagnosis?> = _latestDiagnosis.asStateFlow()

    private val _isWeatherRefreshing = MutableStateFlow(false)
    val isWeatherRefreshing: StateFlow<Boolean> = _isWeatherRefreshing.asStateFlow()

    private var speechRecognizer: SpeechRecognizer? = null
    private var textToSpeech: TextToSpeech? = null
    private var ttsReady = false

    init {
        initTts(application)
        refreshWeather()
    }

    fun refreshWeather() {
        viewModelScope.launch {
            _isWeatherRefreshing.value = true
            repository.refreshWeather()
            _isWeatherRefreshing.value = false
        }
    }

    private fun initTts(app: Application) {
        textToSpeech = TextToSpeech(app) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
                textToSpeech?.language = Locale("kn", "IN")
            }
        }
    }

    fun selectTab(tabIndex: Int) {
        _selectedTab.value = tabIndex
    }

    fun toggleLanguage() {
        val next = if (_currentLanguage.value == Language.ENGLISH) Language.KANNADA else Language.ENGLISH
        _currentLanguage.value = next
        if (ttsReady) {
            val locale = if (next == Language.KANNADA) Locale("kn", "IN") else Locale.US
            textToSpeech?.language = locale
        }
    }

    fun setLanguage(language: Language) {
        _currentLanguage.value = language
        if (ttsReady) {
            val locale = if (language == Language.KANNADA) Locale("kn", "IN") else Locale.US
            textToSpeech?.language = locale
        }
    }

    fun toggleRole() {
        repository.toggleRole()
    }

    fun setRole(role: UserRole) {
        repository.setRole(role)
    }

    fun login(name: String, phone: String, role: UserRole) {
        repository.loginUser(name, phone, role)
    }

    fun updateProfile(name: String, village: String, district: String, acres: Double) {
        repository.updateProfile(name, village, district, acres)
    }

    fun sendChatMessage(text: String, speakAloud: Boolean = false) {
        if (text.isBlank()) return

        val userMsg = ChatMessage(text = text, isUser = true)
        repository.addChatMessage(userMsg)

        viewModelScope.launch {
            _isAiThinking.value = true
            val aiResponse = KrishiAiEngine.askKrishi(
                userPrompt = text,
                history = repository.chatMessages.value,
                language = _currentLanguage.value
            )
            _isAiThinking.value = false

            val botMsg = ChatMessage(text = aiResponse, isUser = false)
            repository.addChatMessage(botMsg)

            if (speakAloud) {
                speakText(aiResponse)
            }
        }
    }

    fun speakText(text: String) {
        if (!ttsReady || textToSpeech == null) return
        stopSpeaking()
        _isTtsSpeaking.value = true
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "KrishiTTS")
    }

    fun stopSpeaking() {
        textToSpeech?.stop()
        _isTtsSpeaking.value = false
    }

    fun startVoiceListening(onResult: (String) -> Unit) {
        val context = getApplication<Application>()
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            val sampleQuery = if (_currentLanguage.value == Language.KANNADA) {
                "ಮೆಕ್ಕೆಜೋಳದಲ್ಲಿ ಸೈನಿಕ ಹುಳು ನಿಯಂತ್ರಣ ಹೇಗೆ?"
            } else {
                "How to control fall armyworm in maize?"
            }
            onResult(sampleQuery)
            return
        }

        try {
            speechRecognizer?.destroy()
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _isListening.value = true
                    }
                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {
                        _isListening.value = false
                    }
                    override fun onError(error: Int) {
                        _isListening.value = false
                    }
                    override fun onResults(results: Bundle?) {
                        _isListening.value = false
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull() ?: ""
                        if (text.isNotBlank()) {
                            onResult(text)
                        }
                    }
                    override fun onPartialResults(partialResults: Bundle?) {}
                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                val langTag = if (_currentLanguage.value == Language.KANNADA) "kn-IN" else "en-IN"
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, langTag)
            }
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            _isListening.value = false
        }
    }

    fun stopVoiceListening() {
        speechRecognizer?.stopListening()
        _isListening.value = false
    }

    fun diagnoseDisease(cropHint: String, bitmap: Bitmap?) {
        viewModelScope.launch {
            _isDiagnosing.value = true
            val result = KrishiAiEngine.diagnosePlantDisease(cropHint, bitmap, _currentLanguage.value)
            _latestDiagnosis.value = result
            _isDiagnosing.value = false
        }
    }

    fun clearDiagnosis() {
        _latestDiagnosis.value = null
    }

    fun markOrderPaid(orderId: String, upiTxnId: String) {
        repository.markOrderAsPaid(orderId, upiTxnId)
    }

    fun addReview(productId: String, reviewerName: String, rating: Int, comment: String) {
        repository.addProductReview(productId, reviewerName, rating, comment)
    }

    fun bookLabor(listing: LaborListing, activity: String, workers: Int, date: String) {
        repository.bookLabor(listing, activity, workers, date)
    }

    fun markNotificationsRead() {
        repository.markNotificationsAsRead()
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}
