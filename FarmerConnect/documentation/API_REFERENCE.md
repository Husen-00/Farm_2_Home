# FarmerConnect Spring Boot REST API Reference

Base URL (local): `http://localhost:8080`
Android Emulator Loopback: `http://10.0.2.2:8080/api`
Swagger UI (when backend is running): `http://localhost:8080/swagger-ui.html`

## Endpoints by Controller

### Authentication (`/api/auth`)
- `POST /api/auth/send-otp?phone=...` — Request mobile OTP verification code.
- `POST /api/auth/login` — Authenticate with phone/role credentials.
- `POST /api/auth/register` — Register a new Farmer or Buyer profile.

### Marketplace Produce (`/api/products`)
- `GET /api/products?search=&category=` — Browse produce listings with optional search and category filter.
- `POST /api/products` — Publish a new crop listing.
- `GET /api/products/{id}` — Retrieve product details by ID.

### Orders (`/api/orders`)
- `GET /api/orders` — List order history and statuses.
- `POST /api/orders` — Place a new produce purchase order.
- `PUT /api/orders/{id}/status?status=` — Update order confirmation or delivery status.

### Reviews (`/api/reviews`)
- `GET /api/reviews/product/{productId}` — Fetch product reviews.
- `POST /api/reviews/product/{productId}` — Submit a product rating and review.

### Live APMC Mandi Prices (`/api/mandi-prices`)
- `GET /api/mandi-prices?district=&crop=` — Query Karnataka APMC min/max/modal crop prices.

### Weather Telemetry (`/api/weather`)
- `GET /api/weather/current?lat=12.52&lon=76.89` — Current agricultural weather & irrigation advisory.

### Equipment Rental (`/api/equipment`)
- `GET /api/equipment` — List tractors, harvesters, and farm machinery.
- `POST /api/equipment/book` — Book equipment by hours/date.

### Agricultural Labour (`/api/labor`)
- `GET /api/labor` — List local agricultural labour teams.
- `POST /api/labor/book` — Book a labour team.

### Krishi AI Assistant (`/api/chat`)
- `POST /api/chat/ask` — Submit bilingual farming question (`prompt`, `language`).
- `POST /api/chat/diagnose-disease` — Analyze crop disease (`crop`).
