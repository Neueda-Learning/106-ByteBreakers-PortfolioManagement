-- =========================================================
-- INVESTMENT OPTIONS
-- =========================================================

INSERT INTO investment_options
(name, category, current_price, trend, estimated_return, volatility)
VALUES

-- EQUITY
('Reliance Industries', 'EQUITY', 1425.50, 'UP', 12.50, 18.20),
('Tata Consultancy Services', 'EQUITY', 3120.75, 'STABLE', 10.80, 14.50),
('HDFC Bank', 'EQUITY', 1985.40, 'UP', 11.50, 15.20),
('Infosys', 'EQUITY', 1480.25, 'DOWN', 9.80, 17.40),

-- BONDS
('Government Bond 2030', 'BOND', 1050.00, 'STABLE', 7.20, 3.50),
('Corporate Bond Fund', 'BOND', 1250.50, 'UP', 8.10, 5.20),

-- COMMODITIES
('Gold ETF', 'COMMODITY', 72.40, 'UP', 9.50, 12.30),
('Silver ETF', 'COMMODITY', 91.25, 'STABLE', 8.70, 16.80),

-- REAL ESTATE
('Real Estate Investment Trust', 'REAL_ESTATE', 385.60, 'UP', 10.20, 11.50),
('Commercial Property REIT', 'REAL_ESTATE', 445.25, 'STABLE', 9.40, 10.80),

-- FUNDS
('Nifty 50 Index Fund', 'FUND', 245.75, 'UP', 11.80, 13.20),
('Flexi Cap Mutual Fund', 'FUND', 185.40, 'UP', 12.40, 15.60),
('Balanced Advantage Fund', 'FUND', 165.80, 'STABLE', 9.20, 9.80),

-- CRYPTO
('Bitcoin', 'CRYPTO', 5800000.00, 'UP', 18.50, 48.50),
('Ethereum', 'CRYPTO', 315000.00, 'DOWN', 16.20, 44.30),

-- CASH
('Liquid Fund', 'CASH', 100.00, 'STABLE', 6.50, 1.20),
('Money Market Fund', 'CASH', 105.50, 'STABLE', 6.80, 1.50);


-- =========================================================
-- TRANSACTION HISTORY
-- =========================================================

INSERT INTO transaction_history
(investment_option_id, action, quantity, transaction_price, transaction_date)
VALUES

-- Reliance Industries
(1, 'BUY', 10.00, 1200.00, '2026-01-15'),
(1, 'BUY', 5.00, 1300.00, '2026-03-10'),
(1, 'SELL', 3.00, 1400.00, '2026-06-20'),

-- TCS
(2, 'BUY', 4.00, 2900.00, '2026-02-05'),

-- HDFC Bank
(3, 'BUY', 8.00, 1750.00, '2026-03-18'),
(3, 'SELL', 2.00, 1900.00, '2026-07-02'),

-- Gold ETF
(7, 'BUY', 50.00, 62.00, '2026-01-28'),
(7, 'BUY', 20.00, 68.00, '2026-05-12'),

-- REIT
(9, 'BUY', 20.00, 350.00, '2026-04-08'),

-- Nifty 50 Index Fund
(11, 'BUY', 40.00, 210.00, '2026-02-22'),
(11, 'BUY', 20.00, 225.00, '2026-05-25'),
(11, 'SELL', 10.00, 240.00, '2026-07-15'),

-- Flexi Cap Fund
(12, 'BUY', 30.00, 165.00, '2026-06-01'),

-- Bitcoin
(14, 'BUY', 0.01, 5200000.00, '2026-04-15'),
(14, 'SELL', 0.002, 5700000.00, '2026-07-20'),

-- Liquid Fund
(16, 'BUY', 100.00, 100.00, '2026-07-25');


-- =========================================================
-- CURRENT HOLDINGS
-- =========================================================

INSERT INTO current_holdings
(investment_option_id, total_quantity_owned, total_invested)
VALUES

-- Reliance
-- Bought: 10 @ 1200 + 5 @ 1300
-- Average cost = 1233.33
-- Sold 3, leaving 12 units
(1, 12.00, 14800.00),

-- TCS
(2, 4.00, 11600.00),

-- HDFC Bank
-- Bought 8 @ 1750, sold 2 using average cost
(3, 6.00, 10500.00),

-- Gold ETF
-- 50 @ 62 + 20 @ 68
(7, 70.00, 4460.00),

-- REIT
(9, 20.00, 7000.00),

-- Nifty 50 Index Fund
-- 40 @ 210 + 20 @ 225 = 12900
-- Average = 215
-- Sold 10 -> remaining cost = 10750
(11, 50.00, 10750.00),

-- Flexi Cap Fund
(12, 30.00, 4950.00),

-- Bitcoin
-- 0.01 bought, 0.002 sold
-- Remaining cost basis = 0.008 * 5,200,000
(14, 0.008, 41600.00),

-- Liquid Fund
(16, 100.00, 10000.00);