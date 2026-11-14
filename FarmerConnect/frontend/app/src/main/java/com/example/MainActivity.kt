package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.localization.AppStrings
import com.example.localization.Language
import com.example.ui.components.FarmerConnectBottomBar
import com.example.ui.components.FarmerConnectTopBar
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(0xFF14532D.toInt()),
            navigationBarStyle = SystemBarStyle.light(0xFFFFFFFF.toInt(), 0xFFFFFFFF.toInt())
        )
        setContent {
            MyApplicationTheme {
                FarmerConnectApp()
            }
        }
    }
}

@Composable
fun FarmerConnectApp(
    viewModel: MainViewModel = viewModel()
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val language by viewModel.currentLanguage.collectAsState()
    val currentUser by viewModel.repository.currentUser.collectAsState()
    val products by viewModel.repository.products.collectAsState()
    val orders by viewModel.repository.orders.collectAsState()
    val mandiPrices by viewModel.repository.mandiPrices.collectAsState()
    val weather by viewModel.repository.weather.collectAsState()
    val isWeatherRefreshing by viewModel.isWeatherRefreshing.collectAsState()
    val equipmentList by viewModel.repository.equipmentList.collectAsState()
    val bookings by viewModel.repository.equipmentBookings.collectAsState()
    val laborListings by viewModel.repository.laborListings.collectAsState()
    val laborBookings by viewModel.repository.laborBookings.collectAsState()
    val transportRequests by viewModel.repository.transportRequests.collectAsState()
    val schemes by viewModel.repository.schemes.collectAsState()
    val transactions by viewModel.repository.transactions.collectAsState()
    val chatMessages by viewModel.repository.chatMessages.collectAsState()
    val notifications by viewModel.repository.notifications.collectAsState()
    val adminMetric by viewModel.repository.adminMetrics.collectAsState()

    val isListening by viewModel.isListening.collectAsState()
    val isSpeaking by viewModel.isTtsSpeaking.collectAsState()
    val isThinking by viewModel.isAiThinking.collectAsState()
    val isDiagnosing by viewModel.isDiagnosing.collectAsState()
    val latestDiagnosis by viewModel.latestDiagnosis.collectAsState()

    var showNotificationsDialog by remember { mutableStateOf(false) }

    // Handle Android system back button
    BackHandler(enabled = selectedTab != 0) {
        viewModel.selectTab(0)
    }

    Scaffold(
        containerColor = WarmCreamBackground,
        topBar = {
            FarmerConnectTopBar(
                user = currentUser,
                currentLanguage = language,
                notifications = notifications,
                onToggleLanguage = { viewModel.toggleLanguage() },
                onToggleRole = { viewModel.toggleRole() },
                onNotificationsClick = {
                    showNotificationsDialog = true
                    viewModel.markNotificationsRead()
                }
            )
        },
        bottomBar = {
            FarmerConnectBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { viewModel.selectTab(it) },
                language = language
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)

        when (selectedTab) {
            0 -> HomeScreen(
                user = currentUser,
                weather = weather,
                isWeatherRefreshing = isWeatherRefreshing,
                mandiPrices = mandiPrices,
                language = language,
                onRefreshWeather = { viewModel.refreshWeather() },
                onNavigateToTab = { viewModel.selectTab(it) },
                onAskQuestion = { q -> viewModel.sendChatMessage(q, false) },
                modifier = screenModifier
            )
            1 -> MarketplaceScreen(
                user = currentUser,
                products = products,
                orders = orders,
                language = language,
                onAddProduct = { viewModel.repository.addProduct(it) },
                onPlaceOrder = { prod, qty, addr -> viewModel.repository.placeOrder(prod, qty, addr) },
                onMarkOrderPaid = { orderId, txnId -> viewModel.markOrderPaid(orderId, txnId) },
                onCancelOrder = { viewModel.repository.cancelOrder(it) },
                onAddReview = { prodId, reviewer, rating, comment -> viewModel.addReview(prodId, reviewer, rating, comment) },
                modifier = screenModifier
            )
            2 -> KrishiAiScreen(
                messages = chatMessages,
                isListening = isListening,
                isSpeaking = isSpeaking,
                isThinking = isThinking,
                isDiagnosing = isDiagnosing,
                latestDiagnosis = latestDiagnosis,
                language = language,
                onSendMessage = { text, speak -> viewModel.sendChatMessage(text, speak) },
                onStartVoice = {
                    viewModel.startVoiceListening { voiceText ->
                        viewModel.sendChatMessage(voiceText, true)
                    }
                },
                onStopVoice = { viewModel.stopVoiceListening() },
                onSpeakMessage = { viewModel.speakText(it) },
                onStopSpeak = { viewModel.stopSpeaking() },
                onClearChat = { viewModel.repository.clearChat() },
                onDiagnoseCrop = { crop, bitmap -> viewModel.diagnoseDisease(crop, bitmap) },
                onClearDiagnosis = { viewModel.clearDiagnosis() },
                modifier = screenModifier
            )
            3 -> ServicesScreen(
                equipmentList = equipmentList,
                bookings = bookings,
                laborListings = laborListings,
                laborBookings = laborBookings,
                transportRequests = transportRequests,
                schemes = schemes,
                language = language,
                onBookEquipment = { eq, hrs, date -> viewModel.repository.bookEquipment(eq, hrs, date) },
                onBookLabor = { listing, act, wrk, date -> viewModel.bookLabor(listing, act, wrk, date) },
                onRequestTransport = { p, d, c, w, v -> viewModel.repository.requestTransport(p, d, c, w, v) },
                onCalculateFertilizer = { c, a -> viewModel.repository.calculateFertilizer(c, a) },
                modifier = screenModifier
            )
            4 -> ProfileScreen(
                user = currentUser,
                transactions = transactions,
                adminMetric = adminMetric,
                language = language,
                onToggleRole = { viewModel.toggleRole() },
                onSelectRole = { viewModel.setRole(it) },
                onLogin = { name, phone, role -> viewModel.login(name, phone, role) },
                onUpdateProfile = { name, vill, dist, acres -> viewModel.updateProfile(name, vill, dist, acres) },
                onSelectLanguage = { viewModel.setLanguage(it) },
                onAddTransaction = { t, a, inc, cat -> viewModel.repository.addTransaction(t, a, inc, cat) },
                modifier = screenModifier
            )
        }
    }

    // In-App Notification Center Dialog
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            containerColor = PureWhiteCard,
            titleContentColor = ForestGreenPrimary,
            textContentColor = DarkCharcoalText,
            shape = RoundedCornerShape(20.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(shape = RoundedCornerShape(8.dp), color = SoftGoldContainer) {
                        Text(
                            text = "ALERT",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnGoldContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(
                        text = AppStrings.notifications.get(language),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = ForestGreenPrimary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    notifications.forEach { n ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmCreamBackground),
                            border = BorderStroke(1.dp, SoftSlateBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(n.iconEmoji, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (language == Language.KANNADA) n.titleKn else n.titleEn,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = DarkCharcoalText
                                    )
                                    Text(
                                        text = if (language == Language.KANNADA) n.descriptionKn else n.descriptionEn,
                                        fontSize = 12.sp,
                                        color = GreySecondaryText
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(n.timestamp, fontSize = 10.sp, color = EmeraldGreenSecondary, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showNotificationsDialog = false },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreenSecondary,
                        contentColor = PureWhiteCard
                    )
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
