package com.neueda.portfolio.diversify.repository;

import com.neueda.portfolio.diversify.model.Option;
import com.neueda.portfolio.diversify.model.History;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class HistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public HistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<History> ROW_MAPPER = (rs, rowNum) -> {

        Option option = new Option();
        option.setId(rs.getLong("option_id"));
        option.setName(rs.getString("name"));
        option.setCategory(rs.getString("category"));
        option.setCurrentPrice(rs.getBigDecimal("current_price"));

        History txn = new History();
        txn.setId(rs.getLong("txn_id"));
        txn.setInvestmentOption(option);
        txn.setAction(rs.getString("action"));
        txn.setQuantity(rs.getBigDecimal("quantity"));
        txn.setBoughtPrice(rs.getBigDecimal("bought_price"));
        txn.setPurchaseDate(rs.getDate("transaction_date").toLocalDate());

        return txn;
    };


    public void insertTransaction(Long optionId,
                                  String action,
                                  java.math.BigDecimal quantity,
                                  java.math.BigDecimal price,
                                  LocalDate date) {

        String sql =
                "INSERT INTO transaction_history " +
                        "(investment_option_id, action, quantity, bought_price, transaction_date) " +
                        "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, optionId, action, quantity, price, date);
    }


    /**
     * Most recent N transactions across all stocks,
     * newest first -- for dashboard recent activity.
     */
    public List<History> findRecent(int limit) {

        String sql =
                "SELECT th.id AS txn_id, " +
                        "       th.action, " +
                        "       th.quantity, " +
                        "       th.bought_price, " +
                        "       th.transaction_date, " +
                        "       io.id AS option_id, " +
                        "       io.name, " +
                        "       io.category, " +
                        "       io.current_price " +
                        "FROM transaction_history th " +
                        "JOIN investment_options io " +
                        "ON io.id = th.investment_option_id " +
                        "ORDER BY th.transaction_date DESC, th.id DESC " +
                        "LIMIT ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, limit);
    }
}