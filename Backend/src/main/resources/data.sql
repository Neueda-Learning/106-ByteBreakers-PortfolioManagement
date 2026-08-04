-- Investment options
INSERT INTO investment_options
(id, name, category, current_price, trend, estimated_return, volatility)
VALUES
(1, 'Apple Inc.', 'Stocks', 225.50, 'up', 0.12, 0.25),
(2, 'Tesla Inc.', 'Stocks', 245.30, 'strong_up', 0.18, 0.55),
(3, 'Microsoft Corp.', 'Stocks', 430.20, 'up', 0.10, 0.20),
(4, 'Bitcoin', 'Crypto', 68000.00, 'strong_up', 0.25, 0.75),
(5, 'Ethereum', 'Crypto', 3500.00, 'up', 0.20, 0.65),
(6, 'Gold ETF', 'Commodities', 185.40, 'flat', 0.06, 0.15),
(7, 'Government Bonds ETF', 'Bonds', 102.30, 'flat', 0.04, 0.05),
(8, 'Nvidia Corp.', 'Stocks', 120.80, 'strong_up', 0.22, 0.45),
(9, 'Oil ETF', 'Commodities', 78.60, 'down', 0.03, 0.40),
(10, 'Real Estate ETF', 'Real Estate', 95.70, 'up', 0.08, 0.30);


-- User investments
INSERT INTO user_investments
(id, investment_option_id, quantity, bought_price, purchase_date)
VALUES
(1, 1, 10.00, 180.00, '2025-01-15'),
(2, 2, 5.00, 210.00, '2025-02-10'),
(3, 4, 0.25, 45000.00, '2024-11-20'),
(4, 6, 20.00, 170.00, '2025-03-05'),
(5, 7, 50.00, 100.00, '2025-01-30');