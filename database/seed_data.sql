-- ==========================================================
-- FarmerConnect – Smart Farming Assistant Sample Seed Data
-- Run AFTER schema.sql against database: farmerconnect_db
-- NOTE: password_hash values below are placeholder markers;
--       replace with real BCrypt hashes before production use.
-- ==========================================================

INSERT INTO users (id, name, phone, password_hash, role, village, district, state)
VALUES
    ('farmer_01', 'Basavaraj Patil', '+91 98451 23456', '$2a$10$PLACEHOLDER_BCRYPT_HASH_REPLACE_ME', 'FARMER', 'Srirangapatna', 'Mandya', 'Karnataka'),
    ('farmer_02', 'Mallikarjun Swamy', '+91 94482 67890', '$2a$10$PLACEHOLDER_BCRYPT_HASH_REPLACE_ME', 'FARMER', 'Belur', 'Hassan', 'Karnataka'),
    ('buyer_01', 'Sri Lakshmi Rice Mill', '+91 94800 11223', '$2a$10$PLACEHOLDER_BCRYPT_HASH_REPLACE_ME', 'BUYER', 'Mandya City', 'Mandya', 'Karnataka')
ON CONFLICT (id) DO NOTHING;

INSERT INTO farmer_profiles (user_id, land_area_acres, primary_crops, fruits_fid, bank_account_verified)
VALUES
    ('farmer_01', 4.50, ARRAY['Paddy', 'Sugarcane', 'Ragi'], 'FID-KA-MYA-00142', TRUE),
    ('farmer_02', 6.00, ARRAY['Ragi', 'Maize'], 'FID-KA-HSN-00489', TRUE)
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO buyer_profiles (user_id, business_name, gst_or_trade_license, apmc_trader_id)
VALUES
    ('buyer_01', 'Sri Lakshmi Agro Mills', 'KA-GST-SAMPLE-001', 'APMC-MYA-TR-882')
ON CONFLICT (user_id) DO NOTHING;

INSERT INTO products (id, farmer_id, title_en, title_kn, category, price_per_unit, unit, quantity_available, quality_grade, location, is_organic, rating)
VALUES
    ('PROD-01', 'farmer_01', 'Organic Sona Masoori Paddy', 'ಸಾವಯವ ಸೋನಾ ಮಸೂರಿ ಭತ್ತ', 'CEREALS', 2450.00, 'Quintal', 45.00, 'Grade A', 'Mandya, Karnataka', TRUE, 4.80),
    ('PROD-02', 'farmer_02', 'Finger Millet / Ragi (GPU-28)', 'ಶುದ್ಧ ನಾಟಿ ರಾಗಿ (GPU-28)', 'CEREALS', 3850.00, 'Quintal', 30.00, 'Grade A', 'Hassan, Karnataka', TRUE, 4.90)
ON CONFLICT (id) DO NOTHING;

INSERT INTO mandi_prices (id, crop_name_en, crop_name_kn, market_name, district, min_price, max_price, modal_price, price_date, trend, percent_change)
VALUES
    ('MP-01', 'Paddy (Sona Masoori)', 'ಭತ್ತ (ಸೋನಾ ಮಸೂರಿ)', 'Mandya APMC', 'Mandya', 2300.00, 2650.00, 2480.00, CURRENT_DATE, 'UP', 3.40),
    ('MP-02', 'Ragi (Finger Millet)', 'ರಾಗಿ (ನಾಟಿ)', 'Hassan APMC', 'Hassan', 3600.00, 4100.00, 3900.00, CURRENT_DATE, 'STABLE', 0.00),
    ('MP-03', 'Tomato (Hybrid)', 'ಟೊಮ್ಯಾಟೊ', 'Kolar APMC', 'Kolar', 1200.00, 1650.00, 1450.00, CURRENT_DATE, 'UP', 5.20)
ON CONFLICT (id) DO NOTHING;

INSERT INTO equipment_listings (id, owner_id, name_en, name_kn, category, specifications, hourly_rate, daily_rate, location, is_available)
VALUES
    ('EQ-01', 'farmer_01', 'Mahindra 575 DI Tractor (45 HP)', 'ಮಹೀಂದ್ರಾ 575 ಡಿಐ ಟ್ರ್ಯಾಕ್ಟರ್', 'TRACTOR', '45 HP Dual Clutch + Rotavator', 450.00, 3200.00, 'Mandya (3 km away)', TRUE),
    ('EQ-02', 'farmer_02', 'Paddy Combine Harvester', 'ಭತ್ತ ಕೊಯ್ಲು ಮಾಡುವ ಯಂತ್ರ', 'HARVESTER', 'Track-mounted wet-land harvester', 1600.00, 12000.00, 'Maddur (6 km away)', TRUE)
ON CONFLICT (id) DO NOTHING;
