package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun ProfileScreen(
    user: UserProfile,
    transactions: List<FarmTransaction>,
    adminMetric: AdminMetric,
    language: Language,
    onToggleRole: () -> Unit,
    onSelectRole: (UserRole) -> Unit,
    onLogin: (String, String, UserRole) -> Unit,
    onUpdateProfile: (String, String, String, Double) -> Unit,
    onSelectLanguage: (Language) -> Unit,
    onAddTransaction: (String, Double, Boolean, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showAddTxnDialog by remember { mutableStateOf(false) }
    var showAuthDialog by remember { mutableStateOf(false) }
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var backendApiUrl by remember { mutableStateOf("http://10.0.2.2:8080/api") }
    var showBackendDialog by remember { mutableStateOf(false) }

    val totalIncome = transactions.filter { it.isIncome }.sumOf { it.amount }
    val totalExpense = transactions.filter { !it.isIncome }.sumOf { it.amount }
    val netProfit = totalIncome - totalExpense

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBackground)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Profile Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                border = BorderStroke(1.dp, SoftSlateBorder),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .size(58.dp)
                                    .clip(CircleShape)
                                    .background(SoftMintContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    when (user.role) {
                                        UserRole.FARMER -> "👨‍🌾"
                                        UserRole.BUYER -> "🛒"
                                        UserRole.ADMIN -> "🛡️"
                                    },
                                    fontSize = 30.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DarkCharcoalText
                                    )
                                )
                                Text(
                                    text = user.phone,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GreySecondaryText
                                )
                                Text(
                                    text = "📍 ${user.village}, ${user.district} • ${user.landAcres} Acres",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ForestGreenPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        FilledTonalIconButton(
                            onClick = { showEditProfileDialog = true },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = SoftEmeraldSurface,
                                contentColor = ForestGreenPrimary
                            )
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Role switch buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = user.role == UserRole.FARMER,
                            onClick = { onSelectRole(UserRole.FARMER) },
                            label = { Text("Farmer", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftMintContainer,
                                selectedLabelColor = ForestGreenPrimary,
                                containerColor = WarmCreamBackground,
                                labelColor = GreySecondaryText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = user.role == UserRole.FARMER,
                                borderColor = SoftSlateBorder,
                                selectedBorderColor = EmeraldGreenSecondary
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = user.role == UserRole.BUYER,
                            onClick = { onSelectRole(UserRole.BUYER) },
                            label = { Text("Buyer", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftMintContainer,
                                selectedLabelColor = ForestGreenPrimary,
                                containerColor = WarmCreamBackground,
                                labelColor = GreySecondaryText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = user.role == UserRole.BUYER,
                                borderColor = SoftSlateBorder,
                                selectedBorderColor = EmeraldGreenSecondary
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = user.role == UserRole.ADMIN,
                            onClick = { onSelectRole(UserRole.ADMIN) },
                            label = { Text("Admin", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftMintContainer,
                                selectedLabelColor = ForestGreenPrimary,
                                containerColor = WarmCreamBackground,
                                labelColor = GreySecondaryText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = user.role == UserRole.ADMIN,
                                borderColor = SoftSlateBorder,
                                selectedBorderColor = EmeraldGreenSecondary
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = { showAuthDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldGreenSecondary,
                            contentColor = PureWhiteCard
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(AppStrings.loginTitle.get(language), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Administrator Control Panel (Visible in ADMIN mode)
        if (user.role == UserRole.ADMIN) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                    border = BorderStroke(1.dp, SoftSlateBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🛡️", fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = AppStrings.adminDashboard.get(language),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = ForestGreenPrimary
                                    )
                                )
                            }
                            Surface(shape = RoundedCornerShape(8.dp), color = SoftGoldContainer) {
                                Text(
                                    text = "ADMIN",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnGoldContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Text("Live Agricultural Platform Metrics & Supervision", fontSize = 12.sp, color = GreySecondaryText)

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AdminMetricBox("Farmers", "${adminMetric.totalFarmers}", SoftMintContainer, ForestGreenPrimary, Modifier.weight(1f))
                            AdminMetricBox("Buyers", "${adminMetric.totalBuyers}", SoftTealTint, OnTealTint, Modifier.weight(1f))
                            AdminMetricBox("Listings", "${adminMetric.activeProduceListings}", SoftGoldContainer, OnGoldContainer, Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AdminMetricBox("Total Trade", "₹${(adminMetric.completedOrdersValue / 100000).toInt()} Lakh", SoftEmeraldSurface, ForestGreenPrimary, Modifier.weight(1f))
                            AdminMetricBox("Machinery", "${adminMetric.activeEquipmentListings}", SoftAmberContainer, OnAmberContainer, Modifier.weight(1f))
                            AdminMetricBox("Disputes", "0 Pending", SoftRedContainer, OnRedContainer, Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        // Farm Income & Expense Passbook
        item {
            Card(
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
                            Text("📒", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = AppStrings.farmAccounting.get(language),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreenPrimary
                                )
                            )
                        }

                        FilledTonalIconButton(
                            onClick = { showAddTxnDialog = true },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = SoftMintContainer,
                                contentColor = ForestGreenPrimary
                            ),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add Txn")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Net Profit Card
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (netProfit >= 0) SoftMintContainer else SoftRedContainer,
                        border = BorderStroke(
                            1.dp,
                            if (netProfit >= 0) EmeraldGreenSecondary.copy(alpha = 0.25f) else SoftRedError.copy(alpha = 0.25f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = AppStrings.netProfit.get(language),
                                style = MaterialTheme.typography.labelMedium,
                                color = if (netProfit >= 0) ForestGreenPrimary else OnRedContainer,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "₹${netProfit.toInt()}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (netProfit >= 0) ForestGreenPrimary else OnRedContainer
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Sales: +₹${totalIncome.toInt()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EmeraldGreenSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Costs: -₹${totalExpense.toInt()}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = SoftRedError,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    transactions.take(4).forEach { txn ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = txn.title,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = DarkCharcoalText
                                    )
                                )
                                Text(
                                    text = "${txn.category} • ${txn.date}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GreySecondaryText
                                )
                            }
                            Text(
                                text = (if (txn.isIncome) "+₹" else "-₹") + txn.amount.toInt().toString(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (txn.isIncome) EmeraldGreenSecondary else SoftRedError
                                )
                            )
                        }
                        HorizontalDivider(color = SoftSlateBorder)
                    }
                }
            }
        }

        // Language Selector Settings
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                border = BorderStroke(1.dp, SoftSlateBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Language, contentDescription = null, tint = ForestGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.changeLanguage.get(language),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = language == Language.ENGLISH,
                            onClick = { onSelectLanguage(Language.ENGLISH) },
                            label = { Text("English", fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftMintContainer,
                                selectedLabelColor = ForestGreenPrimary,
                                containerColor = WarmCreamBackground,
                                labelColor = GreySecondaryText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = language == Language.ENGLISH,
                                borderColor = SoftSlateBorder,
                                selectedBorderColor = EmeraldGreenSecondary
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = language == Language.KANNADA,
                            onClick = { onSelectLanguage(Language.KANNADA) },
                            label = { Text("ಕನ್ನಡ (Kannada)", fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftMintContainer,
                                selectedLabelColor = ForestGreenPrimary,
                                containerColor = WarmCreamBackground,
                                labelColor = GreySecondaryText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = language == Language.KANNADA,
                                borderColor = SoftSlateBorder,
                                selectedBorderColor = EmeraldGreenSecondary
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Backend Spring Boot REST Server Settings
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhiteCard),
                border = BorderStroke(1.dp, SoftSlateBorder)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Dns, contentDescription = null, tint = ForestGreenPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Java Spring Boot REST Backend",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ForestGreenPrimary
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Current Endpoint: $backendApiUrl\nStatus: Local Active Cache + REST Ready (Spring Boot 3 + PostgreSQL)",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreySecondaryText
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { showBackendDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, EmeraldGreenSecondary),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ForestGreenPrimary)
                    ) {
                        Text("Configure Server URL", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Kisan Helpline & Expert Support
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = ForestGreenPrimary)
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
                        Icon(Icons.Default.PhoneInTalk, contentDescription = null, tint = ForestGreenPrimary)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Kisan Call Center (Toll Free)", fontWeight = FontWeight.Bold, color = PureWhiteCard)
                        Text("1800-180-1551 (6:00 AM - 10:00 PM)", fontSize = 12.sp, color = SoftMintContainer)
                    }
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18001801551"))
                            context.startActivity(intent)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldGreenSecondary,
                            contentColor = PureWhiteCard
                        )
                    ) {
                        Text("Call", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Add Transaction Dialog
    if (showAddTxnDialog) {
        var title by remember { mutableStateOf("") }
        var amountStr by remember { mutableStateOf("") }
        var isIncome by remember { mutableStateOf(false) }
        var category by remember { mutableStateOf("Fertilizer") }

        AlertDialog(
            onDismissRequest = { showAddTxnDialog = false },
            containerColor = PureWhiteCard,
            titleContentColor = ForestGreenPrimary,
            textContentColor = DarkCharcoalText,
            shape = RoundedCornerShape(20.dp),
            title = { Text(AppStrings.addTransaction.get(language), fontWeight = FontWeight.Bold, color = ForestGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        FilterChip(
                            selected = !isIncome,
                            onClick = { isIncome = false },
                            label = { Text("Expense (ಖರ್ಚು)") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftRedContainer,
                                selectedLabelColor = OnRedContainer,
                                containerColor = WarmCreamBackground,
                                labelColor = GreySecondaryText
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = isIncome,
                            onClick = { isIncome = true },
                            label = { Text("Income (ಆದಾಯ)") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftMintContainer,
                                selectedLabelColor = ForestGreenPrimary,
                                containerColor = WarmCreamBackground,
                                labelColor = GreySecondaryText
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Description (e.g. Urea, Diesel, Seeds)") },
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
                        value = amountStr,
                        onValueChange = { amountStr = it },
                        label = { Text("Amount (₹)") },
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
                        val amt = amountStr.toDoubleOrNull() ?: 0.0
                        if (title.isNotBlank() && amt > 0) {
                            onAddTransaction(title, amt, isIncome, category)
                            showAddTxnDialog = false
                            Toast.makeText(context, "Transaction logged!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreenSecondary,
                        contentColor = PureWhiteCard
                    )
                ) {
                    Text("Save Record", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAddTxnDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = GreySecondaryText)
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Profile Dialog
    if (showEditProfileDialog) {
        var name by remember { mutableStateOf(user.name) }
        var village by remember { mutableStateOf(user.village) }
        var district by remember { mutableStateOf(user.district) }
        var acresStr by remember { mutableStateOf(user.landAcres.toString()) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            containerColor = PureWhiteCard,
            titleContentColor = ForestGreenPrimary,
            textContentColor = DarkCharcoalText,
            shape = RoundedCornerShape(20.dp),
            title = { Text(AppStrings.editProfile.get(language), fontWeight = FontWeight.Bold, color = ForestGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name") },
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
                        value = village,
                        onValueChange = { village = it },
                        label = { Text("Village / Taluk") },
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
                        value = district,
                        onValueChange = { district = it },
                        label = { Text("District") },
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
                        value = acresStr,
                        onValueChange = { acresStr = it },
                        label = { Text("Total Land Area (Acres)") },
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
                        val ac = acresStr.toDoubleOrNull() ?: user.landAcres
                        onUpdateProfile(name, village, district, ac)
                        showEditProfileDialog = false
                        Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreenSecondary,
                        contentColor = PureWhiteCard
                    )
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEditProfileDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = GreySecondaryText)
                ) { Text("Cancel") }
            }
        )
    }

    // Authentication Modal Dialog
    if (showAuthDialog) {
        var isRegisterMode by remember { mutableStateOf(false) }
        var authPhone by remember { mutableStateOf(user.phone) }
        var authName by remember { mutableStateOf(user.name) }
        var authPass by remember { mutableStateOf("••••••••") }
        var selectedRole by remember { mutableStateOf(user.role) }

        AlertDialog(
            onDismissRequest = { showAuthDialog = false },
            containerColor = PureWhiteCard,
            titleContentColor = ForestGreenPrimary,
            textContentColor = DarkCharcoalText,
            shape = RoundedCornerShape(20.dp),
            title = {
                Text(
                    text = if (isRegisterMode) AppStrings.registerTitle.get(language) else AppStrings.loginTitle.get(language),
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (isRegisterMode) {
                        OutlinedTextField(
                            value = authName,
                            onValueChange = { authName = it },
                            label = { Text("Full Name") },
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
                    OutlinedTextField(
                        value = authPhone,
                        onValueChange = { authPhone = it },
                        label = { Text(AppStrings.phoneNumber.get(language)) },
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
                        value = authPass,
                        onValueChange = { authPass = it },
                        label = { Text(AppStrings.password.get(language)) },
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreenSecondary,
                            unfocusedBorderColor = SoftSlateBorder,
                            focusedLabelColor = ForestGreenPrimary,
                            cursorColor = EmeraldGreenSecondary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Role:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = DarkCharcoalText)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = selectedRole == UserRole.FARMER,
                            onClick = { selectedRole = UserRole.FARMER },
                            label = { Text("Farmer") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftMintContainer,
                                selectedLabelColor = ForestGreenPrimary,
                                containerColor = WarmCreamBackground,
                                labelColor = GreySecondaryText
                            )
                        )
                        FilterChip(
                            selected = selectedRole == UserRole.BUYER,
                            onClick = { selectedRole = UserRole.BUYER },
                            label = { Text("Buyer") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = SoftMintContainer,
                                selectedLabelColor = ForestGreenPrimary,
                                containerColor = WarmCreamBackground,
                                labelColor = GreySecondaryText
                            )
                        )
                    }

                    TextButton(
                        onClick = { isRegisterMode = !isRegisterMode },
                        colors = ButtonDefaults.textButtonColors(contentColor = ForestGreenPrimary)
                    ) {
                        Text(
                            text = if (isRegisterMode) "Already have an account? Login" else "Don't have an account? Register",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onLogin(authName, authPhone, selectedRole)
                        showAuthDialog = false
                        Toast.makeText(context, AppStrings.authSuccess.get(language), Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreenSecondary,
                        contentColor = PureWhiteCard
                    )
                ) {
                    Text(
                        text = if (isRegisterMode) AppStrings.registerButton.get(language) else AppStrings.loginButton.get(language),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showAuthDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = GreySecondaryText)
                ) { Text("Cancel") }
            }
        )
    }

    // Configure Backend Server Dialog
    if (showBackendDialog) {
        var tempUrl by remember { mutableStateOf(backendApiUrl) }
        AlertDialog(
            onDismissRequest = { showBackendDialog = false },
            containerColor = PureWhiteCard,
            titleContentColor = ForestGreenPrimary,
            textContentColor = DarkCharcoalText,
            shape = RoundedCornerShape(20.dp),
            title = { Text("Backend Server Settings", fontWeight = FontWeight.Bold, color = ForestGreenPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Enter your deployed Java Spring Boot backend URL or use the Android emulator host 'http://10.0.2.2:8080/api':",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreySecondaryText
                    )
                    OutlinedTextField(
                        value = tempUrl,
                        onValueChange = { tempUrl = it },
                        label = { Text("REST Base URL") },
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
                        backendApiUrl = tempUrl
                        showBackendDialog = false
                        Toast.makeText(context, "Backend endpoint updated!", Toast.LENGTH_SHORT).show()
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreenSecondary,
                        contentColor = PureWhiteCard
                    )
                ) {
                    Text("Update", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showBackendDialog = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = GreySecondaryText)
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AdminMetricBox(
    title: String,
    value: String,
    bgColor: Color,
    valueColor: Color = DarkCharcoalText,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(title, fontSize = 11.sp, color = GreySecondaryText, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(2.dp))
            Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = valueColor)
        }
    }
}
