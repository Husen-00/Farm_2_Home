# FarmerConnect Architecture

## Overview
FarmerConnect is a bilingual (**English & Kannada**) smart agricultural mobile platform consisting of:
1. **Frontend (Android Native):** Kotlin + Jetpack Compose + Material Design 3 (MVVM with `MainViewModel`, `FarmerRepository`, `RealWeatherService`, and `KrishiAiEngine`).
2. **Backend (Java Spring Boot 3.3.4):** Maven project on Java 17 providing REST endpoints, OpenAPI/Swagger documentation, and PostgreSQL schema readiness.
3. **Database (PostgreSQL):** 11 relational tables defined in `database/schema.sql` and `backend/src/main/resources/schema.sql`.

## Frontend Modules (`frontend/app/src/main/java/com/example/`)
- `MainActivity.kt` — Edge-to-edge host, Scaffold, TopBar/BottomBar, Notification Center dialog, BackHandler.
- `ui/theme/` — **Premium Agricultural Green Theme** (`Color.kt`, `Theme.kt`, `Type.kt`) using Plus Jakarta Sans & Inter fonts.
- `ui/components/TopAndBottomBars.kt` — Forest Green top app bar and Pure White 5-tab navigation bar.
- `ui/screens/` — `HomeScreen.kt`, `MarketplaceScreen.kt`, `KrishiAiScreen.kt`, `ServicesScreen.kt`, `ProfileScreen.kt`.
- `ui/viewmodel/MainViewModel.kt` — StateFlow orchestration, Android `SpeechRecognizer` voice input, `TextToSpeech` readout, and repository actions.
- `data/FarmerRepository.kt` — In-app data flows for produce, orders, mandi prices, weather, equipment, labour, transport, schemes, accounting passbook, notifications, and admin metrics.
- `data/RealWeatherService.kt` — Live Open-Meteo API client for Mandya (`12.52° N, 76.89° E`) with regional offline fallback.
- `data/KrishiAiEngine.kt` — Gemini REST client (`BuildConfig.GEMINI_API_KEY`) for bilingual Q&A and Crop Doctor image diagnosis, with built-in agronomic fallback.
- `localization/AppStrings.kt` — English & Kannada strings across the app.

## Backend Modules (`backend/src/main/java/com/farmerconnect/`)
- `FarmerConnectApplication.java` — Spring Boot 3 application entry point.
- `controller/` — `AuthController`, `ProductController`, `OrderController`, `MandiPriceController`, `WeatherController`, `EquipmentController`, `LaborController`, `KrishiChatController`, `ReviewController`.
- `src/main/resources/application.yml` — Spring Boot & PostgreSQL configuration driven by environment variables.
- `src/main/resources/schema.sql` — PostgreSQL DDL schema.

> **Note on Current Persistence State:** The backend controllers currently ship with fast in-memory collections for immediate local REST testing alongside the complete PostgreSQL `schema.sql` and Spring Data JPA dependencies configured in `pom.xml`.
