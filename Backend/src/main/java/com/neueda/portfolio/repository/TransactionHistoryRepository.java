package com.neueda.portfolio.repository;



import com.neueda.portfolio.entity.InvestmentOption;
import com.neueda.portfolio.entity.TransactionHistory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class TransactionHistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionHistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<TransactionHistory> findAll() {
        String sql = """
            SELECT
                id,
                investment_option_id AS investmentOptionId,
                action,
                quantity,
                bought_price AS boughtPrice,
                transaction_date AS purchaseDate
            FROM transaction_history
            """;

        return jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper<>(TransactionHistory.class)
        );
    }

    public int createTransactionHistory(TransactionHistory transactionHistory) {
        String sql = "INSERT INTO transaction_history (investment_option_id, action, quantity, bought_price, transaction_date) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                transactionHistory.getInvestmentOptionId(),
                transactionHistory.getAction(),
                transactionHistory.getQuantity(),
                transactionHistory.getBoughtPrice(),
                transactionHistory.getPurchaseDate()
        );
    }

    public TransactionHistory getTransactionHistoryById(Long id) {

        String sql = """
            SELECT
                id,
                investment_option_id AS investmentOptionId,
                action,
                quantity,
                bought_price AS boughtPrice,
                transaction_date AS purchaseDate
            FROM transaction_history
            WHERE id = ?
            """;

        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(TransactionHistory.class),
                    id
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public int updateTransactionHistory(Long id, TransactionHistory transactionHistory) {
        String sql = """
            UPDATE transaction_history
            SET investment_option_id = ?,
                action = ?,
                quantity = ?,
                bought_price = ?,
                transaction_date = ?
            WHERE id = ?
            """;

        return jdbcTemplate.update(
                sql,
                transactionHistory.getInvestmentOptionId(),
                transactionHistory.getAction(),
                transactionHistory.getQuantity(),
                transactionHistory.getBoughtPrice(),
                transactionHistory.getPurchaseDate(),
                id
        );
    }

    public int deleteTransactionHistory(Long id) {
        String sql = """
            DELETE FROM transaction_history
            WHERE id = ?
            """;

        return jdbcTemplate.update(sql, id);
    }
}
