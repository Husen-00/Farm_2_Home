# FarmerConnect Database Setup (PostgreSQL)

## Files
- `schema.sql`: Core PostgreSQL DDL creating all 11 tables (`users`, `farmer_profiles`, `buyer_profiles`, `products`, `orders`, `mandi_prices`, `equipment_listings`, `equipment_bookings`, `transport_requests`, `chat_messages`, `farm_transactions`).
- `seed_data.sql`: Optional sample data for local development and testing.

## Setup Commands
```bash
# 1. Create the PostgreSQL database
createdb -U postgres farmerconnect_db

# 2. Apply schema
psql -U postgres -d farmerconnect_db -f database/schema.sql

# 3. (Optional) Load sample seed records
psql -U postgres -d farmerconnect_db -f database/seed_data.sql
```

Or via Docker:
```bash
docker run --name farmerconnect-postgres \
  -e POSTGRES_DB=farmerconnect_db \
  -e POSTGRES_PASSWORD=your_postgres_password \
  -p 5432:5432 -d postgres:15
```
