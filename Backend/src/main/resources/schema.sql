DROP TABLE IF EXISTS current_holdings;
DROP TABLE IF EXISTS transaction_history;
DROP TABLE IF EXISTS investment_options;

CREATE TABLE investment_options (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    category VARCHAR(50) NOT NULL,

    current_price DECIMAL(12,2) NOT NULL,

    trend VARCHAR(20) NOT NULL,

    estimated_return DECIMAL(5,2),

    volatility DECIMAL(5,2)

);


CREATE TABLE transaction_history (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    investment_option_id BIGINT NOT NULL,

    action VARCHAR(10) NOT NULL,

    quantity DECIMAL(12,2) NOT NULL,

    bought_price DECIMAL(12,2) NOT NULL,

    purchase_date DATE NOT NULL,

    CONSTRAINT fk_transaction_investment_option
        FOREIGN KEY (investment_option_id)
        REFERENCES investment_options(id)

);


CREATE TABLE current_holdings (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    option_id BIGINT NOT NULL,

    total_quantity_owned DECIMAL(18,8) NOT NULL,

    total_invested DECIMAL(18,8) NOT NULL,

    CONSTRAINT fk_holding_investment_option
        FOREIGN KEY (option_id)
        REFERENCES investment_options(id),

    CONSTRAINT uq_current_holding_option
        UNIQUE (option_id)

);