DROP TABLE IF EXISTS user_investments;
DROP TABLE IF EXISTS investment_options;


CREATE TABLE investment_options (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(100) NOT NULL,

    category VARCHAR(50) NOT NULL,

    current_price DECIMAL(12,2) NOT NULL,

    trend VARCHAR(20) NOT NULL,

    estimated_return DECIMAL(5,2)

);


CREATE TABLE user_investments (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    investment_option_id BIGINT NOT NULL,

    quantity DECIMAL(12,2) NOT NULL,

    total_invested DECIMAL(12,2) NOT NULL,

    purchase_date DATE NOT NULL,

    CONSTRAINT fk_investment_option
        FOREIGN KEY (investment_option_id)
        REFERENCES investment_options(id)

);