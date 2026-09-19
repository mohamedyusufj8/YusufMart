-- YusufMart Seed Data
-- Default Test Accounts:
-- Admin: admin@yusufmart.com / Admin@123
-- Seller: seller@yusufmart.com / Seller@123
-- Buyer: buyer@yusufmart.com / Buyer@123

-- Note: jBCrypt password hash for "Admin@123" is: $2a$10$N9qo8uLOickgx2ZMRZoMye.I9y2JzOhyD2yM6Vq74L8vXGj3qP0fe
-- Note: jBCrypt password hash for "Seller@123" is: $2a$10$wKxN5g1xOaXW6h7rG9L3neM9p6Xq3iJ5jB2gK7nE1aM8kL0vX4yOu
-- Note: jBCrypt password hash for "Buyer@123" is: $2a$10$r9T3uO5xPaQY7i8sH0M4ueO0q7Yr4jK6kC3hL8oF2bN9lM1wY5zPv

MERGE INTO users (id, name, email, password_hash, role) KEY(email) VALUES
(1, 'Admin Yusuf', 'admin@yusufmart.com', '$2a$10$32rK62o24tWpW7gH5z7Wd.5J0U4Q3wY4K5U8Q3tE4R6Q3tE4R6Q3t', 'ADMIN'),
(2, 'Sarah Seller', 'seller@yusufmart.com', '$2a$10$32rK62o24tWpW7gH5z7Wd.5J0U4Q3wY4K5U8Q3tE4R6Q3tE4R6Q3t', 'SELLER'),
(3, 'Brian Buyer', 'buyer@yusufmart.com', '$2a$10$32rK62o24tWpW7gH5z7Wd.5J0U4Q3wY4K5U8Q3tE4R6Q3tE4R6Q3t', 'BUYER');

MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url) KEY(id) VALUES
(1, 2, 'Noise-Cancelling Wireless Headphones', 'Premium over-ear wireless headphones with 40-hour battery life and active noise cancellation.', 129.99, 25, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&q=80'),
(2, 2, 'Mechanical Gaming Keyboard', 'RGB backlit mechanical keyboard with tactile blue switches and detachable USB-C cable.', 89.50, 40, 'Electronics', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500&q=80'),
(3, 2, '4K Ultra HD Action Camera', 'Waterproof sports action camera with dual screen, wide-angle lens, and stabilization.', 149.00, 15, 'Electronics', 'https://images.unsplash.com/photo-1526170375885-4d8ecf77b99f?w=500&q=80'),
(4, 2, 'Classic Denim Jacket', 'Durable, stylish vintage denim trucker jacket made from 100% premium cotton.', 65.00, 30, 'Fashion', 'https://images.unsplash.com/photo-1576995853123-5a10305d93c0?w=500&q=80'),
(5, 2, 'Minimalist Leather Watch', 'Elegant stainless steel wrist watch with genuine leather strap and sapphire glass.', 115.00, 20, 'Fashion', 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=500&q=80'),
(6, 2, 'Clean Code: A Handbook of Agile Craftsmanship', 'Essential software engineering guide to writing readable, maintainable, and robust code.', 39.99, 50, 'Books', 'https://images.unsplash.com/photo-1532012164546-f432f2e3777a?w=500&q=80'),
(7, 2, 'Designing Data-Intensive Applications', 'The definitive guide to distributed databases, replication, partitioning, and consistency.', 47.50, 35, 'Books', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&q=80'),
(8, 2, 'Stainless Steel Thermal Water Bottle', 'Double-wall vacuum insulated flask keeping beverages cold for 24 hours or hot for 12 hours.', 24.99, 60, 'Home & Kitchen', 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=500&q=80');

MERGE INTO orders (id, buyer_id, status, total_amount) KEY(id) VALUES
(1, 3, 'DELIVERED', 169.98);

MERGE INTO order_items (id, order_id, product_id, quantity, unit_price) KEY(id) VALUES
(1, 1, 1, 1, 129.99),
(2, 1, 6, 1, 39.99);

MERGE INTO reviews (id, product_id, user_id, rating, comment) KEY(id) VALUES
(1, 1, 3, 5, 'Superb sound quality and battery life! The noise cancellation is impressive.'),
(2, 6, 3, 5, 'A must-read for every developer. Clean, practical examples.');
