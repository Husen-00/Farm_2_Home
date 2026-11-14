package com.example.localization

enum class Language(val displayName: String, val nativeName: String, val code: String) {
    ENGLISH("English", "English", "en"),
    KANNADA("Kannada", "ಕನ್ನಡ", "kn")
}

data class LocalizedText(
    val en: String,
    val kn: String
) {
    fun get(lang: Language): String = if (lang == Language.KANNADA) kn else en
}

object AppStrings {
    val appTitle = LocalizedText("FarmerConnect", "ಫಾರ್ಮರ್ ಕನೆಕ್ಟ್")
    val appSubtitle = LocalizedText("Smart Farming Assistant", "ಸ್ಮಾರ್ಟ್ ಕೃಷಿ ಸಹಾಯಕ")

    // Navigation Tabs
    val tabHome = LocalizedText("Home", "ಮುಖಪುಟ")
    val tabMarketplace = LocalizedText("Marketplace", "ಮಾರುಕಟ್ಟೆ")
    val tabAi = LocalizedText("Krishi AI", "ಕೃಷಿ AI")
    val tabServices = LocalizedText("Services", "ಸೇವೆಗಳು")
    val tabProfile = LocalizedText("Profile", "ಪ್ರೊಫೈಲ್")

    // Roles & Auth
    val roleFarmer = LocalizedText("Farmer", "ರೈತ")
    val roleBuyer = LocalizedText("Buyer / Trader", "ಖರೀದಿದಾರ / ವರ್ತಕ")
    val roleAdmin = LocalizedText("Admin", "ಆಡಳಿತಾಧಿಕಾರಿ")
    val switchRole = LocalizedText("Switch Mode", "ಮೋಡ್ ಬದಲಿಸಿ")
    val loginTitle = LocalizedText("Farmer & Buyer Login", "ರೈತ ಮತ್ತು ಖರೀದಿದಾರರ ಲಾಗಿನ್")
    val registerTitle = LocalizedText("Register New Account", "ಹೊಸ ಖಾತೆ ತೆರೆಯಿರಿ")
    val phoneNumber = LocalizedText("Mobile Phone Number", "ಮೊಬೈಲ್ ಸಂಖ್ಯೆ")
    val password = LocalizedText("Password / OTP", "ಪಾಸ್‌ವರ್ಡ್ / ಓಟಿಪಿ")
    val loginButton = LocalizedText("Login Securely", "ಸುರಕ್ಷಿತವಾಗಿ ಲಾಗಿನ್ ಆಗಿ")
    val registerButton = LocalizedText("Register", "ನೋಂದಾಯಿಸಿ")
    val logoutButton = LocalizedText("Logout", "ಲಾಗ್‌ಔಟ್")
    val editProfile = LocalizedText("Edit Farm Profile", "ಪ್ರೊಫೈಲ್ ತಿದ್ದುಪಡಿ")
    val authSuccess = LocalizedText("Authenticated successfully!", "ಯಶಸ್ವಿಯಾಗಿ ಲಾಗಿನ್ ಆಗಿದ್ದೀರಿ!")

    // Home Screen
    val greetingFarmer = LocalizedText("Namaskara, Basavaraj!", "ನಮಸ್ಕಾರ, ಬಸವರಾಜ್!")
    val farmLocation = LocalizedText("Mandya, Karnataka", "ಮಂಡ್ಯ, ಕರ್ನಾಟಕ")
    val quickActions = LocalizedText("Quick Farm Services", "ತ್ವರಿತ ಕೃಷಿ ಸೇವೆಗಳು")
    val askKrishiAi = LocalizedText("Ask Krishi AI", "ಕೃಷಿ AI ಗೆ ಕೇಳಿ")
    val sellProduce = LocalizedText("Sell Produce", "ಬೆಳೆ ಮಾರಿ")
    val mandiPrices = LocalizedText("Mandi Bhav", "ಮಂಡಿ ದರಗಳು")
    val rentEquipment = LocalizedText("Rent Tractor", "ಉಪಕರಣ ಬಾಡಿಗೆ")
    val cropDoctor = LocalizedText("Crop Doctor", "ಬೆಳೆ ರೋಗ ಪರೀಕ್ಷೆ")
    val govtSchemes = LocalizedText("Govt Schemes", "ಸರ್ಕಾರಿ ಯೋಜನೆ")
    val priceTrends = LocalizedText("Price Trends", "ಬೆಲೆ ಪ್ರವೃತ್ತಿ")
    val farmLabour = LocalizedText("Farm Labour", "ಕೃಷಿ ಕಾರ್ಮಿಕರು")

