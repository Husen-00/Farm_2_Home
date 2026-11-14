package com.example.model

import java.util.UUID

enum class UserRole {
    FARMER,
    BUYER,
    ADMIN
}

data class UserProfile(
    val id: String = "user_001",
    val name: String = "Basavaraj Patil",
    val phone: String = "+91 98451 23456",
    val village: String = "Srirangapatna",
    val district: String = "Mandya",
    val state: String = "Karnataka",
    val role: UserRole = UserRole.FARMER,
    val landAcres: Double = 4.5,
    val cropsGrown: List<String> = listOf("Sugarcane", "Paddy", "Ragi"),
    val isAuthenticated: Boolean = true,
    val authToken: String = "jwt_token_sample"
)

enum class ProductCategory {
    CEREALS,
    PULSES,
    VEGETABLES,
    FRUITS,
    COMMERCIAL
}

data class Review(
    val id: String = UUID.randomUUID().toString(),
    val reviewerName: String,
    val rating: Int,
    val comment: String,
    val date: String
)

data class Product(
    val id: String = UUID.randomUUID().toString(),
    val farmerId: String = "farmer_01",
    val farmerName: String = "Basavaraj Patil",
    val farmerPhone: String = "+91 98451 23456",
    val titleEn: String,
    val titleKn: String,
    val category: ProductCategory,
    val price: Double,
    val unit: String = "Quintal",
    val availableQuantity: Double,
    val grade: String = "Grade A",
    val location: String = "Mandya, Karnataka",
    val rating: Double = 4.8,
    val isOrganic: Boolean = true,
    val iconEmoji: String = "🌾",
    val reviews: List<Review> = listOf(
        Review(reviewerName = "Kaveri Agro Traders", rating = 5, comment = "Excellent moisture level and grain purity.", date = "Yesterday"),
        Review(reviewerName = "Shree Murugan Rice Mill", rating = 4, comment = "Good quality Sona Masoori produce.", date = "3 days ago")
    )
)

enum class OrderStatus {
    PLACED,
    CONFIRMED,
    IN_TRANSIT,
    DELIVERED,
    CANCELLED
}

data class Order(
    val id: String = "ORD-${System.currentTimeMillis() % 100000}",
    val productId: String,
    val productTitle: String,
    val buyerName: String,
    val buyerPhone: String,
    val quantity: Double,
    val unit: String,
    val totalAmount: Double,
    val status: OrderStatus = OrderStatus.PLACED,
    val orderDate: String,
    val deliveryAddress: String,
    val paymentStatus: String = "Pending", // Pending, Paid via UPI, Cash on Delivery
    val upiTransactionId: String? = null
)

enum class PriceTrend {
    UP,
    DOWN,
    STABLE
}

data class PricePoint(
    val date: String,
    val price: Double
)

data class MandiPrice(
    val id: String,
    val cropNameEn: String,
    val cropNameKn: String,
    val market: String,
    val district: String,
    val minPrice: Double,
    val maxPrice: Double,
    val modalPrice: Double,
    val date: String,
    val trend: PriceTrend,
    val percentChange: Double,
    val history: List<PricePoint> = listOf(
        PricePoint("Day 1", modalPrice * 0.94),
        PricePoint("Day 2", modalPrice * 0.96),
        PricePoint("Day 3", modalPrice * 0.95),
        PricePoint("Day 4", modalPrice * 0.98),
        PricePoint("Day 5", modalPrice * 0.99),
        PricePoint("Day 6", modalPrice * 0.98),
        PricePoint("Day 7", modalPrice)
    )
)

data class DailyForecast(
    val day: String,
    val tempMax: Int,
    val tempMin: Int,
    val condition: String,
    val rainProb: Int
)

