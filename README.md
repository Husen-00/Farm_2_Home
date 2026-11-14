# FarmerConnect – Smart Farming Assistant

A bilingual (**Kannada & English**) agricultural platform built with an **Android Kotlin / Jetpack Compose** mobile frontend (styled with the **Premium Agricultural Green Theme**) and a **Java 17 Spring Boot 3** REST backend backed by a **PostgreSQL** schema.

---

## 1. Project Folder Structure

```text
FarmerConnect/
├── frontend/          # Android Kotlin + Jetpack Compose app (Gradle project)
├── backend/           # Java 17 Spring Boot 3 REST API (Maven project)
├── database/          # PostgreSQL schema.sql, seed_data.sql, setup notes
├── assets/            # App icon artwork, hero banners, and local .ttf fonts
├── documentation/     # Architecture, REST API Reference, and Design System docs
├── .vscode/           # Recommended VS Code extensions and workspace settings
├── .env.example       # Template for environment variables (no real secrets)
├── .gitignore
└── README.md
```

---

## 2. Required Software Installations

Install the following tools before opening and running the project locally:

1. **Visual Studio Code** (latest) + recommended extensions (prompted automatically via `.vscode/extensions.json`):
   - **Extension Pack for Java** (`vscjava.vscode-java-pack`)
   - **Spring Boot Extension Pack** (`vmware.vscode-boot-dev-pack`)
   - **Kotlin** (`fwcd.kotlin`)
   - **Gradle for Java** (`vscjava.vscode-gradle`)
   - **PostgreSQL / SQLTools** (`mtxr.sqltools`)
2. **Java Development Kit (JDK) 17 or 21** (required for Spring Boot and Android Gradle)
3. **Apache Maven 3.9+** (`mvn -v` to verify)
4. **Gradle 9.x / Android Studio SDK** (Android SDK Platform 36 & Build Tools, or Android Emulator for running the mobile APK)
5. **PostgreSQL 14+** (or Docker Desktop to run PostgreSQL 15 in a container)

---

## 3. Opening the Project in Visual Studio Code

1. Extract `FarmerConnect_Complete_Project.zip` to a local folder (for example, `C:\Projects\FarmerConnect` or `~/Projects/FarmerConnect`).
2. Open **Visual Studio Code** → **File → Open Folder...** → select the extracted `FarmerConnect/` root folder.
3. Click **Install All** when VS Code suggests the workspace extensions from `.vscode/extensions.json`.
4. Open two integrated terminals in VS Code (`Ctrl+~`): one for `backend/` and one for `frontend/`.

---

## 4. Configure and Start PostgreSQL

### Option A: Local PostgreSQL
```bash
createdb -U postgres farmerconnect_db
psql -U postgres -d farmerconnect_db -f database/schema.sql
psql -U postgres -d farmerconnect_db -f database/seed_data.sql
```

### Option B: Docker
```bash
docker run --name farmerconnect-postgres \
  -e POSTGRES_DB=farmerconnect_db \
  -e POSTGRES_PASSWORD=your_postgres_password \
  -p 5432:5432 -d postgres:15

# Apply schema after container starts:
docker exec -i farmerconnect-postgres psql -U postgres -d farmerconnect_db < database/schema.sql
docker exec -i farmerconnect-postgres psql -U postgres -d farmerconnect_db < database/seed_data.sql
```

---

## 5. Configure and Start the Java Spring Boot Backend

1. Copy `backend/.env.example` to `backend/.env` (or export variables in your shell) and provide real local values:
   - `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/farmerconnect_db`
   - `SPRING_DATASOURCE_USERNAME=postgres`
   - `SPRING_DATASOURCE_PASSWORD=your_postgres_password`
   - `JWT_SECRET=<your-256-bit-random-secret>`
   - `GEMINI_API_KEY=<your-gemini-api-key>`