    // Weather Card
    val weatherTitle = LocalizedText("Live Farm Weather", "ಲೈವ್ ಹವಾಮಾನ ವರದಿ")
    val liveVerified = LocalizedText("Live Satellite Telemetry", "ಲೈವ್ ಉಪಗ್ರಹ ವರದಿ")
    val todayRainfall = LocalizedText("Rain Forecast", "ಮಳೆಯ ಮುನ್ಸೂಚನೆ")
    val irrigationAdvice = LocalizedText("Irrigation Alert", "ನೀರಾವರಿ ಸಲಹೆ")
    val soilMoisture = LocalizedText("Soil Moisture", "ಮಣ್ಣಿನ ತೇವಾಂಶ")

    // Mandi Price Ticker & History
    val mandiTickerTitle = LocalizedText("Karnataka APMC Live Prices", "ಕರ್ನಾಟಕ ಎ.ಪಿ.ಎಂ.ಸಿ ಲೈವ್ ಮಂಡಿ ದರ")
    val minPrice = LocalizedText("Min", "ಕನಿಷ್ಠ")
    val maxPrice = LocalizedText("Max", "ಗರಿಷ್ಠ")
    val modalPrice = LocalizedText("Modal Price", "ಮಾದರಿ ಬೆಲೆ")
    val perQuintal = LocalizedText("/ Quintal", "/ ಕ್ವಿಂಟಾಲ್")
    val marketSource = LocalizedText("Verified: Karnataka APMC Realtime", "ದೃಢೀಕರಿಸಲಾಗಿದೆ: ಕರ್ನಾಟಕ ಎ.ಪಿ.ಎಂ.ಸಿ")
    val viewHistoricalPrices = LocalizedText("7-Day Mandi Price History", "7 ದಿನಗಳ ಮಂಡಿ ದರ ಇತಿಹಾಸ")
    val priceChange7d = LocalizedText("7-Day Price Variation", "7 ದಿನಗಳ ಬೆಲೆ ವ್ಯತ್ಯಾಸ")

    // Marketplace & Reviews
    val searchProduce = LocalizedText("Search crops, grains, vegetables...", "ಧಾನ್ಯಗಳು, ತರಕಾರಿ, ಹಣ್ಣುಗಳನ್ನು ಹುಡುಕಿ...")
    val allCrops = LocalizedText("All Crops", "ಎಲ್ಲ ಬೆಳೆಗಳು")
    val cereals = LocalizedText("Grains & Cereals", "ಧಾನ್ಯಗಳು")
    val pulses = LocalizedText("Pulses", "ಕಾಳುಗಳು")
    val vegetables = LocalizedText("Vegetables", "ತರಕಾರಿಗಳು")
    val fruits = LocalizedText("Fruits", "ಹಣ್ಣುಗಳು")
    val commercial = LocalizedText("Commercial", "ವಾಣಿಜ್ಯ ಬೆಳೆಗಳು")
    val addProduct = LocalizedText("List Your Produce", "ನಿಮ್ಮ ಬೆಳೆ ಮಾರಾಟಕ್ಕೆ ಸೇರಿಸಿ")
    val submitListing = LocalizedText("Publish Listing", "ಪ್ರಕಟಿಸಿ")
    val buyNow = LocalizedText("Place Order", "ಆರ್ಡರ್ ಮಾಡಿ")
    val contactFarmer = LocalizedText("Call Farmer", "ರೈತರನ್ನು ಸಂಪರ್ಕಿಸಿ")
    val orderSuccess = LocalizedText("Order Placed Successfully!", "ಆರ್ಡರ್ ಯಶಸ್ವಿಯಾಗಿ ದಾಖಲಾಗಿದೆ!")
    val orderStatus = LocalizedText("Order Status", "ಆರ್ಡರ್ ಸ್ಥಿತಿ")
    val cancelOrder = LocalizedText("Cancel Order", "ಆರ್ಡರ್ ರದ್ದುಮಾಡಿ")
    val payViaUpi = LocalizedText("Pay via UPI / QR Code", "ಯುಪಿಐ / ಕ್ಯೂಆರ್ ಕೋಡ್ ಮೂಲಕ ಪಾವತಿಸಿ")
    val scanUpiQr = LocalizedText("Scan UPI QR Code to Pay Farmer", "ರೈತರಿಗೆ ಹಣ ಪಾವತಿಸಲು ಕ್ಯೂಆರ್ ಸ್ಕ್ಯಾನ್ ಮಾಡಿ")
    val markAsPaid = LocalizedText("Simulate Payment Complete", "ಪಾವತಿ ಪೂರ್ಣಗೊಂಡಿದೆ ಎಂದು ಗುರುತಿಸಿ")
    val viewReviews = LocalizedText("Reviews & Ratings", "ವಿಮರ್ಶೆಗಳು ಮತ್ತು ರೇಟಿಂಗ್")
    val writeReview = LocalizedText("Write a Review", "ವಿಮರ್ಶೆ ಬರೆಯಿರಿ")

