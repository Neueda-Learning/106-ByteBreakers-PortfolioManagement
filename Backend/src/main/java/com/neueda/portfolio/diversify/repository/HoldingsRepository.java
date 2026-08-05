package com.neueda.portfolio.diversify.repository;

import com.neueda.portfolio.diversify.model.Holdin;
import com.neueda.portfolio.diversify.model.Option;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * JDBC access to current_holdings. This is the table DiversificationService
 * and SummaryService read from -- it's already aggregated (one row per
 * stock), so neither service needs to sum lots in Java anymore.
 *
 * Kept in sync exclusively by TransactionService on every buy/sell.
 * Nothing else should write to this table.
 */
@Repository
public class HoldingsRepository {

    private final JdbcTemplate jdbcTemplate;

    public HoldingsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<Holdin> ROW_MAPPER = (rs, rowNum) -> {
        Option option = new Option();
        option.setId(rs.getLong("option_id"));
        option.setName(rs.getString("name"));
        option.setCategory(rs.getString("category"));
        option.setCurrentPrice(rs.getBigDecimal("current_price"));
        option.setTrend(rs.getString("trend"));
        option.setEstimatedReturn(rs.getBigDecimal("estimated_return"));
        option.setVolatility(rs.getBigDecimal("volatility"));

        Holdin holding = new Holdin();
        holding.setId(rs.getLong("holding_id"));
        holding.setInvestmentOption(option);
        holding.setTotalQuantityOwned(rs.getBigDecimal("total_quantity_owned"));
        holding.setTotalInvested(rs.getBigDecimal("total_invested"));
        return holding;
    };

    /** Every stock the user currently owns, joined with its live option data. */
    public List<Holdin> findAllWithOption() {
        String sql =
            "SELECT ch.id AS holding_id, ch.total_quantity_owned, ch.total_invested, " +
            "       io.id AS option_id, io.name, io.category, io.current_price, io.trend, " +
            "       io.estimated_return, io.volatility " +
            "FROM current_holdings ch " +
            "JOIN investment_options io ON io.id = ch.option_id";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    /** Single stock's holding row, if the user owns any. Used before a SELL to validate/compute profit. */
    public Optional<Holdin> findByOptionId(Long optionId) {
        String sql =
            "SELECT ch.id AS holding_id, ch.total_quantity_owned, ch.total_invested, " +
            "       io.id AS option_id, io.name, io.category, io.current_price, io.trend, " +
            "       io.estimated_return, io.volatility " +
            "FROM current_holdings ch " +
            "JOIN investment_options io ON io.id = ch.option_id " +
            "WHERE ch.option_id = ?";
        List<Holdin> results = jdbcTemplate.query(sql, ROW_MAPPER, optionId);
        return results.stream().findFirst();
    }

    /** BUY: creates the row if this is the first purchase of this stock, otherwise adds to it. */
    public void addToHolding(Long optionId, BigDecimal quantity, BigDecimal investedAmount) {
        String sql =
            "INSERT INTO current_holdings (option_id, total_quantity_owned, total_invested) " +
            "VALUES (?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE " +
            "  total_quantity_owned = total_quantity_owned + VALUES(total_quantity_owned), " +
            "  total_invested = total_invested + VALUES(total_invested)";
        jdbcTemplate.update(sql, optionId, quantity, investedAmount);
    }

    /** SELL: reduces both quantity and invested amount. Assumes the row already exists (caller validates). */
    public void subtractFromHolding(Long optionId, BigDecimal quantity, BigDecimal investedAmountRemoved) {
        String sql =
            "UPDATE current_holdings " +
            "SET total_quantity_owned = total_quantity_owned - ?, " +
            "    total_invested = total_invested - ? " +
            "WHERE option_id = ?";
        jdbcTemplate.update(sql, quantity, investedAmountRemoved, optionId);
    }
}
