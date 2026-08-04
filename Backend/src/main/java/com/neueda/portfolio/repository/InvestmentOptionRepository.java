package com.neueda.portfolio.repository;
import com.neueda.portfolio.entity.InvestmentOption;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class InvestmentOptionRepository {

    private final JdbcTemplate jdbcTemplate;

    public InvestmentOptionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<InvestmentOption> ROW_MAPPER = (rs, rowNum) -> {
        InvestmentOption o = new InvestmentOption();
        o.setId(rs.getLong("id"));
        o.setName(rs.getString("name"));
        o.setCategory(rs.getString("category"));
        o.setCurrentPrice(rs.getBigDecimal("current_price"));
        o.setTrend(rs.getString("trend"));
        o.setEstimatedReturn(rs.getBigDecimal("estimated_return"));
        o.setVolatility(rs.getBigDecimal("volatility")); // null is fine, service defaults it
        return o;
    };

    public List<InvestmentOption> findAll() {
        String sql = "SELECT id, name, category, current_price, trend, estimated_return, volatility " +
                     "FROM investment_options";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }
}