data class WeatherAdvisory(
    val temp: Int = 29,
    val humidity: Int = 74,
    val windKmh: Int = 14,
    val condition: String = "Partly Cloudy with Evening Showers",
    val conditionKn: String = "ಭಾಗಶಃ ಮೋಡ ಕವಿದ ವಾತಾವರಣ, ಸಂಜೆ ಸಾಧಾರಣ ಮಳೆ",
    val location: String = "Mandya, Karnataka (12.52° N, 76.89° E)",
    val alertEn: String = "Rainfall expected between 4:00 PM and 7:00 PM (15mm).",
    val alertKn: String = "ಸಂಜೆ 4:00 ರಿಂದ 7:00 ರ ನಡುವೆ ಸಾಧಾರಣ ಮಳೆಯಾಗುವ ಸಾಧ್ಯತೆ (15 ಮಿ.ಮೀ).",
    val irrigationAdviceEn: String = "Hold irrigation today. Ensure drainage in low-lying paddy nurseries.",
    val irrigationAdviceKn: String = "ಇಂದು ನೀರುಣಿಸಬೇಡಿ. ಭತ್ತದ ಮಡಿಗಳಲ್ಲಿ ಹೆಚ್ಚುವರಿ ನೀರು ಹರಿದುಹೋಗಲು ಕಾಲುವೆ ಸಿದ್ಧಪಡಿಸಿ.",
    val isLiveApi: Boolean = true,
    val apiSource: String = "Open-Meteo Verified Live Telemetry",
    val forecasts: List<DailyForecast> = listOf(
        DailyForecast("Today", 29, 21, "Scattered Showers", 65),
        DailyForecast("Tomorrow", 31, 20, "Sunny Intervals", 20),
        DailyForecast("Wed", 30, 21, "Clear & Warm", 10),
        DailyForecast("Thu", 32, 22, "Light Breeze", 15),
        DailyForecast("Fri", 30, 20, "Cloudy", 40)
    )
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isAudioAvailable: Boolean = !isUser,
    val isAiDisclaimer: Boolean = !isUser
)

data class DiseaseDiagnosis(
    val id: String = UUID.randomUUID().toString(),
    val cropName: String,
    val diseaseNameEn: String,
    val diseaseNameKn: String,
    val confidenceScore: Int = 94,
    val symptomsEn: String,
    val symptomsKn: String,
    val organicRemedyEn: String,
    val organicRemedyKn: String,
    val chemicalRemedyEn: String,
    val chemicalRemedyKn: String,
    val severity: String = "Moderate",
    val isAiGenerated: Boolean = true
)

data class Equipment(
    val id: String,
    val nameEn: String,
    val nameKn: String,
    val ownerName: String,
    val ownerPhone: String,
    val hpOrCapacity: String,
    val hourlyRate: Double,
    val dailyRate: Double,
    val location: String,
    val rating: Double,
    val isAvailable: Boolean = true,
    val emoji: String = "🚜"
)

data class EquipmentBooking(
    val id: String = "EQP-${System.currentTimeMillis() % 10000}",
    val equipmentName: String,
    val farmerName: String,
    val date: String,
    val hours: Int,
    val totalCost: Double,
    val status: String = "Confirmed"
)

data class LaborListing(
    val id: String = UUID.randomUUID().toString(),
    val teamLeadName: String,
    val phone: String,
    val village: String,
    val skillsEn: String,
    val skillsKn: String,
    val teamSize: Int,
    val dailyWagePerWorker: Double,
    val isAvailable: Boolean = true
)

data class LaborBooking(
    val id: String = "LAB-${System.currentTimeMillis() % 10000}",
    val teamLeadName: String,
    val activity: String,
    val workersCount: Int,
    val date: String,
    val totalWage: Double,
    val status: String = "Confirmed"
)

data class TransportRequest(
    val id: String = "TRN-${System.currentTimeMillis() % 10000}",
    val pickupVillage: String,
    val destinationMandi: String,
    val cropName: String,
    val weightQuintals: Double,
    val vehicleType: String,
    val estimatedFare: Double,
    val status: String = "Driver Assigned"
)

data class GovernmentScheme(
    val id: String,
    val titleEn: String,
    val titleKn: String,
    val authority: String,
    val benefitEn: String,
    val benefitKn: String,
    val eligibilityEn: String,
    val eligibilityKn: String,
    val documentsEn: String,
    val documentsKn: String,
    val officialUrl: String
)

data class FarmTransaction(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amount: Double,
    val isIncome: Boolean,
    val category: String,
    val date: String
)

data class FarmNotification(
    val id: String = UUID.randomUUID().toString(),
    val titleEn: String,
    val titleKn: String,
    val descriptionEn: String,
    val descriptionKn: String,
    val timestamp: String,
    val iconEmoji: String,
    val isRead: Boolean = false,
    val category: String // WEATHER, PRICE, ORDER, SCHEME
)

data class FertilizerRecommendation(
    val crop: String,
    val acreage: Double,
    val ureaBags: Int,
    val dapBags: Int,
    val mopBags: Int,
    val estimatedCost: Double,
    val organicCompostTons: Double,
    val micronutrientTipEn: String,
    val micronutrientTipKn: String
)

data class AdminMetric(
    val totalFarmers: Int = 1420,
    val totalBuyers: Int = 310,
    val activeProduceListings: Int = 86,
    val completedOrdersValue: Double = 2845000.0,
    val activeEquipmentListings: Int = 42,
    val resolvedSupportQueries: Int = 95
)
