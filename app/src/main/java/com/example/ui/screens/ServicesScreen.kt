package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppStrings
import com.example.localization.Language
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun ServicesScreen(
    equipmentList: List<Equipment>,
    bookings: List<EquipmentBooking>,
    laborListings: List<LaborListing>,
    laborBookings: List<LaborBooking>,
    transportRequests: List<TransportRequest>,
    schemes: List<GovernmentScheme>,
    language: Language,
    onBookEquipment: (Equipment, Int, String) -> Unit,
    onBookLabor: (LaborListing, String, Int, String) -> Unit,
    onRequestTransport: (String, String, String, Double, String) -> Unit,
    onCalculateFertilizer: (String, Double) -> FertilizerRecommendation,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedServiceTab by remember { mutableStateOf(0) }
    var targetEquipmentForBooking by remember { mutableStateOf<Equipment?>(null) }
    var targetLaborForBooking by remember { mutableStateOf<LaborListing?>(null) }
    var showTransportDialog by remember { mutableStateOf(false) }

    // Soil calculator interactive state
    var calcCrop by remember { mutableStateOf("Paddy") }
    var calcAcresStr by remember { mutableStateOf("2.0") }
    var fertilizerRec by remember { mutableStateOf(onCalculateFertilizer("Paddy", 2.0)) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("services_screen")
    ) {
        // Tab Row with 5 Service Categories
        ScrollableTabRow(
            selectedTabIndex = selectedServiceTab,
            containerColor = PureWhiteCard,
            contentColor = ForestGreenPrimary,
            edgePadding = 16.dp,
            indicator = { tabPositions ->
                if (selectedServiceTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedServiceTab]),
                        height = 3.dp,
                        color = EmeraldGreenSecondary
                    )
                }
            },
            divider = { HorizontalDivider(color = SoftSlateBorder) }
        ) {
            Tab(
                selected = selectedServiceTab == 0,
                onClick = { selectedServiceTab = 0 },
                selectedContentColor = ForestGreenPrimary,
                unselectedContentColor = GreySecondaryText,
                text = { Text("🚜 " + AppStrings.equipmentRental.get(language).substringBefore("&").trim(), fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedServiceTab == 1,
                onClick = { selectedServiceTab = 1 },
                selectedContentColor = ForestGreenPrimary,
                unselectedContentColor = GreySecondaryText,
                text = { Text("👥 " + AppStrings.labourTitle.get(language).substringBefore(" ").trim(), fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedServiceTab == 2,
                onClick = { selectedServiceTab = 2 },
                selectedContentColor = ForestGreenPrimary,
                unselectedContentColor = GreySecondaryText,
                text = { Text("🚚 " + AppStrings.transportStorage.get(language), fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedServiceTab == 3,
                onClick = { selectedServiceTab = 3 },
                selectedContentColor = ForestGreenPrimary,
                unselectedContentColor = GreySecondaryText,
                text = { Text("🏛️ " + AppStrings.govtSchemesTitle.get(language), fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedServiceTab == 4,
                onClick = { selectedServiceTab = 4 },
                selectedContentColor = ForestGreenPrimary,
                unselectedContentColor = GreySecondaryText,
                text = { Text("🧪 Soil & Fertilizer", fontWeight = FontWeight.Bold) }
            )
        }

        when (selectedServiceTab) {
            0 -> {
                // Equipment Rental Subtab
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = SoftMintContainer),
                            border = BorderStroke(1.dp, EmeraldGreenSecondary.copy(alpha = 0.25f))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = PureWhiteCard,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("🚜", fontSize = 24.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (language == Language.KANNADA) "ಕೃಷಿ ಉಪಕರಣ ಬಾಡಿಗೆ ಕೇಂದ್ರ (CHC)" else "Custom Hiring Center (CHC)",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreenPrimary
                                        )
                                    )
                                    Text(
                                        text = if (language == Language.KANNADA) "ಗಂಟೆ ಅಥವಾ ದಿನದ ಲೆಕ್ಕದಲ್ಲಿ ಟ್ರ್ಯಾಕ್ಟರ್ ಮತ್ತು ಹಾರ್ವೆಸ್ಟರ್ ಬಾಡಿಗೆಗೆ ಪಡೆಯಿರಿ" else "Affordable farm mechanization with verified operators nearby",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DarkCharcoalText
                                    )
                                }
                            }
                        }
                    }

                    items(equipmentList, key = { it.id }) { eq ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                            border = BorderStroke(1.dp, SoftSlateBorder),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = SoftEmeraldSurface,
                                            modifier = Modifier.size(48.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(eq.emoji, fontSize = 26.sp)
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = if (language == Language.KANNADA) eq.nameKn else eq.nameEn,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = DarkCharcoalText
                                                )
                                            )
                                            Text(
                                                text = eq.hpOrCapacity,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = GreySecondaryText
                                            )
                                        }
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = SoftGoldContainer
                                    ) {
                                        Text(
                                            text = "★ ${eq.rating}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OnGoldContainer,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                HorizontalDivider(color = SoftSlateBorder)
                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "₹${eq.hourlyRate.toInt()} / Hour  •  ₹${eq.dailyRate.toInt()} / Day",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = ForestGreenPrimary
                                            )
                                        )
                                        Text(
                                            text = "📍 ${eq.location} • Owner: ${eq.ownerName}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GreySecondaryText
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    Button(
                                        onClick = { targetEquipmentForBooking = eq },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = EmeraldGreenSecondary,
                                            contentColor = PureWhiteCard
                                        )
                                    ) {
                                        Text(AppStrings.rentNow.get(language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    if (bookings.isNotEmpty()) {
                        item {
                            Text(
                                text = if (language == Language.KANNADA) "ನನ್ನ ಬುಕಿಂಗ್‌ಗಳು" else "My Equipment Bookings",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenPrimary
                                ),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        items(bookings) { b ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                                border = BorderStroke(1.dp, SoftSlateBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(b.equipmentName, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkCharcoalText)
                                        Text("${b.hours} Hours on ${b.date}", fontSize = 12.sp, color = GreySecondaryText)
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = SoftMintContainer
                                    ) {
                                        Text(
                                            text = "₹${b.totalCost.toInt()}",
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreenPrimary,
                                            fontSize = 13.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Agricultural Labour Booking Subtab
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = SoftGoldContainer),
                            border = BorderStroke(1.dp, GoldenYellowAccent.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = PureWhiteCard,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("👥", fontSize = 24.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = AppStrings.labourTitle.get(language),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = OnGoldContainer
                                        )
                                    )
                                    Text(
                                        text = if (language == Language.KANNADA) "ಭತ್ತ ನಾಟಿ, ಕಬ್ಬು ಕಟಾವು, ಕಳೆ ಕೀಳುವ ನುರಿತ ಕೃಷಿ ಕಾರ್ಮಿಕರ ತಂಡಗಳು" else "Hire skilled farm labor teams for transplanting, weeding, spraying, and harvesting",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = DarkCharcoalText
                                    )
                                }
                            }
                        }
                    }

                    items(laborListings, key = { it.id }) { l ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                            border = BorderStroke(1.dp, SoftSlateBorder),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(l.teamLeadName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DarkCharcoalText)
                                        Text(
                                            text = "📍 ${l.village} • Team Size: ${l.teamSize} Workers",
                                            fontSize = 12.sp,
                                            color = GreySecondaryText
                                        )
                                    }
                                    Surface(shape = RoundedCornerShape(8.dp), color = SoftMintContainer) {
                                        Text(
                                            text = "AVAILABLE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreenPrimary,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = SoftEmeraldSurface,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Skills: " + if (language == Language.KANNADA) l.skillsKn else l.skillsEn,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = ForestGreenPrimary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "₹${l.dailyWagePerWorker.toInt()} / Worker / Day",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DarkCharcoalText
                                    )
                                    Button(
                                        onClick = { targetLaborForBooking = l },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = EmeraldGreenSecondary,
                                            contentColor = PureWhiteCard
                                        )
                                    ) {
                                        Text(AppStrings.bookLabour.get(language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    if (laborBookings.isNotEmpty()) {
                        item {
                            Text(
                                text = if (language == Language.KANNADA) "ನನ್ನ ಕಾರ್ಮಿಕ ಬುಕಿಂಗ್‌ಗಳು" else "Active Labour Bookings",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenPrimary
                                ),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                        items(laborBookings) { lb ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                                border = BorderStroke(1.dp, SoftSlateBorder)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("${lb.teamLeadName} (${lb.workersCount} Workers)", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DarkCharcoalText)
                                        Text("${lb.activity} • Date: ${lb.date}", fontSize = 11.sp, color = GreySecondaryText)
                                    }
                                    Surface(shape = RoundedCornerShape(8.dp), color = SoftMintContainer) {
                                        Text(
                                            text = "₹${lb.totalWage.toInt()}",
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreenPrimary,
                                            fontSize = 13.sp,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Transport & Cold Storage Subtab
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = SoftMintContainer),
                            border = BorderStroke(1.dp, EmeraldGreenSecondary.copy(alpha = 0.25f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = PureWhiteCard,
                                        modifier = Modifier.size(46.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("🚚", fontSize = 24.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = if (language == Language.KANNADA) "ಕೃಷಿ ಸಾರಿಗೆ ಮತ್ತು ವಾಹನ ಬುಕಿಂಗ್" else "Farm-to-Mandi Produce Logistics",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = ForestGreenPrimary
                                            )
                                        )
                                        Text(
                                            text = if (language == Language.KANNADA) "ನಿಮ್ಮ ಊರಿನಿಂದ ಹತ್ತಿರದ ಎಪಿಎಂಸಿ ಮಂಡಿಗೆ ವಾಹನ ಬುಕ್ ಮಾಡಿ" else "On-demand mini-trucks, tractors, and lorries for farm harvest",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = DarkCharcoalText
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = { showTransportDialog = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = EmeraldGreenSecondary,
                                        contentColor = PureWhiteCard
                                    )
                                ) {
                                    Text(AppStrings.bookTransport.get(language), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = AppStrings.coldStorage.get(language),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary
                            )
                        )
                    }

                    val coldStorages = listOf(
                        "Kaveri Cold Storage (Capacity: 5000 MT)" to "Mandya Industrial Area • ₹180/Qtl/Month",
                        "Karnataka State Warehousing Corp (KSWC)" to "Maddur APMC Yard • Subsidized 20% for small farmers",
                        "GreenFresh Multi-Commodity Cold Chain" to "Mysore Road, Srirangapatna • Pre-cooling & grading available"
                    )

                    items(coldStorages) { (name, details) ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                            border = BorderStroke(1.dp, SoftSlateBorder),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = SoftTealTint,
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text("❄️", fontSize = 22.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DarkCharcoalText)
                                    Text(details, fontSize = 12.sp, color = GreySecondaryText)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedButton(
                                    onClick = { Toast.makeText(context, "Storage requested: $name", Toast.LENGTH_SHORT).show() },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, EmeraldGreenSecondary),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary)
                                ) {
                                    Text("Book", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    if (transportRequests.isNotEmpty()) {
                        item {
                            Text(
                                text = if (language == Language.KANNADA) "ಸಾರಿಗೆ ಬುಕಿಂಗ್ ಸ್ಥಿತಿ" else "Active Transport Requests",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenPrimary
                                )
                            )
                        }
                        items(transportRequests) { tr ->
                            Card(
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                                border = BorderStroke(1.dp, SoftSlateBorder)
                            ) {
                                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("${tr.vehicleType} • ${tr.cropName}", fontWeight = FontWeight.Bold, color = DarkCharcoalText)
                                        Surface(shape = RoundedCornerShape(8.dp), color = SoftGoldContainer) {
                                            Text(
                                                text = tr.status,
                                                color = OnGoldContainer,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text("Route: ${tr.pickupVillage} ➔ ${tr.destinationMandi}", fontSize = 12.sp, color = GreySecondaryText)
                                    Text(
                                        text = "Est. Fare: ₹${tr.estimatedFare.toInt()} (${tr.weightQuintals} Quintals)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = ForestGreenPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // Government Schemes Subtab
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(schemes, key = { it.id }) { s ->
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                            border = BorderStroke(1.dp, SoftSlateBorder),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = SoftGoldContainer,
                                        modifier = Modifier.size(38.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("🏛️", fontSize = 20.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = if (language == Language.KANNADA) s.titleKn else s.titleEn,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = ForestGreenPrimary
                                            )
                                        )
                                        Text(
                                            text = s.authority,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GreySecondaryText
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = SoftMintContainer,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "💰 Benefit: " + if (language == Language.KANNADA) s.benefitKn else s.benefitEn,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = ForestGreenPrimary
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = "✅ Eligibility: " + if (language == Language.KANNADA) s.eligibilityKn else s.eligibilityEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DarkCharcoalText
                                )
                                Text(
                                    text = "📄 Required Documents: " + if (language == Language.KANNADA) s.documentsKn else s.documentsEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GreySecondaryText
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = {
                                            try {
                                                val browserIntent = android.content.Intent(
                                                    android.content.Intent.ACTION_VIEW,
                                                    android.net.Uri.parse(s.officialUrl)
                                                )
                                                context.startActivity(browserIntent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Portal URL: " + s.officialUrl, Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = EmeraldGreenSecondary,
                                            contentColor = PureWhiteCard
                                        )
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(AppStrings.viewEligibility.get(language), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            4 -> {
                // Soil Health & Interactive NPK Fertilizer Calculator Subtab
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                            border = BorderStroke(1.dp, SoftSlateBorder),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = SoftMintContainer,
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("🧮", fontSize = 20.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = if (language == Language.KANNADA) "ಕೃಷಿ ರಸಗೊಬ್ಬರ ಮತ್ತು ಪೋಷಕಾಂಶ ಕ್ಯಾಲ್ಕುಲೇಟರ್" else "NPK Fertilizer Schedule Calculator",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = ForestGreenPrimary
                                        )
                                    )
                                }

                                Text(
                                    text = if (language == Language.KANNADA) "ಬೆಳೆ ಮತ್ತು ಜಮೀನಿನ ವಿಸ್ತೀರ್ಣ ನಮೂದಿಸಿ ರಸಗೊಬ್ಬರದ ಚೀಲಗಳ ಲೆಕ್ಕ ಪಡೆಯಿರಿ:" else "Enter crop and acreage to compute exact Urea, DAP, and MOP bags:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GreySecondaryText
                                )

                                val cropOptions = listOf("Paddy", "Sugarcane", "Maize", "Ragi")
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    cropOptions.forEach { c ->
                                        FilterChip(
                                            selected = calcCrop == c,
                                            onClick = {
                                                calcCrop = c
                                                val ac = calcAcresStr.toDoubleOrNull() ?: 2.0
                                                fertilizerRec = onCalculateFertilizer(c, ac)
                                            },
                                            label = { Text(c, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = SoftMintContainer,
                                                selectedLabelColor = ForestGreenPrimary,
                                                containerColor = PureWhiteCard,
                                                labelColor = GreySecondaryText
                                            ),
                                            border = FilterChipDefaults.filterChipBorder(
                                                enabled = true,
                                                selected = calcCrop == c,
                                                borderColor = SoftSlateBorder,
                                                selectedBorderColor = EmeraldGreenSecondary
                                            )
                                        )
                                    }
                                }

                                OutlinedTextField(
                                    value = calcAcresStr,
                                    onValueChange = {
                                        calcAcresStr = it
                                        val ac = it.toDoubleOrNull() ?: 1.0
                                        fertilizerRec = onCalculateFertilizer(calcCrop, ac)
                                    },
                                    label = { Text("Land Area (Acres)") },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = EmeraldGreenSecondary,
                                        unfocusedBorderColor = SoftSlateBorder,
                                        focusedLabelColor = ForestGreenPrimary,
                                        cursorColor = EmeraldGreenSecondary
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = SoftMintContainer,
                                    border = BorderStroke(1.dp, EmeraldGreenSecondary.copy(alpha = 0.25f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(
                                            text = "Calculated Fertilizer Requirement (${fertilizerRec.acreage} Acres):",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = ForestGreenPrimary
                                        )
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Urea (46% N):", fontWeight = FontWeight.SemiBold, color = DarkCharcoalText)
                                            Text("${fertilizerRec.ureaBags} Bags (50kg)", fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                                        }
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("DAP (18-46-0):", fontWeight = FontWeight.SemiBold, color = DarkCharcoalText)
                                            Text("${fertilizerRec.dapBags} Bags", fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                                        }
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("MOP (60% K₂O):", fontWeight = FontWeight.SemiBold, color = DarkCharcoalText)
                                            Text("${fertilizerRec.mopBags} Bags", fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                                        }
                                        HorizontalDivider(color = EmeraldGreenSecondary.copy(alpha = 0.2f))
                                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                            Text("Estimated Fertilizer Cost:", fontWeight = FontWeight.Bold, color = DarkCharcoalText)
                                            Text("₹${fertilizerRec.estimatedCost.toInt()}", fontWeight = FontWeight.Bold, color = ForestGreenPrimary, fontSize = 15.sp)
                                        }
                                        Text(
                                            text = "💡 Tip: " + if (language == Language.KANNADA) fertilizerRec.micronutrientTipKn else fertilizerRec.micronutrientTipEn,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = DarkCharcoalText
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Card(
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = SoftEmeraldSurface),
                            border = BorderStroke(1.dp, EmeraldGreenSecondary.copy(alpha = 0.25f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = if (language == Language.KANNADA) "🌿 ಸಾವಯವ ಜೀವಾಮೃತ ತಯಾರಿಸುವ ವಿಧಾನ" else "🌿 Organic Jeevamrutha Recipe (Zero Budget)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = ForestGreenPrimary
                                )
                                Text(
                                    text = if (language == Language.KANNADA)
                                        "1. 200 ಲೀಟರ್ ನೀರಿಗೆ 10 ಕೆಜಿ ದೇಸಿ ಹಸುವಿನ ಸಗಣಿ ಮತ್ತು 10 ಲೀಟರ್ ಗಂಜಲ ಸೇರಿಸಿ.\n2. 2 ಕೆಜಿ ಬೆಲ್ಲ ಮತ್ತು 2 ಕೆಜಿ ದ್ವಿದಳ ಧಾನ್ಯದ ಹಿಟ್ಟು (ಕಡಲೆ/ಉದ್ದು) ಬೆರೆಸಿ.\n3. 1 ಹಿಡಿ ಹೊಲದ ಫಲವತ್ತಾದ ಮಣ್ಣು ಹಾಕಿ ದಿನಕ್ಕೆ 2 ಬಾರಿ ಬಲಗಡೆ ತಿರುಗಿಸಿ.\n4. 48 ಗಂಟೆಗಳ ನಂತರ ಎಕರೆಗೆ ನೀರಾವರಿ ಜೊತೆ ಹರಿಸಿ."
                                    else
                                        "1. Mix 10 kg native desi cow dung + 10 L cow urine in 200 L water.\n2. Add 2 kg jaggery + 2 kg pulse flour (gram flour).\n3. Add a handful of virgin forest/field bund soil.\n4. Stir clockwise twice daily; ready for irrigation drenching within 48 hours.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DarkCharcoalText,
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Equipment Booking Dialog
    targetEquipmentForBooking?.let { eq ->
        var hours by remember { mutableStateOf(4) }
        var date by remember { mutableStateOf("Tomorrow Morning (7:00 AM)") }
        AlertDialog(
            onDismissRequest = { targetEquipmentForBooking = null },
            containerColor = PureWhiteCard,
            titleContentColor = ForestGreenPrimary,
            textContentColor = DarkCharcoalText,
            shape = RoundedCornerShape(20.dp),
            title = { Text(AppStrings.rentNow.get(language), fontWeight = FontWeight.Bold, color = ForestGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(if (language == Language.KANNADA) eq.nameKn else eq.nameEn, fontWeight = FontWeight.Bold, color = DarkCharcoalText)
                    Text("Hourly Rate: ₹${eq.hourlyRate.toInt()} / Hour", color = GreySecondaryText, fontSize = 13.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Duration (Hours):", color = DarkCharcoalText, fontWeight = FontWeight.Medium)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledTonalIconButton(
                                onClick = { if (hours > 1) hours -= 1 },
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = SoftMintContainer,
                                    contentColor = ForestGreenPrimary
                                )
                            ) { Text("-", fontWeight = FontWeight.Bold) }
                            Text("$hours Hours", modifier = Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold, color = DarkCharcoalText)
                            FilledTonalIconButton(
                                onClick = { hours += 1 },
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = SoftMintContainer,
                                    contentColor = ForestGreenPrimary
                                )
                            ) { Text("+", fontWeight = FontWeight.Bold) }
                        }
                    }

                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Booking Date & Time") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreenSecondary,
                            unfocusedBorderColor = SoftSlateBorder,
                            focusedLabelColor = ForestGreenPrimary,
                            cursorColor = EmeraldGreenSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SoftMintContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estimated Total:", color = DarkCharcoalText, fontWeight = FontWeight.Medium)
                            Text("₹${(hours * eq.hourlyRate).toInt()}", fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onBookEquipment(eq, hours, date)
                        targetEquipmentForBooking = null
                        Toast.makeText(context, "Equipment booking confirmed!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreenSecondary,
                        contentColor = PureWhiteCard
                    )
                ) {
                    Text("Confirm Booking", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { targetEquipmentForBooking = null },
                    colors = ButtonDefaults.textButtonColors(contentColor = GreySecondaryText)
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Labor Booking Dialog
    targetLaborForBooking?.let { l ->
        var activity by remember { mutableStateOf("Paddy Transplanting") }
        var workers by remember { mutableStateOf(l.teamSize) }
        var date by remember { mutableStateOf("Day after tomorrow") }
        val total = workers * l.dailyWagePerWorker

        AlertDialog(
            onDismissRequest = { targetLaborForBooking = null },
            containerColor = PureWhiteCard,
            titleContentColor = ForestGreenPrimary,
            textContentColor = DarkCharcoalText,
            shape = RoundedCornerShape(20.dp),
            title = { Text(AppStrings.bookLabour.get(language), fontWeight = FontWeight.Bold, color = ForestGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(l.teamLeadName, fontWeight = FontWeight.Bold, color = DarkCharcoalText)
                    Text("Daily Wage: ₹${l.dailyWagePerWorker.toInt()} per worker", color = GreySecondaryText, fontSize = 13.sp)

                    OutlinedTextField(
                        value = activity,
                        onValueChange = { activity = it },
                        label = { Text("Farm Operation (e.g. Weeding, Harvesting)") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreenSecondary,
                            unfocusedBorderColor = SoftSlateBorder,
                            focusedLabelColor = ForestGreenPrimary,
                            cursorColor = EmeraldGreenSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Workers Needed:", color = DarkCharcoalText, fontWeight = FontWeight.Medium)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            FilledTonalIconButton(
                                onClick = { if (workers > 1) workers -= 1 },
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = SoftMintContainer,
                                    contentColor = ForestGreenPrimary
                                )
                            ) { Text("-", fontWeight = FontWeight.Bold) }
                            Text("$workers", modifier = Modifier.padding(horizontal = 8.dp), fontWeight = FontWeight.Bold, color = DarkCharcoalText)
                            FilledTonalIconButton(
                                onClick = { if (workers < l.teamSize) workers += 1 },
                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                    containerColor = SoftMintContainer,
                                    contentColor = ForestGreenPrimary
                                )
                            ) { Text("+", fontWeight = FontWeight.Bold) }
                        }
                    }

                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Required Date") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreenSecondary,
                            unfocusedBorderColor = SoftSlateBorder,
                            focusedLabelColor = ForestGreenPrimary,
                            cursorColor = EmeraldGreenSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SoftMintContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Estimated Total Wage:", color = DarkCharcoalText, fontWeight = FontWeight.Medium)
                            Text("₹${total.toInt()}", fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onBookLabor(l, activity, workers, date)
                        targetLaborForBooking = null
                        Toast.makeText(context, "Labour team booked successfully!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreenSecondary,
                        contentColor = PureWhiteCard
                    )
                ) {
                    Text("Confirm Hire", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { targetLaborForBooking = null },
                    colors = ButtonDefaults.textButtonColors(contentColor = GreySecondaryText)
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Request Transport Dialog
    if (showTransportDialog) {
        var pickup by remember { mutableStateOf("Srirangapatna Village") }
        var mandi by remember { mutableStateOf("Mandya APMC Yard") }
        var crop by remember { mutableStateOf("Paddy (25 Quintals)") }
        var weightStr by remember { mutableStateOf("25") }
        var vehicle by remember { mutableStateOf("Tata Ace Mini Truck") }

        AlertDialog(
            onDismissRequest = { showTransportDialog = false },
            containerColor = PureWhiteCard,
            titleContentColor = ForestGreenPrimary,
            textContentColor = DarkCharcoalText,
            shape = RoundedCornerShape(20.dp),
            title = { Text(AppStrings.bookTransport.get(language), fontWeight = FontWeight.Bold, color = ForestGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = pickup,
                        onValueChange = { pickup = it },
                        label = { Text("Pickup Location / Farm") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreenSecondary,
                            unfocusedBorderColor = SoftSlateBorder,
                            focusedLabelColor = ForestGreenPrimary,
                            cursorColor = EmeraldGreenSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mandi,
                        onValueChange = { mandi = it },
                        label = { Text("Destination APMC Mandi") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreenSecondary,
                            unfocusedBorderColor = SoftSlateBorder,
                            focusedLabelColor = ForestGreenPrimary,
                            cursorColor = EmeraldGreenSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = crop,
                        onValueChange = { crop = it },
                        label = { Text("Produce & Quantity") },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreenSecondary,
                            unfocusedBorderColor = SoftSlateBorder,
                            focusedLabelColor = ForestGreenPrimary,
                            cursorColor = EmeraldGreenSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val w = weightStr.toDoubleOrNull() ?: 20.0
                        onRequestTransport(pickup, mandi, crop, w, vehicle)
                        showTransportDialog = false
                        Toast.makeText(context, "Transport request assigned to nearby driver!", Toast.LENGTH_LONG).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreenSecondary,
                        contentColor = PureWhiteCard
                    )
                ) {
                    Text("Submit Request", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showTransportDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = GreySecondaryText)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