2. Review `backend/src/main/resources/application.yml` (all sensitive values default to placeholders and read from environment variables).
3. Start Spring Boot from the VS Code terminal:
   ```bash
   cd backend
   export SPRING_DATASOURCE_PASSWORD=your_postgres_password
   export JWT_SECRET=your_long_random_32_plus_char_secret_key
   export GEMINI_API_KEY=your_gemini_api_key
   mvn spring-boot:run
   ```
4. Verify the backend is running:
   - API Base: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 6. Configure, Build, and Run the Android Frontend

The frontend is an **Android Jetpack Compose (Kotlin)** application located in `frontend/` (`app/`, `build.gradle.kts`, `settings.gradle.kts`).

1. **Create `.env` for the Android app:**
   ```bash
   cd frontend
   cp .env.example .env
   ```
   Edit `frontend/.env` and set `GEMINI_API_KEY=<your-gemini-api-key>`.
2. **Generate a local `debug.keystore` (first time only):**
   `frontend/app/build.gradle.kts` uses `debug.keystore` for debug signing (excluded from ZIP/Git for security). Generate one locally:
   ```bash
   cd frontend
   keytool -genkey -v -keystore debug.keystore -storepass android \
     -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 -validity 10000 \
     -dname "CN=Android Debug,O=Android,C=US"
   ```
3. **Ensure `ANDROID_SDK_ROOT` / `ANDROID_HOME` is set** (or create `frontend/local.properties` locally with `sdk.dir=/path/to/Android/Sdk` — never commit `local.properties`).
4. **Build and run tests:**
   ```bash
   cd frontend
   gradle :app:testDebugUnitTest
   gradle :app:assembleDebug
   ```
5. **Install on a running Android Emulator or USB device:**
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
   *(Or open `frontend/` in Android Studio and click **Run ▶**.)*

---

## 7. Connecting Frontend and Backend

- Start the Spring Boot server on port `8080`.
- When running the Android app in an **Android Emulator**, `http://10.0.2.2:8080/api` maps automatically to `http://localhost:8080/api` on your computer.
- When running on a **physical Android phone**, connect both devices to the same Wi-Fi network, open the **Profile** tab inside FarmerConnect, tap **Configure Server URL**, and enter `http://<YOUR_PC_LAN_IP>:8080/api`.

---

## 8. Configuring Gemini AI, Weather, and Market APIs

- **Gemini AI (Krishi AI & Crop Doctor):**
  - Android client (`KrishiAiEngine.kt`) reads `BuildConfig.GEMINI_API_KEY` from `frontend/.env` via the Secrets Gradle Plugin and falls back gracefully to the built-in bilingual agronomic engine when offline or unconfigured.
  - Spring Boot (`application.yml`) reads `GEMINI_API_KEY` from the environment.
- **Weather Forecasts:**
  - `RealWeatherService.kt` calls the public **Open-Meteo API** (`https://api.open-meteo.com/v1/forecast`) directly without requiring an API key, with an automatic offline fallback when network access is unavailable.
- **APMC Market Prices:**
  - Served from `FarmerRepository.kt` on the client and `/api/mandi-prices` on the Spring Boot backend (`MandiPriceController.java`).

---

## 9. Troubleshooting Common Errors

- **`debug.keystore not found` during Gradle build:** Run the `keytool -genkey` command in Section 6 inside `frontend/`.
- **`SDK location not found`:** Set `ANDROID_HOME` / `ANDROID_SDK_ROOT` in your environment or add `sdk.dir=...` in `frontend/local.properties`.
- **Android Emulator cannot reach `localhost:8080`:** Use `http://10.0.2.2:8080/api` instead of `localhost`.
- **PostgreSQL connection refused:** Verify PostgreSQL is running on port `5432`, `farmerconnect_db` exists, and `SPRING_DATASOURCE_PASSWORD` matches your local instance.
- **`./gradlew` missing:** This project uses system `gradle` (or you can run `gradle wrapper` once inside `frontend/` to generate wrapper scripts).
