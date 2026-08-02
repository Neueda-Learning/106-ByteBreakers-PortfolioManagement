INSERT INTO investment_options
(name, category, current_price, trend, estimated_return)
VALUES

-- Equity
('Apple Inc.', 'EQUITY', 220.00, 'UP', 12.50),

('Microsoft Corporation', 'EQUITY', 450.00, 'UP', 15.00),

('Tesla Inc.', 'EQUITY', 280.00, 'DOWN', 18.00),


-- Bonds
('Government Treasury Bond', 'BOND', 1000.00, 'STABLE', 6.00),

('Corporate Bond Fund', 'BOND', 5000.00, 'STABLE', 7.50),


-- Commodities
('Gold ETF', 'COMMODITY', 6500.00, 'UP', 8.00),

('Silver ETF', 'COMMODITY', 850.00, 'STABLE', 6.50),


-- Real Estate
('Residential REIT Fund', 'REAL_ESTATE', 12000.00, 'UP', 10.00),


-- Mutual Funds / ETFs
('Nifty 50 Index Fund', 'FUND', 250.00, 'UP', 11.00),

('S&P 500 ETF', 'FUND', 550.00, 'UP', 9.50),


-- Crypto
('Bitcoin', 'CRYPTO', 60000.00, 'UP', 20.00),

('Ethereum', 'CRYPTO', 3500.00, 'DOWN', 15.00),


-- Cash
('Money Market Fund', 'CASH', 1000.00, 'STABLE', 4.00);



INSERT INTO user_investments
(investment_option_id, quantity, bought_price, purchase_date)
VALUES

-- Apple
(1, 10, 180.00, '2026-01-15'),

-- Microsoft
(2, 5, 400.00, '2026-02-10'),

-- Gold ETF
(6, 20, 5800.00, '2026-01-20'),

-- Residential REIT
(8, 3, 11000.00, '2026-03-05'),

-- Nifty 50 Index Fund
(9, 50, 220.00, '2026-02-25'),

-- Bitcoin
(11, 0.05, 55000.00, '2026-04-01');