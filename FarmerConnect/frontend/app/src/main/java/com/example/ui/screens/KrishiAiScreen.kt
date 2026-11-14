package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.localization.AppStrings
import com.example.localization.Language
import com.example.model.ChatMessage
import com.example.model.DiseaseDiagnosis
import com.example.ui.theme.*

@Composable
fun KrishiAiScreen(
    messages: List<ChatMessage>,
    isListening: Boolean,
    isSpeaking: Boolean,
    isThinking: Boolean,
    isDiagnosing: Boolean,
    latestDiagnosis: DiseaseDiagnosis?,
    language: Language,
    onSendMessage: (String, Boolean) -> Unit,
    onStartVoice: () -> Unit,
    onStopVoice: () -> Unit,
    onSpeakMessage: (String) -> Unit,
    onStopSpeak: () -> Unit,
    onClearChat: () -> Unit,
    onDiagnoseCrop: (String, Bitmap?) -> Unit,
    onClearDiagnosis: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var inputText by remember { mutableStateOf("") }
    var showCropDoctorDialog by remember { mutableStateOf(false) }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            onStartVoice()
        } else {
            Toast.makeText(context, "Microphone permission is required for voice chat", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(messages.size, isThinking) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
    ) {
        // Chat Header
        Surface(
            color = PureWhiteCard,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(SoftMintContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🤖", fontSize = 20.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.chatbotTitle.get(language),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenPrimary
                                )
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(FreshGreenSuccess)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "Gemini AI • Kannada & English",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GreySecondaryText
                                )
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = { showCropDoctorDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldenYellowAccent,
                                contentColor = DarkCharcoalText
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text("🔬", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (language == Language.KANNADA) "ರೋಗ ಪರೀಕ್ಷೆ" else "Crop Doctor",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        IconButton(onClick = onClearChat) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Clear Chat", tint = GreySecondaryText)
                        }
                    }
                }
                HorizontalDivider(thickness = 1.dp, color = SoftSlateBorder)
            }
        }

        // Active Voice Listening Bar
        AnimatedVisibility(visible = isListening) {
            Surface(
                color = SoftMintContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(EmeraldGreenSecondary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Mic, contentDescription = null, tint = PureWhiteCard, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = AppStrings.listeningVoice.get(language),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary
                            )
                        )
                    }
                    TextButton(onClick = onStopVoice) {
                        Text("Cancel", color = SoftRedError, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Active TTS Speaking Bar
        AnimatedVisibility(visible = isSpeaking) {
            Surface(
                color = SoftGoldContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔊", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.KANNADA) "ಧ್ವನಿಯಲ್ಲಿ ಓದಲಾಗುತ್ತಿದೆ..." else "Speaking AI Answer aloud...",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnGoldContainer
                            )
                        )
                    }
                    IconButton(onClick = onStopSpeak, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Stop, contentDescription = "Stop TTS", tint = SoftRedError)
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubble(
                    message = msg,
                    language = language,
                    onPlayAudio = { onSpeakMessage(msg.text) }
                )
            }

            if (isThinking) {
                item {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = EmeraldGreenSecondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (language == Language.KANNADA) "ಕೃಷಿ AI ಉತ್ತರಿಸುತ್ತಿದೆ..." else "Krishi AI is analyzing...",
                            style = MaterialTheme.typography.bodySmall,
                            color = GreySecondaryText
                        )
                    }
                }
            }
        }

        // Suggested prompt chips row
        LazyRow(
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val chips = listOf(
                AppStrings.q1.get(language),
                AppStrings.q2.get(language),
                AppStrings.q3.get(language),
                AppStrings.q4.get(language)
            )
            items(chips) { prompt ->
                SuggestionChip(
                    onClick = { onSendMessage(prompt, false) },
                    label = { Text(prompt, maxLines = 1, fontSize = 11.sp, color = ForestGreenPrimary, fontWeight = FontWeight.Medium) },
                    shape = RoundedCornerShape(12.dp),
                    colors = SuggestionChipDefaults.suggestionChipColors(containerColor = PureWhiteCard),
                    border = BorderStroke(1.dp, SoftSlateBorder)
                )
            }
        }

        // Bottom Input Bar
        Surface(
            color = PureWhiteCard,
            shadowElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            Column {
                HorizontalDivider(thickness = 1.dp, color = SoftSlateBorder)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.RECORD_AUDIO
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasPermission) {
                                if (isListening) onStopVoice() else onStartVoice()
                            } else {
                                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(
                                if (isListening) SoftRedError else SoftMintContainer
                            )
                            .testTag("voice_mic_button")
                    ) {
                        Icon(
                            imageVector = if (isListening) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Voice Input",
                            tint = if (isListening) PureWhiteCard else ForestGreenPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text(AppStrings.typeQuestion.get(language), fontSize = 13.sp, color = GreySecondaryText) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input_text_field"),
                        shape = RoundedCornerShape(22.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = WarmCreamBackground,
                            unfocusedContainerColor = WarmCreamBackground,
                            focusedBorderColor = EmeraldGreenSecondary,
                            unfocusedBorderColor = SoftSlateBorder,
                            focusedTextColor = DarkCharcoalText,
                            unfocusedTextColor = DarkCharcoalText
                        ),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                val textToSend = inputText.trim()
                                inputText = ""
                                onSendMessage(textToSend, false)
                            }
                        },
                        enabled = inputText.isNotBlank(),
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(
                                if (inputText.isNotBlank()) EmeraldGreenSecondary else SoftSlateBorder
                            )
                            .testTag("send_chat_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Send",
                            tint = if (inputText.isNotBlank()) PureWhiteCard else GreySecondaryText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }

    if (showCropDoctorDialog) {
        CropDoctorDialog(
            language = language,
            isDiagnosing = isDiagnosing,
            latestDiagnosis = latestDiagnosis,
            onDismiss = {
                showCropDoctorDialog = false
                onClearDiagnosis()
            },
            onDiagnose = { cropName, bitmap ->
                onDiagnoseCrop(cropName, bitmap)
            }
        )
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    language: Language,
    onPlayAudio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isUser = message.isUser
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (isUser) 18.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 18.dp
            ),
            color = if (isUser) ForestGreenPrimary else PureWhiteCard,
            border = if (isUser) null else BorderStroke(1.dp, SoftSlateBorder),
            shadowElevation = if (isUser) 1.dp else 2.dp,
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                if (!isUser) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🌱", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Krishi AI",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldGreenSecondary
                            )
                        }

                        FilledTonalIconButton(
                            onClick = onPlayAudio,
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = SoftMintContainer,
                                contentColor = ForestGreenPrimary
                            ),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Play Audio", modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }

                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (isUser) PureWhiteCard else DarkCharcoalText,
                        lineHeight = 20.sp
                    )
                )

                if (message.isAiDisclaimer) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ℹ️ " + AppStrings.aiDisclaimer.get(language),
                        style = MaterialTheme.typography.labelSmall,
                        color = GreySecondaryText,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CropDoctorDialog(
    language: Language,
    isDiagnosing: Boolean,
    latestDiagnosis: DiseaseDiagnosis?,
    onDismiss: () -> Unit,
    onDiagnose: (String, Bitmap?) -> Unit
) {
    val context = LocalContext.current
    var selectedCrop by remember { mutableStateOf("Paddy") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        if (uri != null) {
            try {
                val bmp = if (Build.VERSION.SDK_INT < 28) {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val src = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(src)
                }
                selectedBitmap = bmp
            } catch (e: Exception) {
                selectedBitmap = null
            }
        }
    }

    val crops = listOf(
        "Paddy" to "ಭತ್ತ (Paddy)",
        "Maize" to "ಮೆಕ್ಕೆಜೋಳ (Maize)",
        "Sugarcane" to "ಕಬ್ಬು (Sugarcane)",
        "Tomato" to "ಟೊಮ್ಯಾಟೊ (Tomato)"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhiteCard,
        titleContentColor = ForestGreenPrimary,
        textContentColor = DarkCharcoalText,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔬", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AppStrings.diseaseDiagnosis.get(language), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ForestGreenPrimary)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = if (language == Language.KANNADA) "ಬೆಳೆಯನ್ನು ಆಯ್ಕೆಮಾಡಿ ಅಥವಾ ಎಲೆಯ ಫೋಟೋ ಅಪ್‌ಲೋಡ್ ಮಾಡಿ:" else "Select crop or choose leaf photo from gallery:",
                    style = MaterialTheme.typography.bodySmall,
                    color = GreySecondaryText
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(crops) { (key, label) ->
                        val isSel = selectedCrop == key
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedCrop = key },
                            label = { Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                            shape = RoundedCornerShape(10.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ForestGreenPrimary,
                                selectedLabelColor = PureWhiteCard,
                                containerColor = WarmCreamBackground,
                                labelColor = DarkCharcoalText
                            )
                        )
                    }
                }

                OutlinedButton(
                    onClick = { photoPickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, EmeraldGreenSecondary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary)
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (selectedImageUri != null) "Photo Attached ✓" else AppStrings.uploadPhoto.get(language),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = { onDiagnose(selectedCrop, selectedBitmap) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSecondary, contentColor = PureWhiteCard)
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = "Scan")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(AppStrings.analyzeCrop.get(language), fontWeight = FontWeight.Bold)
                }

                if (isDiagnosing) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = EmeraldGreenSecondary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("AI Vision analyzing plant pathology...", fontSize = 13.sp, color = DarkCharcoalText)
                    }
                }

                latestDiagnosis?.let { diag ->
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = SoftEmeraldSurface,
                        border = BorderStroke(1.dp, SoftMintContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (language == Language.KANNADA) diag.diseaseNameKn else diag.diseaseNameEn,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreenPrimary
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(shape = RoundedCornerShape(6.dp), color = SoftMintContainer) {
                                    Text(
                                        text = "${diag.confidenceScore}% Match",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreenPrimary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Text(
                                text = "⚠️ Symptoms: " + if (language == Language.KANNADA) diag.symptomsKn else diag.symptomsEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = DarkCharcoalText
                            )
                            Text(
                                text = "🌿 Organic: " + if (language == Language.KANNADA) diag.organicRemedyKn else diag.organicRemedyEn,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = ForestGreenPrimary
                            )
                            Text(
                                text = "💊 Spray: " + if (language == Language.KANNADA) diag.chemicalRemedyKn else diag.chemicalRemedyEn,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = OnGoldContainer
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SoftAmberContainer
                            ) {
                                Text(
                                    text = "⚠️ " + AppStrings.aiDisclaimer.get(language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = OnAmberContainer,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }

                // Toll-free helpline
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SoftGoldContainer)
                        .clickable {
                            try {
                                val dialIntent = android.content.Intent(
                                    android.content.Intent.ACTION_DIAL,
                                    android.net.Uri.parse("tel:18001801551")
                                )
                                context.startActivity(dialIntent)
                            } catch (e: Exception) {
                                Toast.makeText(context, "Kisan Call Center: 1800-180-1551", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📞", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.helplineContact.get(language),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = OnGoldContainer,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSecondary, contentColor = PureWhiteCard)
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}