    // Krishi AI Assistant
    val chatbotTitle = LocalizedText("Krishi AI Farming Assistant", "ಕೃಷಿ AI ಕೃಷಿ ಸಲಹೆಗಾರ")
    val typeQuestion = LocalizedText("Type agricultural question here...", "ನಿಮ್ಮ ಕೃಷಿ ಪ್ರಶ್ನೆಯನ್ನು ಇಲ್ಲಿ ಬರೆಯಿರಿ...")
    val listeningVoice = LocalizedText("Listening... Speak now in Kannada or English", "ಕೇಳಿಸಿಕೊಳ್ಳುತ್ತಿದೆ... ಕನ್ನಡ ಅಥವಾ ಇಂಗ್ಲಿಷ್‌ನಲ್ಲಿ ಮಾತನಾಡಿ")
    val scanPlantDisease = LocalizedText("Scan Crop Disease", "ಗಿಡದ ರೋಗ ಪರೀಕ್ಷಿಸಿ")
    val suggestedQuestions = LocalizedText("Suggested Questions", "ಪ್ರಮುಖ ಕೃಷಿ ಪ್ರಶ್ನೆಗಳು")
    val q1 = LocalizedText("How to control fall armyworm in maize?", "ಮೆಕ್ಕೆಜೋಳದಲ್ಲಿ ಸೈನಿಕ ಹುಳುವಿನ ಬಾಧೆ ನಿಯಂತ್ರಿಸುವುದು ಹೇಗೆ?")
    val q2 = LocalizedText("Best fertilizer schedule for sugarcane in Karnataka?", "ಕರ್ನಾಟಕದಲ್ಲಿ ಕಬ್ಬು ಬೆಳೆಗೆ ರಸಗೊಬ್ಬರ ನೀಡುವ ವಿಧಾನ ತಿಳಿಸಿ?")
    val q3 = LocalizedText("PM-KISAN 17th installment release status?", "ಪಿಎಂ-ಕಿಸಾನ್ ಮುಂದಿನ ಕಂತಿನ ಹಣ ಯಾವಾಗ ಬರುತ್ತದೆ?")
    val q4 = LocalizedText("How to manage blast disease in paddy crop?", "ಭತ್ತದ ಬೆಳೆಯ ಬೆಂಕಿ ರೋಗ ನಿಯಂತ್ರಣಕ್ಕೆ ಯಾವ ಔಷಧ ಸಿಂಪಡಿಸಬೇಕು?")

