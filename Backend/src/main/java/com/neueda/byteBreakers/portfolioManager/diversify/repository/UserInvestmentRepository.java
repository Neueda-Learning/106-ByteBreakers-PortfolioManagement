package com.example.diversify.repository;

import com.example.diversify.model.InvestmentOption;
import com.example.diversify.model.UserInvestment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UserInvestmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserInvestmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final RowMapper<UserInvestment> ROW_MAPPER = (rs, rowNum) -> {
        InvestmentOption option = new InvestmentOption();
        option.setId(rs.getLong("option_id"));
        option.setName(rs.getString("name"));
        option.setCategory(rs.getString("category"));
        option.setCurrentPrice(rs.getBigDecimal("current_price"));
        option.setTrend(rs.getString("trend"));
        option.setEstimatedReturn(rs.getBigDecimal("estimated_return"));
        option.setVolatility(rs.getBigDecimal("volatility"));

        UserInvestment ui = new UserInvestment();
        ui.setId(rs.getLong("investment_id"));
        ui.setInvestmentOption(option);
        ui.setQuantity(rs.getBigDecimal("quantity"));
        ui.setBoughtPrice(rs.getBigDecimal("bought_price"));
        ui.setPurchaseDate(rs.getDate("purchase_date").toLocalDate());
        return ui;
    };

    public List<UserInvestment> findAllWithOption() {
        String sql =
                "SELECT ui.id AS investment_id, ui.quantity, ui.bought_price, ui.purchase_date, " +
                "       io.id AS option_id, io.name, io.category, io.current_price, io.trend, " +
                "       io.estimated_return, io.volatility " +
                "FROM user_investments ui " +
                "JOIN investment_options io ON io.id = ui.investment_option_id";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }
}
