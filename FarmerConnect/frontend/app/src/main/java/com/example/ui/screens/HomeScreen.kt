package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.localization.AppStrings
import com.example.localization.Language
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    user: UserProfile,
    weather: WeatherAdvisory,
    isWeatherRefreshing: Boolean,
    mandiPrices: List<MandiPrice>,
    language: Language,
    onRefreshWeather: () -> Unit,
    onNavigateToTab: (Int) -> Unit,
    onAskQuestion: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedMandiForTrend by remember { mutableStateOf<MandiPrice?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 84.dp)
    ) {
        // Hero Banner Card with Welcome Message
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(190.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_farmer_banner),
                        contentDescription = "Farmland Landscape",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Subtle bottom scrim only for legible white text over bright farmland art
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0x3314532D),
                                        Color(0xCC14532D)
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (language == Language.KANNADA) "ನಮಸ್ಕಾರ, ${user.name}!" else "Namaskara, ${user.name}!",
                            color = PureWhiteCard,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = GoldenYellowAccent,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${user.village}, ${user.district}",
                                color = SoftMintContainer,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = GoldenYellowAccent
                            ) {
                                Text(
                                    text = "${user.landAcres} Acres",
                                    color = DarkCharcoalText,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Weather & 5-Day Forecast Card
        item {
            WeatherAdvisoryCard(
                weather = weather,
                isRefreshing = isWeatherRefreshing,
                language = language,
                onRefresh = onRefreshWeather,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        // Live Mandi Prices Ticker Section
        item {
            Column(modifier = Modifier.padding(top = 18.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📊", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = AppStrings.mandiTickerTitle.get(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkCharcoalText
                            )
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = SoftMintContainer
                    ) {
                        Text(
                            text = "APMC Live",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = ForestGreenPrimary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(mandiPrices) { price ->
                        MandiTickerItem(
                            price = price,
                            language = language,
                            onClick = { selectedMandiForTrend = price }
                        )
                    }
                }
            }
        }

        // Quick Action Grid (Pure White Cards with Tinted Icon Badges)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
                Text(
                    text = AppStrings.quickActions.get(language),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkCharcoalText
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionButton(
                        icon = "🤖",
                        title = AppStrings.askKrishiAi.get(language),
                        subtitle = "Gemini Powered",
                        iconBadgeColor = SoftMintContainer,
                        onClick = { onNavigateToTab(2) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = "🌾",
                        title = AppStrings.sellProduce.get(language),
                        subtitle = "Direct Buyers & UPI",
                        iconBadgeColor = SoftEmeraldSurface,
                        onClick = { onNavigateToTab(1) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionButton(
                        icon = "🚜",
                        title = AppStrings.rentEquipment.get(language),
                        subtitle = "Tractors & Harvester",
                        iconBadgeColor = SoftGoldContainer,
                        onClick = { onNavigateToTab(3) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = "👥",
                        title = AppStrings.farmLabour.get(language),
                        subtitle = "Transplanting Teams",
                        iconBadgeColor = SoftAmberContainer,
                        onClick = { onNavigateToTab(3) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionButton(
                        icon = "🔬",
                        title = AppStrings.cropDoctor.get(language),
                        subtitle = "AI Leaf Disease Scan",
                        iconBadgeColor = SoftMintContainer,
                        onClick = { onNavigateToTab(2) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionButton(
                        icon = "🧪",
                        title = "NPK Calculator",
                        subtitle = "Fertilizer Schedule",
                        iconBadgeColor = SoftTealTint,
                        onClick = { onNavigateToTab(3) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Suggested AI Questions Carousel
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("💡", fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = AppStrings.suggestedQuestions.get(language),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DarkCharcoalText
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                val questions = listOf(
                    AppStrings.q1.get(language),
                    AppStrings.q2.get(language),
                    AppStrings.q3.get(language),
                    AppStrings.q4.get(language)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(questions) { q ->
                        Card(
                            onClick = {
                                onAskQuestion(q)
                                onNavigateToTab(2)
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                            border = BorderStroke(1.dp, SoftSlateBorder),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.widthIn(max = 280.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(SoftGoldContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("❓", fontSize = 14.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = q,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = DarkCharcoalText
                                    ),
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }
        }

        // Government Scheme Highlight Banner (Forest Green with Golden Yellow accent)
        item {
            Card(
                onClick = { onNavigateToTab(3) },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreenPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(GoldenYellowAccent),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🏛️", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == Language.KANNADA) "ಪಿಎಂ-ಕಿಸಾನ್ ಮತ್ತು ಪಿಎಂ-ಕುಸುಮ್ ಯೋಜನೆಗಳು" else "PM-KISAN & PM-KUSUM Schemes",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PureWhiteCard
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (language == Language.KANNADA) "ಅರ್ಹತೆ ಪರಿಶೀಲಿಸಿ, 60% ಸೌರ ಪಂಪ್ ಸಬ್ಸಿಡಿ & ₹6,000 ನೇರ ಜಮೆ" else "Check eligibility, 60% solar pump subsidy & ₹6,000 direct transfer.",
                            style = MaterialTheme.typography.bodySmall,
                            color = SoftMintContainer
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "View Schemes",
                        tint = GoldenYellowAccent
                    )
                }
            }
        }
    }

    // Historical Price Trend Dialog
    selectedMandiForTrend?.let { mandi ->
        HistoricalPriceTrendDialog(
            mandi = mandi,
            language = language,
            onDismiss = { selectedMandiForTrend = null }
        )
    }
}

@Composable
fun WeatherAdvisoryCard(
    weather: WeatherAdvisory,
    isRefreshing: Boolean,
    language: Language,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
        border = BorderStroke(1.dp, SoftSlateBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SoftGoldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("⛅", fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${weather.temp}°C",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenPrimary
                                )
                            )
                            if (weather.isLiveApi) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(shape = RoundedCornerShape(6.dp), color = SoftMintContainer) {
                                    Text(
                                        text = "● LIVE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreenPrimary,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = if (language == Language.KANNADA) weather.conditionKn else weather.condition,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = DarkCharcoalText,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(SoftCreamVariant)
                ) {
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = EmeraldGreenSecondary
                        )
                    } else {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh Weather", tint = ForestGreenPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Metrics pill row (Humidity, Wind, Location)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WeatherMetricPill("💧 ${weather.humidity}% Humidity", Modifier.weight(1f))
                WeatherMetricPill("💨 ${weather.windKmh} km/h Wind", Modifier.weight(1f))
            }

            Text(
                text = "📍 ${weather.location}",
                style = MaterialTheme.typography.labelSmall,
                color = GreySecondaryText,
                modifier = Modifier.padding(top = 8.dp)
            )

            // 5-Day Mini Forecast Strip
            if (weather.forecasts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(WarmCreamBackground)
                        .padding(vertical = 8.dp, horizontal = 6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    weather.forecasts.take(5).forEach { fc ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(fc.day, fontSize = 10.sp, color = GreySecondaryText, fontWeight = FontWeight.SemiBold)
                            Text("${fc.tempMax}°", fontSize = 13.sp, color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
                            Text("${fc.rainProb}% 💧", fontSize = 10.sp, color = EmeraldGreenSecondary)
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = SoftSlateBorder)

            // Irrigation alert callout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftEmeraldSurface)
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🌱", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == Language.KANNADA) weather.irrigationAdviceKn else weather.irrigationAdviceEn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = ForestGreenPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }
    }
}

@Composable
private fun WeatherMetricPill(label: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = WarmCreamBackground,
        border = BorderStroke(1.dp, SoftSlateBorder),
        modifier = modifier
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = DarkCharcoalText,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun MandiTickerItem(
    price: MandiPrice,
    language: Language,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val badgeBg = when (price.trend) {
        PriceTrend.UP -> SoftMintContainer
        PriceTrend.DOWN -> SoftRedContainer
        PriceTrend.STABLE -> SoftAmberContainer
    }
    val badgeFg = when (price.trend) {
        PriceTrend.UP -> ForestGreenPrimary
        PriceTrend.DOWN -> OnRedContainer
        PriceTrend.STABLE -> OnAmberContainer
    }
    val trendLabel = when (price.trend) {
        PriceTrend.UP -> "▲ +${price.percentChange}%"
        PriceTrend.DOWN -> "▼ ${price.percentChange}%"
        PriceTrend.STABLE -> "● 0.0%"
    }

    Card(
        onClick = onClick,
        modifier = modifier.width(178.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
        border = BorderStroke(1.dp, SoftSlateBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = if (language == Language.KANNADA) price.cropNameKn else price.cropNameEn,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkCharcoalText
                ),
                maxLines = 1
            )
            Text(
                text = price.market,
                style = MaterialTheme.typography.labelSmall,
                color = GreySecondaryText,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${price.modalPrice.toInt()}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                    )
                    Text(
                        text = if (language == Language.KANNADA) "/ ಕ್ವಿಂಟಾಲ್" else "/ Quintal",
                        style = MaterialTheme.typography.labelSmall,
                        color = GreySecondaryText
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = badgeBg
                ) {
                    Text(
                        text = trendLabel,
                        color = badgeFg,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HistoricalPriceTrendDialog(
    mandi: MandiPrice,
    language: Language,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhiteCard,
        titleContentColor = ForestGreenPrimary,
        textContentColor = DarkCharcoalText,
        title = {
            Column {
                Text(
                    text = if (language == Language.KANNADA) mandi.cropNameKn else mandi.cropNameEn,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = ForestGreenPrimary
                )
                Text(
                    text = "${mandi.market} • ${AppStrings.viewHistoricalPrices.get(language)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldGreenSecondary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PriceStatBox("Min Price", "₹${mandi.minPrice.toInt()}", SoftCreamVariant, DarkCharcoalText)
                    PriceStatBox("Modal Price", "₹${mandi.modalPrice.toInt()}", SoftMintContainer, ForestGreenPrimary)
                    PriceStatBox("Max Price", "₹${mandi.maxPrice.toInt()}", SoftGoldContainer, OnGoldContainer)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = SoftSlateBorder)

                Text(
                    text = if (language == Language.KANNADA) "ಕಳೆದ 7 ದಿನಗಳ ಬೆಲೆ ವ್ಯತ್ಯಾಸ (ಪ್ರತಿ ಕ್ವಿಂಟಾಲ್‌ಗೆ):" else "Past 7 Days APMC Recorded Modal Prices:",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkCharcoalText
                )

                mandi.history.forEach { pt ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(pt.date, fontSize = 12.sp, color = GreySecondaryText)
                        Text("₹${pt.price.toInt()} / Qtl", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                    }
                }

                Text(
                    text = "Source: APMC Karnataka & Agmarknet Daily Reporting",
                    style = MaterialTheme.typography.labelSmall,
                    color = GreySecondaryText,
                    modifier = Modifier.padding(top = 6.dp)
                )
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

@Composable
fun PriceStatBox(label: String, value: String, bgColor: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = bgColor,
        modifier = Modifier.padding(2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, color = GreySecondaryText)
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}

@Composable
fun QuickActionButton(
    icon: String,
    title: String,
    subtitle: String,
    iconBadgeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
        border = BorderStroke(1.dp, SoftSlateBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBadgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(icon, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkCharcoalText
                ),
                maxLines = 1
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = GreySecondaryText,
                maxLines = 1
            )
        }
    }
}
