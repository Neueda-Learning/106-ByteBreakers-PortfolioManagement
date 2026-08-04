package com.neueda.portfolio.repository;



import com.neueda.portfolio.entity.InvestmentOption;
import com.neueda.portfolio.entity.UserInvestment;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public int createUserInvestment(UserInvestment userInvestment) {
        String sql = "INSERT INTO user_investments (investment_option_id, quantity, bought_price, purchase_date) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                userInvestment.getInvestmentOption().getId(),
                userInvestment.getQuantity(),
                userInvestment.getBoughtPrice(),
                userInvestment.getPurchaseDate()
        );
    }

    public UserInvestment getUserInvestmentById(Long id) {
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

    public int updateUserInvestment(Long id, UserInvestment userInvestment) {
        String sql = "UPDATE user_investments SET investment_option_id = ?, quantity = ?, bought_price = ?, purchase_date = ? WHERE id = ?";
        return jdbcTemplate.update(
                sql,
                userInvestment.getInvestmentOption().getId(),
                userInvestment.getQuantity(),
                userInvestment.getBoughtPrice(),
                userInvestment.getPurchaseDate(),
                id
        );
    }

    public int deleteUserInvestment(Long id) {
        String sql = "DELETE FROM user_investments WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
