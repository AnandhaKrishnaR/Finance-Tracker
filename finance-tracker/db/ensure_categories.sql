-- Ensure categories exist in the database
-- Run this if categories are not being loaded

-- Check if categories table exists and has data
SELECT COUNT(*) as category_count FROM categories;

-- If no categories exist, insert them
INSERT IGNORE INTO categories (name) VALUES 
('Grocery'),
('Gas'),
('Entertainment'),
('Netflix'),
('Pharmacy'),
('Utilities'),
('Restaurant'),
('Transportation'),
('Shopping'),
('Healthcare'),
('Education'),
('Travel');

-- Verify categories were inserted
SELECT * FROM categories ORDER BY name;







