package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketplaceScreen(
    user: UserProfile,
    products: List<Product>,
    orders: List<Order>,
    language: Language,
    onAddProduct: (Product) -> Unit,
    onPlaceOrder: (Product, Double, String) -> Order,
    onMarkOrderPaid: (String, String) -> Unit,
    onCancelOrder: (String) -> Unit,
    onAddReview: (String, String, Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }
    var orderTargetProduct by remember { mutableStateOf<Product?>(null) }
    var reviewsTargetProduct by remember { mutableStateOf<Product?>(null) }
    var currentMarketTab by remember { mutableStateOf(0) } // 0: Browse, 1: Orders Tracking
    var activeUpiOrder by remember { mutableStateOf<Order?>(null) }

    Scaffold(
        containerColor = WarmCreamBackground,
        floatingActionButton = {
            if (user.role == UserRole.FARMER) {
                ExtendedFloatingActionButton(
                    onClick = { showAddDialog = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add Produce") },
                    text = { Text(AppStrings.addProduct.get(language), fontWeight = FontWeight.Bold) },
                    containerColor = EmeraldGreenSecondary,
                    contentColor = PureWhiteCard,
                    modifier = Modifier.testTag("fab_add_product")
                )
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmCreamBackground)
        ) {
            // Market Tabs
            TabRow(
                selectedTabIndex = currentMarketTab,
                containerColor = PureWhiteCard,
                contentColor = ForestGreenPrimary,
                indicator = { tabPositions ->
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[currentMarketTab]),
                        color = EmeraldGreenSecondary
                    )
                }
            ) {
                Tab(
                    selected = currentMarketTab == 0,
                    onClick = { currentMarketTab = 0 },
                    selectedContentColor = ForestGreenPrimary,
                    unselectedContentColor = GreySecondaryText,
                    text = {
                        Text(
                            if (language == Language.KANNADA) "ಲಭ್ಯವಿರುವ ಬೆಳೆಗಳು" else "Available Produce",
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
                Tab(
                    selected = currentMarketTab == 1,
                    onClick = { currentMarketTab = 1 },
                    selectedContentColor = ForestGreenPrimary,
                    unselectedContentColor = GreySecondaryText,
                    text = {
                        BadgedBox(badge = {
                            if (orders.isNotEmpty()) {
                                Badge(
                                    containerColor = GoldenYellowAccent,
                                    contentColor = DarkCharcoalText
                                ) { Text("${orders.size}", fontWeight = FontWeight.Bold) }
                            }
                        }) {
                            Text(
                                if (language == Language.KANNADA) "ಆರ್ಡರ್ ಟ್ರ್ಯಾಕಿಂಗ್" else "Orders & Tracking",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                )
            }

            if (currentMarketTab == 0) {
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .testTag("marketplace_search_input"),
                    placeholder = { Text(AppStrings.searchProduce.get(language), fontSize = 14.sp, color = GreySecondaryText) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = ForestGreenPrimary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = GreySecondaryText)
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = PureWhiteCard,
                        unfocusedContainerColor = PureWhiteCard,
                        focusedBorderColor = EmeraldGreenSecondary,
                        unfocusedBorderColor = SoftSlateBorder,
                        focusedTextColor = DarkCharcoalText,
                        unfocusedTextColor = DarkCharcoalText
                    ),
                    singleLine = true
                )

                // Category Filter Chips
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { selectedCategory = null },
                            label = { Text(AppStrings.allCrops.get(language), fontWeight = FontWeight.SemiBold) },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ForestGreenPrimary,
                                selectedLabelColor = PureWhiteCard,
                                containerColor = PureWhiteCard,
                                labelColor = DarkCharcoalText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedCategory == null,
                                borderColor = SoftSlateBorder
                            )
                        )
                    }
                    items(ProductCategory.values()) { category ->
                        val catLabel = when (category) {
                            ProductCategory.CEREALS -> AppStrings.cereals.get(language)
                            ProductCategory.PULSES -> AppStrings.pulses.get(language)
                            ProductCategory.VEGETABLES -> AppStrings.vegetables.get(language)
                            ProductCategory.FRUITS -> AppStrings.fruits.get(language)
                            ProductCategory.COMMERCIAL -> AppStrings.commercial.get(language)
                        }
                        val isSel = selectedCategory == category
                        FilterChip(
                            selected = isSel,
                            onClick = { selectedCategory = if (isSel) null else category },
                            label = { Text(catLabel, fontWeight = FontWeight.SemiBold) },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ForestGreenPrimary,
                                selectedLabelColor = PureWhiteCard,
                                containerColor = PureWhiteCard,
                                labelColor = DarkCharcoalText
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSel,
                                borderColor = SoftSlateBorder
                            )
                        )
                    }
                }

                // Filtered List
                val filteredProducts = products.filter { p ->
                    val matchQuery = searchQuery.isBlank() ||
                            p.titleEn.contains(searchQuery, ignoreCase = true) ||
                            p.titleKn.contains(searchQuery, ignoreCase = true) ||
                            p.location.contains(searchQuery, ignoreCase = true)
                    val matchCat = selectedCategory == null || p.category == selectedCategory
                    matchQuery && matchCat
                }

                if (filteredProducts.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🌾", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (language == Language.KANNADA) "ಯಾವುದೇ ಬೆಳೆಗಳು ಕಂಡುಬಂದಿಲ್ಲ" else "No matching produce found",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkCharcoalText
                                )
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 84.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredProducts, key = { it.id }) { product ->
                            ProduceItemCard(
                                product = product,
                                language = language,
                                onOrderClick = { orderTargetProduct = product },
                                onReviewsClick = { reviewsTargetProduct = product },
                                onCallFarmer = {
                                    try {
                                        val dialIntent = android.content.Intent(
                                            android.content.Intent.ACTION_DIAL,
                                            android.net.Uri.parse("tel:" + product.farmerPhone.replace(" ", ""))
                                        )
                                        context.startActivity(dialIntent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "Farmer Phone: " + product.farmerPhone, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                // Orders Tracking List
                if (orders.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("📦", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = if (language == Language.KANNADA) "ಯಾವುದೇ ಆರ್ಡರ್‌ಗಳು ದಾಖಲಾಗಿಲ್ಲ" else "No orders placed yet",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkCharcoalText
                                )
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 84.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(orders, key = { it.id }) { order ->
                            OrderItemCard(
                                order = order,
                                language = language,
                                onPayUpi = { activeUpiOrder = order },
                                onCancelOrder = { onCancelOrder(order.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddProductDialog(
            language = language,
            onDismiss = { showAddDialog = false },
            onSubmit = { newProd ->
                onAddProduct(newProd)
                showAddDialog = false
                Toast.makeText(context, "Produce listed successfully!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    orderTargetProduct?.let { product ->
        PlaceOrderDialog(
            product = product,
            language = language,
            onDismiss = { orderTargetProduct = null },
            onConfirmOrder = { qty, address, payViaUpi ->
                val order = onPlaceOrder(product, qty, address)
                orderTargetProduct = null
                currentMarketTab = 1
                if (payViaUpi) {
                    activeUpiOrder = order
                } else {
                    Toast.makeText(context, AppStrings.orderSuccess.get(language), Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    activeUpiOrder?.let { order ->
        UpiPaymentDialog(
            order = order,
            language = language,
            onDismiss = { activeUpiOrder = null },
            onPaymentComplete = { txnId ->
                onMarkOrderPaid(order.id, txnId)
                activeUpiOrder = null
                Toast.makeText(context, "Payment Confirmed! Ref: $txnId", Toast.LENGTH_LONG).show()
            }
        )
    }

    reviewsTargetProduct?.let { prod ->
        ProductReviewsDialog(
            product = prod,
            language = language,
            onDismiss = { reviewsTargetProduct = null },
            onSubmitReview = { rating, comment ->
                onAddReview(prod.id, user.name, rating, comment)
                reviewsTargetProduct = null
                Toast.makeText(context, "Review submitted! Thank you.", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun ProduceItemCard(
    product: Product,
    language: Language,
    onOrderClick: () -> Unit,
    onReviewsClick: () -> Unit,
    onCallFarmer: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(SoftMintContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(product.iconEmoji, fontSize = 28.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (language == Language.KANNADA) product.titleKn else product.titleEn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DarkCharcoalText
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "👨‍🌾 ${product.farmerName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = GreySecondaryText
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = SoftGoldContainer,
                                modifier = Modifier.clickable { onReviewsClick() }
                            ) {
                                Text(
                                    text = "⭐ ${product.rating} (${product.reviews.size})",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = OnGoldContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (product.isOrganic) SoftMintContainer else SoftGoldContainer
                ) {
                    Text(
                        text = if (product.isOrganic) "ORGANIC" else product.grade,
                        color = if (product.isOrganic) ForestGreenPrimary else OnGoldContainer,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SoftSlateBorder)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${product.price.toInt()} / ${product.unit}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                    )
                    Text(
                        text = "Stock: ${product.availableQuantity} ${product.unit}s available",
                        style = MaterialTheme.typography.bodySmall,
                        color = GreySecondaryText
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedIconButton(
                        onClick = onCallFarmer,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, SoftSlateBorder),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(Icons.Default.Phone, contentDescription = "Call", tint = ForestGreenPrimary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onOrderClick,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldGreenSecondary,
                            contentColor = PureWhiteCard
                        )
                    ) {
                        Text(AppStrings.buyNow.get(language), fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = EmeraldGreenSecondary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = product.location,
                    style = MaterialTheme.typography.labelSmall,
                    color = GreySecondaryText
                )
            }
        }
    }
}

@Composable
fun OrderItemCard(
    order: Order,
    language: Language,
    onPayUpi: () -> Unit,
    onCancelOrder: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (statusBg, statusFg) = when (order.status) {
        OrderStatus.PLACED -> SoftTealTint to OnTealTint
        OrderStatus.CONFIRMED -> SoftMintContainer to ForestGreenPrimary
        OrderStatus.IN_TRANSIT -> SoftAmberContainer to OnAmberContainer
        OrderStatus.DELIVERED -> SoftMintContainer to ForestGreenPrimary
        OrderStatus.CANCELLED -> SoftRedContainer to OnRedContainer
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
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
                Text(
                    text = order.id,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkCharcoalText
                    )
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusBg
                ) {
                    Text(
                        text = order.status.name,
                        color = statusFg,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = order.productTitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = DarkCharcoalText
                )
            )
            Text(
                text = "${order.quantity} ${order.unit} • ₹${order.totalAmount.toInt()}",
                style = MaterialTheme.typography.bodySmall,
                color = ForestGreenPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Payment: ${order.paymentStatus} ${order.upiTransactionId?.let { "($it)" } ?: ""}",
                style = MaterialTheme.typography.labelSmall,
                color = if (order.paymentStatus.contains("Paid")) EmeraldGreenSecondary else AmberWarning,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Delivery to: ${order.deliveryAddress}",
                style = MaterialTheme.typography.labelSmall,
                color = GreySecondaryText
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (order.paymentStatus == "Pending" && order.status != OrderStatus.CANCELLED) {
                    Button(
                        onClick = onPayUpi,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EmeraldGreenSecondary,
                            contentColor = PureWhiteCard
                        )
                    ) {
                        Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Pay via UPI QR", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                if (order.status != OrderStatus.CANCELLED && order.status != OrderStatus.DELIVERED) {
                    OutlinedButton(
                        onClick = onCancelOrder,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, SoftRedError),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SoftRedError)
                    ) {
                        Text(AppStrings.cancelOrder.get(language), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceOrderDialog(
    product: Product,
    language: Language,
    onDismiss: () -> Unit,
    onConfirmOrder: (Double, String, Boolean) -> Unit
) {
    var quantity by remember { mutableStateOf(1.0) }
    var deliveryAddress by remember { mutableStateOf("Farmer Market Mandi Yard, Mandya") }
    var payViaUpi by remember { mutableStateOf(true) }
    val totalCost = quantity * product.price

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhiteCard,
        titleContentColor = ForestGreenPrimary,
        textContentColor = DarkCharcoalText,
        title = { Text(AppStrings.buyNow.get(language), fontWeight = FontWeight.Bold, color = ForestGreenPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = if (language == Language.KANNADA) product.titleKn else product.titleEn,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = DarkCharcoalText)
                )
                Text(
                    text = "Price: ₹${product.price.toInt()} per ${product.unit}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GreySecondaryText
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Quantity (${product.unit}):", fontWeight = FontWeight.SemiBold, color = DarkCharcoalText)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FilledTonalIconButton(
                            onClick = { if (quantity > 1) quantity -= 1 },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = SoftMintContainer,
                                contentColor = ForestGreenPrimary
                            ),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "${quantity.toInt()} ${product.unit}s",
                            modifier = Modifier.padding(horizontal = 12.dp),
                            fontWeight = FontWeight.Bold,
                            color = DarkCharcoalText
                        )
                        FilledTonalIconButton(
                            onClick = { if (quantity < product.availableQuantity) quantity += 1 },
                            colors = IconButtonDefaults.filledTonalIconButtonColors(
                                containerColor = SoftMintContainer,
                                contentColor = ForestGreenPrimary
                            ),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Text("+", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                OutlinedTextField(
                    value = deliveryAddress,
                    onValueChange = { deliveryAddress = it },
                    label = { Text("Delivery Address / Mandi Yard") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldGreenSecondary,
                        unfocusedBorderColor = SoftSlateBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = payViaUpi,
                        onCheckedChange = { payViaUpi = it },
                        colors = CheckboxDefaults.colors(checkedColor = EmeraldGreenSecondary)
                    )
                    Text("Pay instantly with UPI QR Code", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = DarkCharcoalText)
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoftMintContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Total Amount:", fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                        Text(
                            text = "₹${totalCost.toInt()}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreenPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirmOrder(quantity, deliveryAddress, payViaUpi) },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSecondary, contentColor = PureWhiteCard)
            ) {
                Text(AppStrings.buyNow.get(language), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = GreySecondaryText)
            }
        }
    )
}

@Composable
fun UpiPaymentDialog(
    order: Order,
    language: Language,
    onDismiss: () -> Unit,
    onPaymentComplete: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhiteCard,
        titleContentColor = ForestGreenPrimary,
        textContentColor = DarkCharcoalText,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null, tint = ForestGreenPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(AppStrings.payViaUpi.get(language), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = ForestGreenPrimary)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = AppStrings.scanUpiQr.get(language),
                    style = MaterialTheme.typography.bodySmall,
                    color = GreySecondaryText
                )

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = PureWhiteCard,
                    border = BorderStroke(1.5.dp, SoftSlateBorder),
                    modifier = Modifier.size(174.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = "QR Code",
                            modifier = Modifier.size(120.dp),
                            tint = DarkCharcoalText
                        )
                        Text("UPI: farmerconnect@sbi", fontSize = 10.sp, color = GreySecondaryText, fontWeight = FontWeight.SemiBold)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoftMintContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Payable Amount", fontSize = 12.sp, color = ForestGreenPrimary)
                        Text("₹${order.totalAmount.toInt()}", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = ForestGreenPrimary)
                        Text("To: ${order.productTitle}", fontSize = 11.sp, color = DarkCharcoalText)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val fakeUpiId = "UPI/${System.currentTimeMillis() % 1000000}"
                    onPaymentComplete(fakeUpiId)
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSecondary, contentColor = PureWhiteCard)
            ) {
                Text(AppStrings.markAsPaid.get(language), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = GreySecondaryText)
            }
        }
    )
}

@Composable
fun ProductReviewsDialog(
    product: Product,
    language: Language,
    onDismiss: () -> Unit,
    onSubmitReview: (Int, String) -> Unit
) {
    var rating by remember { mutableStateOf(5) }
    var reviewComment by remember { mutableStateOf("") }
    var showAddReviewForm by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhiteCard,
        titleContentColor = ForestGreenPrimary,
        textContentColor = DarkCharcoalText,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⭐", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${AppStrings.viewReviews.get(language)} (${product.rating})",
                    fontWeight = FontWeight.Bold,
                    color = ForestGreenPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (!showAddReviewForm) {
                    if (product.reviews.isEmpty()) {
                        Text("No reviews yet. Be the first buyer to review this produce!", style = MaterialTheme.typography.bodySmall, color = GreySecondaryText)
                    } else {
                        product.reviews.forEach { r ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = WarmCreamBackground),
                                border = BorderStroke(1.dp, SoftSlateBorder),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(r.reviewerName, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DarkCharcoalText)
                                        Surface(shape = RoundedCornerShape(6.dp), color = SoftGoldContainer) {
                                            Text(
                                                "⭐ ${r.rating}/5",
                                                color = OnGoldContainer,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                    Text(r.comment, fontSize = 12.sp, color = DarkCharcoalText, modifier = Modifier.padding(top = 4.dp))
                                    Text(r.date, fontSize = 10.sp, color = GreySecondaryText)
                                }
                            }
                        }
                    }
                    Button(
                        onClick = { showAddReviewForm = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSecondary, contentColor = PureWhiteCard)
                    ) {
                        Text(AppStrings.writeReview.get(language), fontWeight = FontWeight.Bold)
                    }
                } else {
                    Text("Select Rating (1 to 5 Stars):", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = DarkCharcoalText)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        (1..5).forEach { star ->
                            IconButton(onClick = { rating = star }, modifier = Modifier.size(36.dp)) {
                                Text(if (star <= rating) "⭐" else "☆", fontSize = 20.sp)
                            }
                        }
                    }
                    OutlinedTextField(
                        value = reviewComment,
                        onValueChange = { reviewComment = it },
                        label = { Text("Your Review / Quality Feedback") },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldGreenSecondary,
                            unfocusedBorderColor = SoftSlateBorder
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (reviewComment.isNotBlank()) {
                                onSubmitReview(rating, reviewComment.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSecondary, contentColor = PureWhiteCard),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Submit Review", fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = ForestGreenPrimary, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
fun AddProductDialog(
    language: Language,
    onDismiss: () -> Unit,
    onSubmit: (Product) -> Unit
) {
    var titleEn by remember { mutableStateOf("") }
    var titleKn by remember { mutableStateOf("") }
    var priceStr by remember { mutableStateOf("") }
    var qtyStr by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("Mandya, Karnataka") }
    var category by remember { mutableStateOf(ProductCategory.CEREALS) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = PureWhiteCard,
        titleContentColor = ForestGreenPrimary,
        textContentColor = DarkCharcoalText,
        title = { Text(AppStrings.addProduct.get(language), fontWeight = FontWeight.Bold, color = ForestGreenPrimary) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text("Produce Name (English)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreenSecondary, unfocusedBorderColor = SoftSlateBorder),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = titleKn,
                    onValueChange = { titleKn = it },
                    label = { Text("ಬೆಳೆಯ ಹೆಸರು (ಕನ್ನಡ)") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreenSecondary, unfocusedBorderColor = SoftSlateBorder),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Price (₹/Qtl)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreenSecondary, unfocusedBorderColor = SoftSlateBorder),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = qtyStr,
                        onValueChange = { qtyStr = it },
                        label = { Text("Qty (Qtls)") },
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreenSecondary, unfocusedBorderColor = SoftSlateBorder),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Village / District") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldGreenSecondary, unfocusedBorderColor = SoftSlateBorder),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titleEn.isNotBlank() && priceStr.isNotBlank() && qtyStr.isNotBlank()) {
                        val p = Product(
                            titleEn = titleEn,
                            titleKn = titleKn.ifBlank { titleEn },
                            category = category,
                            price = priceStr.toDoubleOrNull() ?: 2000.0,
                            availableQuantity = qtyStr.toDoubleOrNull() ?: 10.0,
                            location = location,
                            iconEmoji = "🌾"
                        )
                        onSubmit(p)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreenSecondary, contentColor = PureWhiteCard)
            ) {
                Text(AppStrings.submitListing.get(language), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = GreySecondaryText)
            }
        }
    )
}
