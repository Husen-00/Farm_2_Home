# FarmerConnect – Smart Farming Assistant (Backend Architecture)

FarmerConnect is a modern agricultural management platform with an Android client and a Spring Boot 3 Java REST backend using PostgreSQL.

---

## 1. System Architecture

```
[ Android App (Jetpack Compose) ] 
       │ 
       ├─► (REST APIs / JSON) ──► [ Spring Boot 3 Java Backend ]
       │                                     │
       │                                     ├─► [ PostgreSQL Database ]
       │                                     └─► [ Gemini AI API (Krishi Chat) ]
       │
       └─► Offline-First Local Cache & Zero-Latency Engine
```

---

## 2. PostgreSQL Database Setup

1. Install PostgreSQL 14+ or start with Docker:
   ```bash
   docker run --name farmerconnect-postgres -e POSTGRES_DB=farmerconnect_db -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:15
   ```

2. Run the provided SQL migration script located at:
   `backend/src/main/resources/schema.sql`

---

## 3. Environment Variables Configuration

| Variable | Description | Default Value |
|---|---|---|
| `SPRING_DATASOURCE_URL` | PostgreSQL JDBC connection URL | `jdbc:postgresql://localhost:5432/farmerconnect_db` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `postgres` |
| `JWT_SECRET` | 256-bit secret key for HMAC-SHA JWT | Pre-configured in `application.yml` |
| `GEMINI_API_KEY` | Google Gemini AI Key | Injected via AI Studio Secrets Panel |

---

## 4. Running the Spring Boot Backend

### Using Maven:
```bash
cd backend
mvn clean spring-boot:run
```

Once running:
- **API Base URL:** `http://localhost:8080/api`
- **Swagger UI Interactive Documentation:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON Spec:** `http://localhost:8080/api-docs`

---

## 5. REST API Endpoints Overview

- `POST /api/auth/login`: Phone/OTP login with JWT token issuance
- `GET  /api/products`: Browse produce with crop and district filters
- `POST /api/products`: Farmers list produce (grade, price, quintals)
- `POST /api/orders`: Buyers place produce orders
- `GET  /api/mandi-prices`: Live Karnataka APMC mandi prices
- `GET  /api/equipment`: Equipment rental and custom hiring center listings
- `POST /api/equipment/book`: Book tractors and harvesters
- `POST /api/chat/ask`: Krishi AI Assistant Q&A in Kannada and English
- `POST /api/chat/diagnose-disease`: AI crop disease diagnosis

---

## 6. Android Mobile Application Integration

The Android mobile application is pre-configured with:
1. **Bilingual Language Switcher:** Instant toggle between English and Kannada (ಕನ್ನಡ) on all screens.
2. **Krishi AI Farming Chatbot:** Interactive speech-to-text, audio readout (Text-to-Speech), and Crop Doctor plant disease diagnosis.
3. **Live APMC Mandi Prices & Weather Advisory.**
4. **Marketplace:** Produce listings, order placement, and tracking.
5. **Configurable Endpoint:** In the mobile app's **Profile** tab, you can point to your live server URL or default to the Android emulator loopback `http://10.0.2.2:8080/api`.