    val diseaseDiagnosis = LocalizedText("Crop Doctor & Disease Analysis", "ಬೆಳೆ ರೋಗ ಪತ್ತೆ ಮತ್ತು ಪರಿಹಾರ")
    val uploadPhoto = LocalizedText("Upload / Capture Leaf Photo", "ಎಲೆಯ ಫೋಟೋ ಅಪ್‌ಲೋಡ್ ಮಾಡಿ")
    val analyzeCrop = LocalizedText("Analyze Leaf / Plant Photo", "ಎಲೆಯ ಫೋಟೋ ಪರಿಶೀಲಿಸಿ")
    val aiDisclaimer = LocalizedText("AI Advisory • Not a Certified Chemical Prescription", "AI ಕೃಷಿ ಸಲಹೆ • ಅಧಿಕೃತ ಪ್ರಯೋಗಾಲಯ ದೃಢೀಕರಣವಲ್ಲ")
    val helplineContact = LocalizedText("Kisan Call Center: 1800-180-1551 (Toll Free)", "ಕಿಸಾನ್ ಕಾಲ್ ಸೆಂಟರ್: 1800-180-1551 (ಉಚಿತ ಕರೆ)")

    // Services
    val equipmentRental = LocalizedText("Equipment Rental & CHC", "ಕೃಷಿ ಉಪಕರಣ ಬಾಡಿಗೆ")
    val rentNow = LocalizedText("Book Rental", "ಬಾಡಿಗೆಗೆ ಬುಕ್ ಮಾಡಿ")
    val transportStorage = LocalizedText("Transport & Storage", "ಸಾರಿಗೆ ಮತ್ತು ಶೀತಲ ಉಗ್ರಾಣ")
    val bookTransport = LocalizedText("Request Agri-Transport", "ಕೃಷಿ ಸಾರಿಗೆ ವಾಹನ ಬುಕ್ ಮಾಡಿ")
    val coldStorage = LocalizedText("Nearby Cold Storages & Warehouses", "ಹತ್ತಿರದ ಶೀತಲ ಗೋದಾಮುಗಳು")
    val soilTesting = LocalizedText("Soil Health & Fertilizer Calculator", "ಮಣ್ಣು ಪರೀಕ್ಷೆ ಮತ್ತು ರಸಗೊಬ್ಬರ ಕ್ಯಾಲ್ಕುಲೇಟರ್")
    val govtSchemesTitle = LocalizedText("Government Welfare Schemes", "ರೈತ ಕಲ್ಯಾಣ ಯೋಜನೆಗಳು")
    val viewEligibility = LocalizedText("Check Eligibility & Apply", "ಅರ್ಹತೆ ಮತ್ತು ಅರ್ಜಿ ವಿಧಾನ")
    val labourTitle = LocalizedText("Agricultural Labour Booking", "ಕೃಷಿ ಕಾರ್ಮಿಕರ ತಂಡಗಳು")
    val bookLabour = LocalizedText("Hire Labour Team", "ಕಾರ್ಮಿಕರನ್ನು ಬುಕ್ ಮಾಡಿ")

    // Finance & Profile
    val farmAccounting = LocalizedText("Farm Passbook & Profit Tracker", "ಕೃಷಿ ಲೆಕ್ಕಪತ್ರ ಮತ್ತು ಆದಾಯ-ವೆಚ್ಚ")
    val netProfit = LocalizedText("Net Farm Profit", "ನಿವ್ವಳ ಲಾಭ")
    val addTransaction = LocalizedText("Log Income / Expense", "ಆದಾಯ / ಖರ್ಚು ನಮೂದಿಸಿ")
    val notifications = LocalizedText("Notifications & Alerts", "ಸೂಚನೆಗಳು ಮತ್ತು ಎಚ್ಚರಿಕೆಗಳು")
    val adminDashboard = LocalizedText("Administrator Control Panel", "ಆಡಳಿತಾಧಿಕಾರಿ ನಿಯಂತ್ರಣ ಫಲಕ")
    val changeLanguage = LocalizedText("Change Language / ಭಾಷೆ ಬದಲಿಸಿ", "ಭಾಷೆ ಬದಲಿಸಿ / Change Language")
}
