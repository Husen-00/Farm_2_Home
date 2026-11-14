-- ==========================================================
-- FarmerConnect – Smart Farming Assistant PostgreSQL Schema
-- Database: farmerconnect_db
-- ==========================================================

-- 1. Users & Roles
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    phone VARCHAR(20) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'FARMER', -- FARMER, BUYER, ADMIN
    village VARCHAR(100),
    district VARCHAR(100),
    state VARCHAR(100) DEFAULT 'Karnataka',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Farmer Profiles
CREATE TABLE IF NOT EXISTS farmer_profiles (
    user_id VARCHAR(64) PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    land_area_acres NUMERIC(6, 2) DEFAULT 0.0,
    primary_crops TEXT[], -- e.g. ARRAY['Paddy', 'Sugarcane', 'Ragi']
    fruits_fid VARCHAR(50), -- Karnataka FRUITS Farmer ID
    soil_health_card_id VARCHAR(50),
    bank_account_verified BOOLEAN DEFAULT FALSE
);

-- 3. Buyer Profiles
CREATE TABLE IF NOT EXISTS buyer_profiles (
    user_id VARCHAR(64) PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    business_name VARCHAR(150),
    gst_or_trade_license VARCHAR(50),
    apmc_trader_id VARCHAR(50)
);

-- 4. Products (Marketplace produce listings)
CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(64) PRIMARY KEY,
    farmer_id VARCHAR(64) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title_en VARCHAR(200) NOT NULL,
    title_kn VARCHAR(200) NOT NULL,
    category VARCHAR(50) NOT NULL, -- CEREALS, PULSES, VEGETABLES, FRUITS, COMMERCIAL
    price_per_unit NUMERIC(10, 2) NOT NULL,
    unit VARCHAR(20) NOT NULL DEFAULT 'Quintal',
    quantity_available NUMERIC(10, 2) NOT NULL,
    quality_grade VARCHAR(50) DEFAULT 'Grade A',
    location VARCHAR(150) NOT NULL,
    is_organic BOOLEAN DEFAULT TRUE,
    rating NUMERIC(3, 2) DEFAULT 5.0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 5. Orders & Transactions
CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(64) PRIMARY KEY,
    product_id VARCHAR(64) NOT NULL REFERENCES products(id),
    buyer_id VARCHAR(64) NOT NULL REFERENCES users(id),
    quantity NUMERIC(10, 2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PLACED', -- PLACED, CONFIRMED, IN_TRANSIT, DELIVERED, CANCELLED
    delivery_address TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 6. Live APMC Mandi Prices
CREATE TABLE IF NOT EXISTS mandi_prices (
    id VARCHAR(64) PRIMARY KEY,
    crop_name_en VARCHAR(100) NOT NULL,
    crop_name_kn VARCHAR(100) NOT NULL,
    market_name VARCHAR(100) NOT NULL,
    district VARCHAR(100) NOT NULL,
    min_price NUMERIC(10, 2) NOT NULL,
    max_price NUMERIC(10, 2) NOT NULL,
    modal_price NUMERIC(10, 2) NOT NULL,
    price_date DATE NOT NULL,
    trend VARCHAR(20) DEFAULT 'STABLE',
    percent_change NUMERIC(5, 2) DEFAULT 0.0
);

-- 7. Custom Hiring & Equipment Listings
CREATE TABLE IF NOT EXISTS equipment_listings (
    id VARCHAR(64) PRIMARY KEY,
    owner_id VARCHAR(64) NOT NULL REFERENCES users(id),
    name_en VARCHAR(150) NOT NULL,
    name_kn VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    specifications VARCHAR(255),
    hourly_rate NUMERIC(10, 2) NOT NULL,
    daily_rate NUMERIC(10, 2) NOT NULL,
    location VARCHAR(150) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS equipment_bookings (
    id VARCHAR(64) PRIMARY KEY,
    equipment_id VARCHAR(64) NOT NULL REFERENCES equipment_listings(id),
    renter_id VARCHAR(64) NOT NULL REFERENCES users(id),
    booking_date VARCHAR(50) NOT NULL,
    duration_hours INT NOT NULL,
    total_cost NUMERIC(10, 2) NOT NULL,
    status VARCHAR(30) DEFAULT 'CONFIRMED'
);

-- 8. Agri Transport & Storage Requests
CREATE TABLE IF NOT EXISTS transport_requests (
    id VARCHAR(64) PRIMARY KEY,
    farmer_id VARCHAR(64) NOT NULL REFERENCES users(id),
    pickup_village VARCHAR(150) NOT NULL,
    destination_mandi VARCHAR(150) NOT NULL,
    crop_name VARCHAR(100) NOT NULL,
    weight_quintals NUMERIC(8, 2) NOT NULL,
    vehicle_type VARCHAR(50) NOT NULL,
    estimated_fare NUMERIC(10, 2) NOT NULL,
    status VARCHAR(30) DEFAULT 'DRIVER_ASSIGNED',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 9. AI Krishi Chatbot Messages
CREATE TABLE IF NOT EXISTS chat_messages (
    id VARCHAR(64) PRIMARY KEY,
    user_id VARCHAR(64) NOT NULL REFERENCES users(id),
    message_text TEXT NOT NULL,
    is_user BOOLEAN NOT NULL,
    language_code VARCHAR(10) DEFAULT 'en',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 10. Farm Accounting (Income & Expense Ledger)
CREATE TABLE IF NOT EXISTS farm_transactions (
    id VARCHAR(64) PRIMARY KEY,
    farmer_id VARCHAR(64) NOT NULL REFERENCES users(id),
    title VARCHAR(200) NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    is_income BOOLEAN NOT NULL,
    category VARCHAR(100) NOT NULL,
    transaction_date VARCHAR(50) NOT NULL
);
