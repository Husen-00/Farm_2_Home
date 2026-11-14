package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class FarmerRepository {

    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    private val _currentUser = MutableStateFlow(
        UserProfile(
            id = "farmer_01",
            name = "Basavaraj Patil",
            phone = "+91 98451 23456",
            village = "Srirangapatna",
            district = "Mandya",
            state = "Karnataka",
            role = UserRole.FARMER,
            landAcres = 4.5,
            cropsGrown = listOf("Sugarcane", "Paddy", "Ragi"),
            isAuthenticated = true
        )
    )
    val currentUser: StateFlow<UserProfile> = _currentUser.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _orders = MutableStateFlow<List<Order>>(emptyList())
    val orders: StateFlow<List<Order>> = _orders.asStateFlow()

    private val _mandiPrices = MutableStateFlow<List<MandiPrice>>(emptyList())
    val mandiPrices: StateFlow<List<MandiPrice>> = _mandiPrices.asStateFlow()

    private val _weather = MutableStateFlow(WeatherAdvisory())
    val weather: StateFlow<WeatherAdvisory> = _weather.asStateFlow()

    private val _equipmentList = MutableStateFlow<List<Equipment>>(emptyList())
    val equipmentList: StateFlow<List<Equipment>> = _equipmentList.asStateFlow()

    private val _equipmentBookings = MutableStateFlow<List<EquipmentBooking>>(emptyList())
    val equipmentBookings: StateFlow<List<EquipmentBooking>> = _equipmentBookings.asStateFlow()

    private val _laborListings = MutableStateFlow<List<LaborListing>>(emptyList())
    val laborListings: StateFlow<List<LaborListing>> = _laborListings.asStateFlow()

    private val _laborBookings = MutableStateFlow<List<LaborBooking>>(emptyList())
    val laborBookings: StateFlow<List<LaborBooking>> = _laborBookings.asStateFlow()

    private val _transportRequests = MutableStateFlow<List<TransportRequest>>(emptyList())
    val transportRequests: StateFlow<List<TransportRequest>> = _transportRequests.asStateFlow()

    private val _schemes = MutableStateFlow<List<GovernmentScheme>>(emptyList())
    val schemes: StateFlow<List<GovernmentScheme>> = _schemes.asStateFlow()

    private val _transactions = MutableStateFlow<List<FarmTransaction>>(emptyList())
    val transactions: StateFlow<List<FarmTransaction>> = _transactions.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _notifications = MutableStateFlow<List<FarmNotification>>(emptyList())
    val notifications: StateFlow<List<FarmNotification>> = _notifications.asStateFlow()

    private val _adminMetrics = MutableStateFlow(AdminMetric())
    val adminMetrics: StateFlow<AdminMetric> = _adminMetrics.asStateFlow()

    init {
        initializeSampleData()
    }

    suspend fun refreshWeather() {
        val live = RealWeatherService.fetchLiveWeather()
        _weather.value = live
    }

    private fun initializeSampleData() {
        val todayStr = dateFormat.format(Date())

        // Initial Marketplace Produce
        _products.value = listOf(
            Product(
                id = "PROD-01",
                farmerName = "Basavaraj Patil",
                farmerPhone = "+91 98451 23456",
                titleEn = "Organic Sona Masoori Paddy",
                titleKn = "ಸಾವಯವ ಸೋನಾ ಮಸೂರಿ ಭತ್ತ",
                category = ProductCategory.CEREALS,
                price = 2450.0,
                unit = "Quintal",
                availableQuantity = 45.0,
                grade = "Grade A",
                location = "Mandya, Karnataka",
                rating = 4.9,
                isOrganic = true,
                iconEmoji = "🌾"
            ),
            Product(
                id = "PROD-02",
                farmerName = "Mallikarjun Swamy",
                farmerPhone = "+91 94482 67890",
                titleEn = "Finger Millet / Ragi (GPU-28)",
                titleKn = "ಶುದ್ಧ ನಾಟಿ ರಾಗಿ (GPU-28)",
                category = ProductCategory.CEREALS,
                price = 3850.0,
                unit = "Quintal",
                availableQuantity = 30.0,
                grade = "Grade A",
                location = "Hassan, Karnataka",
                rating = 4.8,
                isOrganic = true,
                iconEmoji = "🌾"
            ),
            Product(
                id = "PROD-03",
                farmerName = "Shankar Gouda",
                farmerPhone = "+91 99014 55432",
                titleEn = "Belagavi Red Round Onion",
                titleKn = "ಬೆಳಗಾವಿ ಕೆಂಪು ಈರುಳ್ಳಿ",
                category = ProductCategory.VEGETABLES,
                price = 2200.0,
                unit = "Quintal",
                availableQuantity = 80.0,
                grade = "Grade A",
                location = "Belagavi, Karnataka",
                rating = 4.7,
                isOrganic = false,
                iconEmoji = "🧅"
            ),
            Product(
                id = "PROD-04",
                farmerName = "Ramesh Kumar",
                farmerPhone = "+91 97412 88990",
                titleEn = "Farm Fresh Hybrid Tomatoes",
                titleKn = "ತಾಜಾ ನಾಟಿ ಹೈಬ್ರಿಡ್ ಟೊಮ್ಯಾಟೊ",
                category = ProductCategory.VEGETABLES,
                price = 1450.0,
                unit = "Quintal",
                availableQuantity = 50.0,
                grade = "Grade A",
                location = "Kolar, Karnataka",
                rating = 4.6,
                isOrganic = false,
                iconEmoji = "🍅"
            ),
            Product(
                id = "PROD-05",
                farmerName = "Chandrashekar",
                farmerPhone = "+91 98801 12345",
                titleEn = "CO-86032 High-Sugar Sugarcane",
                titleKn = "CO-86032 ಸಿಹಿ ಕಬ್ಬು",
                category = ProductCategory.COMMERCIAL,
                price = 3300.0,
                unit = "Ton",
                availableQuantity = 120.0,
                grade = "Grade A",
                location = "Dharwad, Karnataka",
                rating = 4.9,
                isOrganic = true,
                iconEmoji = "🎋"
            ),
            Product(
                id = "PROD-06",
                farmerName = "Anand Rao",
                farmerPhone = "+91 96113 45678",
                titleEn = "Yellow Pigeon Pea (Toor Dal)",
                titleKn = "ಕಲಬುರಗಿ ತೊಗರಿ ಕಾಳು (ತೊಗರಿ ಬೇಳೆ)",
                category = ProductCategory.PULSES,
                price = 9200.0,
                unit = "Quintal",
                availableQuantity = 25.0,
                grade = "Grade A (GI Tagged)",
                location = "Kalaburagi, Karnataka",
                rating = 5.0,
                isOrganic = true,
                iconEmoji = "🫘"
            )
        )

        // Karnataka APMC Live Mandi Bhav Data with 7-day Historical Price Points
        _mandiPrices.value = listOf(
            MandiPrice("MP-01", "Paddy (Sona Masoori)", "ಭತ್ತ (ಸೋನಾ ಮಸೂರಿ)", "Mandya APMC", "Mandya", 2300.0, 2650.0, 2480.0, todayStr, PriceTrend.UP, 3.4),
            MandiPrice("MP-02", "Ragi (Finger Millet)", "ರಾಗಿ (ನಾಟಿ)", "Hassan APMC", "Hassan", 3600.0, 4100.0, 3900.0, todayStr, PriceTrend.STABLE, 0.0),
            MandiPrice("MP-03", "Onion (Medium Red)", "ಈರುಳ್ಳಿ (ಕೆಂಪು)", "Yeshwanthpur APMC", "Bengaluru", 1900.0, 2400.0, 2250.0, todayStr, PriceTrend.DOWN, -2.1),
            MandiPrice("MP-04", "Tomato (Hybrid)", "ಟೊಮ್ಯಾಟೊ", "Kolar APMC", "Kolar", 1200.0, 1650.0, 1450.0, todayStr, PriceTrend.UP, 5.2),
            MandiPrice("MP-05", "Toor Dal (Red Gram)", "ತೊಗರಿ (ಕೆಂಪು ಕಾಳು)", "Kalaburagi APMC", "Kalaburagi", 8800.0, 9600.0, 9250.0, todayStr, PriceTrend.UP, 1.8),
            MandiPrice("MP-06", "Maize (Hybrid)", "ಮೆಕ್ಕೆಜೋಳ", "Davanagere APMC", "Davanagere", 2100.0, 2350.0, 2250.0, todayStr, PriceTrend.STABLE, 0.5),
            MandiPrice("MP-07", "Cotton (Medium Staple)", "ಹತ್ತಿ", "Haveri APMC", "Haveri", 6900.0, 7500.0, 7200.0, todayStr, PriceTrend.UP, 2.3)
        )

        // Sample Orders with UPI payment tracking
        _orders.value = listOf(
            Order(
                id = "ORD-94281",
                productId = "PROD-01",
                productTitle = "Organic Sona Masoori Paddy",
                buyerName = "Sri Lakshmi Rice Mill",
                buyerPhone = "+91 94800 11223",
                quantity = 20.0,
                unit = "Quintal",
                totalAmount = 49000.0,
                status = OrderStatus.CONFIRMED,
                orderDate = todayStr,
                deliveryAddress = "Industrial Area, Mysore Road, Mandya",
                paymentStatus = "Paid via UPI",
                upiTransactionId = "UPI/261017/998124"
            )
        )

        // Equipment listings
        _equipmentList.value = listOf(
            Equipment("EQ-01", "Mahindra 575 DI Tractor (45 HP)", "ಮಹೀಂದ್ರಾ 575 ಡಿಐ ಟ್ರ್ಯಾಕ್ಟರ್", "Rudrappa", "+91 94481 00112", "45 HP with Rotavator & Plough", 450.0, 3200.0, "Mandya (3 km away)", 4.9, true, "🚜"),
            Equipment("EQ-02", "Paddy Combine Harvester", "ಭತ್ತ ಕೊಯ್ಲು ಮಾಡುವ ಯಂತ್ರ (ಹಾರ್ವೆಸ್ಟರ್)", "Veeranna Gowda", "+91 98450 33445", "Rubber Track Harvester", 1600.0, 12000.0, "Maddur (6 km away)", 4.8, true, "🌾"),
            Equipment("EQ-03", "Power Tiller & Rotary Weeder", "ಪವರ್ ಟಿಲ್ಲರ್ ಮತ್ತು ಕಳೆ ಕತ್ತರಿಸುವ ಯಂತ್ರ", "Nagappa", "+91 97311 55667", "12 HP Diesel", 280.0, 1800.0, "Srirangapatna (2 km away)", 4.7, true, "⚙️"),
            Equipment("EQ-04", "Tractor-Mounted Boom Sprayer", "ಟ್ರ್ಯಾಕ್ಟರ್ ಮೌಂಟೆಡ್ ಬೂಮ್ ಸ್ಪ್ರೇಯರ್", "Somasekhar", "+91 99002 44331", "500L Tank, 40ft Boom", 350.0, 2400.0, "Pandavapura (8 km away)", 4.8, true, "🚜")
        )

        // Agricultural Labour Teams
        _laborListings.value = listOf(
            LaborListing(
                teamLeadName = "Chikkanna Gowda Team",
                phone = "+91 98440 12890",
                village = "Srirangapatna",
                skillsEn = "Paddy Transplanting, Weeding & Harvesting",
                skillsKn = "ಭತ್ತ ನಾಟಿ, ಕಳೆ ಕೀಳುವುದು ಮತ್ತು ಕೊಯ್ಲು",
                teamSize = 8,
                dailyWagePerWorker = 450.0
            ),
            LaborListing(
                teamLeadName = "Maruthi Sugarcane Cutters",
                phone = "+91 99011 44556",
                village = "Maddur",
                skillsEn = "Sugarcane Harvesting, Binding & Truck Loading",
                skillsKn = "ಕಬ್ಬು ಕಟಾವು, ಕಟ್ಟುವುದು ಮತ್ತು ಲಾರಿ ಲೋಡಿಂಗ್",
                teamSize = 12,
                dailyWagePerWorker = 500.0
            )
        )

        // Government Schemes
        _schemes.value = listOf(
            GovernmentScheme(
                id = "SCH-01",
                titleEn = "PM-KISAN Samman Nidhi",
                titleKn = "ಪಿಎಂ-ಕಿಸಾನ್ ಸಮ್ಮಾನ್ ನಿಧಿ ಯೋಜನೆ",
                authority = "Ministry of Agriculture & Farmers Welfare, GoI",
                benefitEn = "₹6,000 per year in 3 direct bank transfer installments of ₹2,000 each.",
                benefitKn = "ವರ್ಷಕ್ಕೆ ₹6,000 ಪ್ರೋತ್ಸಾಹ ಧನ (ಮೂರು ಕಂತುಗಳಲ್ಲಿ ತಲಾ ₹2,000 ನೇರ ಬ್ಯಾಂಕ್ ಜಮೆ).",
                eligibilityEn = "All small and marginal landholding farmer families having cultivable land in their names.",
                eligibilityKn = "ತಮ್ಮ ಹೆಸರಿನಲ್ಲಿ ಸಾಗುವಳಿ ಜಮೀನು ಹೊಂದಿರುವ ಸಣ್ಣ ಮತ್ತು ಅತಿ ಸಣ್ಣ ರೈತ ಕುಟುಂಬಗಳು.",
                documentsEn = "Aadhaar Card, Land Record (Pahani/RTC), Bank Passbook seeded with Aadhaar/NPCI.",
                documentsKn = "ಆಧಾರ್ ಕಾರ್ಡ್, ಪಹಣಿ/ಆರ್.ಟಿ.ಸಿ ದಾಖಲೆ, ಆಧಾರ್ ಲಿಂಕ್ ಆದ ಬ್ಯಾಂಕ್ ಪಾಸ್‌ಬುಕ್.",
                officialUrl = "https://pmkisan.gov.in"
            ),
            GovernmentScheme(
                id = "SCH-02",
                titleEn = "Karnataka Raitha Siri Scheme",
                titleKn = "ಕರ್ನಾಟಕ ರೈತ ಸಿರಿ ಯೋಜನೆ (ಸಿರಿಧಾನ್ಯ ಪ್ರೋತ್ಸಾಹ)",
                authority = "Department of Agriculture, Govt. of Karnataka",
                benefitEn = "Incentive of ₹10,000 per hectare for growing minor millets (Ragi, Navane, Same, Korale).",
                benefitKn = "ಸಿರಿಧಾನ್ಯ ಬೆಳೆಯುವ ರೈತರಿಗೆ ಪ್ರತಿ ಹೆಕ್ಟೇರ್‌ಗೆ ₹10,000 ನೇರ ಪ್ರೋತ್ಸಾಹಧನ.",
                eligibilityEn = "Karnataka farmers cultivating certified millet varieties on rainfed farmland.",
                eligibilityKn = "ಖುಷ್ಕಿ ಜಮೀನಿನಲ್ಲಿ ಸಿರಿಧಾನ್ಯ ಬೆಳೆದಿರುವ ಕರ್ನಾಟಕದ ಎಲ್ಲಾ ನೋಂದಾಯಿತ ರೈತರು.",
                documentsEn = "FID (Farmer ID from FRUITS portal), RTC Pahani showing millet crop, Aadhaar.",
                documentsKn = "ಫ್ರೂಟ್ಸ್ (FRUITS) ಪೋರ್ಟಲ್ FID ಸಂಖ್ಯೆ, ಪಹಣಿ, ಆಧಾರ್ ಕಾರ್ಡ್.",
                officialUrl = "https://fruits.karnataka.gov.in"
            ),
            GovernmentScheme(
                id = "SCH-03",
                titleEn = "Pradhan Mantri Fasal Bima Yojana (PMFBY)",
                titleKn = "ಪ್ರಧಾನ ಮಂತ್ರಿ ಫಸಲ್ ಬಿಮಾ ಯೋಜನೆ (ಬೆಳೆ ವಿಮೆ)",
                authority = "National Crop Insurance Portal",
                benefitEn = "Comprehensive crop insurance coverage against drought, flood, pests, and unseasonal rainfall.",
                benefitKn = "ಬರಗಾಲ, ಅಕಾಲಿಕ ಮಳೆ ಮತ್ತು ಕೀಟ ಬಾಧೆಯಿಂದ ಉಂಟಾಗುವ ಬೆಳೆ ನಷ್ಟಕ್ಕೆ ಸಂಪೂರ್ಣ ವಿಮಾ ಪರಿಹಾರ.",
                eligibilityEn = "All farmers cultivating notified crops in notified areas during Kharif & Rabi seasons.",
                eligibilityKn = "ಅಧಿಸೂಚಿತ ಬೆಳೆ ಬೆಳೆದ ಎಲ್ಲಾ ರೈತರು (ಖಾರಿಫ್ ಮತ್ತು ರಬಿ ಹಂಗಾಮು).",
                documentsEn = "RTC Pahani, Sowing Certificate from Gram Panchayat/Agriculture Officer, Aadhaar.",
                documentsKn = "ಆರ್‌ಟಿಸಿ ಪಹಣಿ, ಗ್ರಾಮ ಪಂಚಾಯತ್ ಬಿತ್ತನೆ ದೃಢೀಕರಣ ಪತ್ರ, ಬ್ಯಾಂಕ್ ಖಾತೆ ವಿವರ.",
                officialUrl = "https://pmfby.gov.in"
            ),
            GovernmentScheme(
                id = "SCH-04",
                titleEn = "PM-KUSUM Solar Irrigation Pump",
                titleKn = "ಪಿಎಂ-ಕುಸುಮ್ ಸೌರ ಪಂಪ್‌ಸೆಟ್ ಯೋಜನೆ",
                authority = "MNRE & KREDL (Karnataka Renewable Energy)",
                benefitEn = "Up to 60% subsidy for installing 3HP, 5HP, or 7.5HP off-grid solar water pumps.",
                benefitKn = "ಸೌರ ಕೃಷಿ ಪಂಪ್‌ಸೆಟ್‌ಗಳಿಗೆ 60% ರಿಯಾಯಿತಿ ಮತ್ತು ದಿನದ ವೇಳೆಯಲ್ಲಿ ಉಚಿತ ವಿದ್ಯುತ್.",
                eligibilityEn = "Individual farmers, water user associations with water source on land.",
                eligibilityKn = "ಕೃಷಿ ಜಮೀನಿನಲ್ಲಿ ನೀರಾವರಿ ಮೂಲ ಹೊಂದಿರುವ ರೈತರು.",
                documentsEn = "RTC, Water source certificate from Tahsildar, Bank Passbook, Aadhaar.",
                documentsKn = "ಆರ್‌ಟಿಸಿ, ನೀರಾವರಿ ದೃಢೀಕರಣ, ಆಧಾರ್ ಕಾರ್ಡ್.",
                officialUrl = "https://pmkusum.mnre.gov.in"
            )
        )

        // Farm transactions
        _transactions.value = listOf(
            FarmTransaction(title = "Paddy Harvest Sale (20 Quintals)", amount = 49000.0, isIncome = true, category = "Produce Sale", date = todayStr),
            FarmTransaction(title = "IFFCO 19:19:19 & Urea Fertilizer", amount = 4200.0, isIncome = false, category = "Fertilizer", date = todayStr),
            FarmTransaction(title = "Tractor Ploughing Rental", amount = 3200.0, isIncome = false, category = "Machinery", date = "Yesterday"),
            FarmTransaction(title = "Weeding & Transplanting Labor (4 workers)", amount = 2400.0, isIncome = false, category = "Farm Labor", date = "12 Oct 2026")
        )

        // In-App Notifications
        _notifications.value = listOf(
            FarmNotification(
                titleEn = "Rainfall Advisory for Mandya",
                titleKn = "ಮಂಡ್ಯ ಜಿಲ್ಲೆಗೆ ಮಳೆಯ ಮುನ್ಸೂಚನೆ",
                descriptionEn = "15mm rainfall forecast in the evening. Hold fertilizer application.",
                descriptionKn = "ಸಂಜೆ ವೇಳೆಗೆ ಸಾಧಾರಣ ಮಳೆಯ ಮುನ್ಸೂಚನೆ. ಗೊಬ್ಬರ ಸಿಂಪಡಿಸುವುದನ್ನು ಮುಂದೂಡಿ.",
                timestamp = "10 mins ago",
                iconEmoji = "🌧️",
                category = "WEATHER"
            ),
            FarmNotification(
                titleEn = "Mandi Price Spike: Tomato (+5.2%)",
                titleKn = "ಟೊಮ್ಯಾಟೊ ಮಂಡಿ ದರ ಏರಿಕೆ (+5.2%)",
                descriptionEn = "Kolar APMC modal price jumped to ₹1,450/Qtl today.",
                descriptionKn = "ಕೋಲಾರ ಮಂಡಿಯಲ್ಲಿ ಟೊಮ್ಯಾಟೊ ಬೆಲೆ ₹1,450 ಕ್ಕೆ ಏರಿಕೆಯಾಗಿದೆ.",
                timestamp = "2 hours ago",
                iconEmoji = "📈",
                category = "PRICE"
            ),
            FarmNotification(
                titleEn = "Order Confirmed by Sri Lakshmi Mill",
                titleKn = "ಆರ್ಡರ್ ದೃಢೀಕರಿಸಲಾಗಿದೆ",
                descriptionEn = "20 Quintals of Sona Masoori Paddy accepted. Payment received via UPI.",
                descriptionKn = "20 ಕ್ವಿಂಟಾಲ್ ಭತ್ತದ ಆರ್ಡರ್ ದೃಢಪಟ್ಟಿದೆ. ಯುಪಿಐ ಪಾವತಿ ಯಶಸ್ವಿಯಾಗಿದೆ.",
                timestamp = "Yesterday",
                iconEmoji = "✅",
                category = "ORDER"
            )
        )

        // Initial Krishi AI message
        _chatMessages.value = listOf(
            ChatMessage(
                text = "Namaskara! I am Krishi AI Assistant. How can I help your farm today? You can ask in Kannada or English, tap the mic to speak, or tap 'Crop Doctor' to check affected leaves.",
                isUser = false
            )
        )
    }

    fun loginUser(name: String, phone: String, role: UserRole) {
        _currentUser.value = _currentUser.value.copy(
            name = name,
            phone = phone,
            role = role,
            isAuthenticated = true
        )
    }

    fun updateProfile(name: String, village: String, district: String, acres: Double) {
        _currentUser.value = _currentUser.value.copy(
            name = name,
            village = village,
            district = district,
            landAcres = acres
        )
    }

    fun toggleRole() {
        val current = _currentUser.value
        val newRole = when (current.role) {
            UserRole.FARMER -> UserRole.BUYER
            UserRole.BUYER -> UserRole.ADMIN
            UserRole.ADMIN -> UserRole.FARMER
        }
        _currentUser.value = current.copy(role = newRole)
    }

    fun setRole(role: UserRole) {
        _currentUser.value = _currentUser.value.copy(role = role)
    }

    fun addProduct(product: Product) {
        val list = _products.value.toMutableList()
        list.add(0, product)
        _products.value = list
    }

    fun addProductReview(productId: String, reviewerName: String, rating: Int, comment: String) {
        val dateStr = dateFormat.format(Date())
        val review = Review(reviewerName = reviewerName, rating = rating, comment = comment, date = dateStr)
        val updated = _products.value.map { prod ->
            if (prod.id == productId) {
                val newReviews = prod.reviews + review
                val newAvg = newReviews.map { it.rating }.average()
                prod.copy(reviews = newReviews, rating = String.format(Locale.US, "%.1f", newAvg).toDouble())
            } else prod
        }
        _products.value = updated
    }

    fun placeOrder(product: Product, quantity: Double, address: String): Order {
        val todayStr = dateFormat.format(Date())
        val newOrder = Order(
            productId = product.id,
            productTitle = product.titleEn,
            buyerName = _currentUser.value.name,
            buyerPhone = _currentUser.value.phone,
            quantity = quantity,
            unit = product.unit,
            totalAmount = product.price * quantity,
            status = OrderStatus.PLACED,
            orderDate = todayStr,
            deliveryAddress = address,
            paymentStatus = "Pending"
        )
        val list = _orders.value.toMutableList()
        list.add(0, newOrder)
        _orders.value = list

        val prodList = _products.value.map {
            if (it.id == product.id) {
                it.copy(availableQuantity = (it.availableQuantity - quantity).coerceAtLeast(0.0))
            } else it
        }
        _products.value = prodList

        // Notification
        addNotification(
            FarmNotification(
                titleEn = "New Order Placed: ${product.titleEn}",
                titleKn = "ಹೊಸ ಆರ್ಡರ್ ದಾಖಲಾಗಿದೆ: ${product.titleKn}",
                descriptionEn = "$quantity ${product.unit}s ordered for ₹${newOrder.totalAmount.toInt()}.",
                descriptionKn = "$quantity ${product.unit} ಆರ್ಡರ್ ಮಾಡಲಾಗಿದೆ (ಒಟ್ಟು ₹${newOrder.totalAmount.toInt()}).",
                timestamp = "Just now",
                iconEmoji = "📦",
                category = "ORDER"
            )
        )
        return newOrder
    }

    fun markOrderAsPaid(orderId: String, upiTxnId: String) {
        val list = _orders.value.map {
            if (it.id == orderId) {
                it.copy(
                    paymentStatus = "Paid via UPI",
                    upiTransactionId = upiTxnId,
                    status = OrderStatus.CONFIRMED
                )
            } else it
        }
        _orders.value = list
    }

    fun cancelOrder(orderId: String) {
        val list = _orders.value.map {
            if (it.id == orderId) it.copy(status = OrderStatus.CANCELLED) else it
        }
        _orders.value = list
    }

    fun bookEquipment(equipment: Equipment, hours: Int, date: String) {
        val booking = EquipmentBooking(
            equipmentName = equipment.nameEn,
            farmerName = _currentUser.value.name,
            date = date,
            hours = hours,
            totalCost = equipment.hourlyRate * hours,
            status = "Confirmed"
        )
        val list = _equipmentBookings.value.toMutableList()
        list.add(0, booking)
        _equipmentBookings.value = list
    }

    fun bookLabor(listing: LaborListing, activity: String, workersCount: Int, date: String) {
        val booking = LaborBooking(
            teamLeadName = listing.teamLeadName,
            activity = activity,
            workersCount = workersCount,
            date = date,
            totalWage = workersCount * listing.dailyWagePerWorker,
            status = "Confirmed"
        )
        val list = _laborBookings.value.toMutableList()
        list.add(0, booking)
        _laborBookings.value = list
    }

    fun requestTransport(pickup: String, destination: String, crop: String, weight: Double, vehicle: String) {
        val req = TransportRequest(
            pickupVillage = pickup,
            destinationMandi = destination,
            cropName = crop,
            weightQuintals = weight,
            vehicleType = vehicle,
            estimatedFare = weight * 45.0 + 800.0,
            status = "Driver Assigned"
        )
        val list = _transportRequests.value.toMutableList()
        list.add(0, req)
        _transportRequests.value = list
    }

    fun addTransaction(title: String, amount: Double, isIncome: Boolean, category: String) {
        val todayStr = dateFormat.format(Date())
        val txn = FarmTransaction(
            title = title,
            amount = amount,
            isIncome = isIncome,
            category = category,
            date = todayStr
        )
        val list = _transactions.value.toMutableList()
        list.add(0, txn)
        _transactions.value = list
    }

    fun addChatMessage(msg: ChatMessage) {
        val list = _chatMessages.value.toMutableList()
        list.add(msg)
        _chatMessages.value = list
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                text = "Chat history cleared. How can Krishi AI assist you now?",
                isUser = false
            )
        )
    }

    fun addNotification(notification: FarmNotification) {
        val list = _notifications.value.toMutableList()
        list.add(0, notification)
        _notifications.value = list
    }

    fun markNotificationsAsRead() {
        val list = _notifications.value.map { it.copy(isRead = true) }
        _notifications.value = list
    }

    fun calculateFertilizer(crop: String, acres: Double): FertilizerRecommendation {
        return when (crop.lowercase()) {
            "paddy", "rice", "ಭತ್ತ" -> FertilizerRecommendation(
                crop = "Paddy / Rice (ಭತ್ತ)",
                acreage = acres,
                ureaBags = (acres * 2.2).toInt().coerceAtLeast(1),
                dapBags = (acres * 1.5).toInt().coerceAtLeast(1),
                mopBags = (acres * 1.0).toInt().coerceAtLeast(1),
                estimatedCost = acres * 2650.0,
                organicCompostTons = acres * 2.0,
                micronutrientTipEn = "Apply Zinc Sulphate @ 10 kg/acre at 25 days to prevent Khaira disease.",
                micronutrientTipKn = "ಖೈರಾ ರೋಗ ತಡೆಗಟ್ಟಲು 25ನೇ ದಿನಕ್ಕೆ ಎಕರೆಗೆ 10 ಕೆಜಿ ಸತುವಿನ ಸಲ್ಫೇಟ್ (Zinc Sulphate) ನೀಡಿ."
            )
            "sugarcane", "ಕಬ್ಬು" -> FertilizerRecommendation(
                crop = "Sugarcane (ಕಬ್ಬು)",
                acreage = acres,
                ureaBags = (acres * 4.5).toInt().coerceAtLeast(2),
                dapBags = (acres * 2.8).toInt().coerceAtLeast(1),
                mopBags = (acres * 2.0).toInt().coerceAtLeast(1),
                estimatedCost = acres * 4800.0,
                organicCompostTons = acres * 4.0,
                micronutrientTipEn = "Apply Ferrous Sulphate & Magnesium Sulphate with drip irrigation at 60 days.",
                micronutrientTipKn = "ನಾಟಿ ಮಾಡಿದ 60 ದಿನಕ್ಕೆ ಕಬ್ಬಿಣದ ಸಲ್ಫೇಟ್ ಮತ್ತು ಮೆಗ್ನೀಸಿಯಮ್ ಹನಿ ನೀರಾವರಿ ಮೂಲಕ ನೀಡಿ."
            )
            "maize", "corn", "ಮೆಕ್ಕೆಜೋಳ" -> FertilizerRecommendation(
                crop = "Maize (ಮೆಕ್ಕೆಜೋಳ)",
                acreage = acres,
                ureaBags = (acres * 2.5).toInt().coerceAtLeast(1),
                dapBags = (acres * 1.2).toInt().coerceAtLeast(1),
                mopBags = (acres * 0.8).toInt().coerceAtLeast(1),
                estimatedCost = acres * 2300.0,
                organicCompostTons = acres * 2.0,
                micronutrientTipEn = "Split nitrogen into 3 doses: basal, knee-high stage, and tasseling.",
                micronutrientTipKn = "ಸಾರಜನಕವನ್ನು ಮೂರು ಹಂತಗಳಲ್ಲಿ ನೀಡಿ: ಬಿತ್ತನೆ ಸಮಯದಲ್ಲಿ, ಮೊಣಕಾಲು ಎತ್ತರಕ್ಕೆ ಮತ್ತು ತೆನೆ ಮೂಡುವಾಗ."
            )
            else -> FertilizerRecommendation(
                crop = "Finger Millet / Ragi (ರಾಗಿ)",
                acreage = acres,
                ureaBags = (acres * 1.8).toInt().coerceAtLeast(1),
                dapBags = (acres * 1.0).toInt().coerceAtLeast(1),
                mopBags = (acres * 0.7).toInt().coerceAtLeast(1),
                estimatedCost = acres * 1900.0,
                organicCompostTons = acres * 1.5,
                micronutrientTipEn = "Incorporate 2 tons of well-decomposed FYM or vermicompost 3 weeks prior to sowing.",
                micronutrientTipKn = "ಬಿತ್ತನೆಗೆ 3 ವಾರ ಮೊದಲು 2 ಟನ್ ಎರೆಹುಳು ಗೊಬ್ಬರ ಅಥವಾ ಕೊಟ್ಟಿಗೆ ಗೊಬ್ಬರವನ್ನು ಮಣ್ಣಿಗೆ ಸೇರಿಸಿ."
            )
        }
    }
}
