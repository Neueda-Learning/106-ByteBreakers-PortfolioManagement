package com.neueda.portfolio.repository;



import com.neueda.portfolio.entity.InvestmentOption;
import com.neueda.portfolio.entity.TransactionHistory;
import org.springframework.dao.EmptyResultDataAccessException;
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

    private static final RowMapper<TransactionHistory> ROW_MAPPER = (rs, rowNum) -> {
        InvestmentOption option = new InvestmentOption();
        option.setId(rs.getLong("option_id"));
        option.setName(rs.getString("name"));
        option.setCategory(rs.getString("category"));
        option.setCurrentPrice(rs.getBigDecimal("current_price"));
        option.setTrend(rs.getString("trend"));
        option.setEstimatedReturn(rs.getBigDecimal("estimated_return"));
        option.setVolatility(rs.getBigDecimal("volatility"));

        TransactionHistory ui = new TransactionHistory();
        ui.setId(rs.getLong("investment_id"));
        ui.setInvestmentOption(option);
        ui.setQuantity(rs.getBigDecimal("quantity"));
        ui.setBoughtPrice(rs.getBigDecimal("bought_price"));
        ui.setPurchaseDate(rs.getDate("purchase_date").toLocalDate());
        return ui;
    };

    public List<TransactionHistory> findAllWithOption() {
        String sql =
                "SELECT ui.id AS investment_id, ui.quantity, ui.bought_price, ui.purchase_date, " +
                "       io.id AS option_id, io.name, io.category, io.current_price, io.trend, " +
                "       io.estimated_return, io.volatility " +
                "FROM user_investments ui " +
                "JOIN investment_options io ON io.id = ui.investment_option_id";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public int createTransactionHistory(TransactionHistory transactionHistory) {
        String sql = "INSERT INTO user_investments (investment_option_id, quantity, bought_price, purchase_date) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                transactionHistory.getInvestmentOption().getId(),
                transactionHistory.getQuantity(),
                transactionHistory.getBoughtPrice(),
                transactionHistory.getPurchaseDate()
        );
    }

    public TransactionHistory getTransactionHistoryById(Long id) {
        String sql =
                "SELECT ui.id AS investment_id, ui.quantity, ui.bought_price, ui.purchase_date, " +
                "       io.id AS option_id, io.name, io.category, io.current_price, io.trend, " +
                "       io.estimated_return, io.volatility " +
                "FROM user_investments ui " +
                "JOIN investment_options io ON io.id = ui.investment_option_id " +
                "WHERE ui.id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public int updateTransactionHistory(Long id, TransactionHistory transactionHistory) {
        String sql = "UPDATE user_investments SET investment_option_id = ?, quantity = ?, bought_price = ?, purchase_date = ? WHERE id = ?";
        return jdbcTemplate.update(
                sql,
                transactionHistory.getInvestmentOption().getId(),
                transactionHistory.getQuantity(),
                transactionHistory.getBoughtPrice(),
                transactionHistory.getPurchaseDate(),
                id
        );
    }

    public int deleteTransactionHistory(Long id) {
        String sql = "DELETE FROM user_investments WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
