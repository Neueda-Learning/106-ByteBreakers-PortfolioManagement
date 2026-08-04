package com.neueda.byteBreakers.portfolioManager.repository;

import com.neueda.byteBreakers.portfolioManager.entity.UserInvestment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserInvestmentRepository
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Read all user investments
    public List<UserInvestment> getAllUserInvestments() {
        String sql = "SELECT * FROM user_investments";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserInvestment.class));
    }

    // Create a new user investment
    public int createUserInvestment(UserInvestment userInvestment) {
        String sql = "INSERT INTO user_investments (investment_option_id, quantity, total_invested, purchase_date) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                userInvestment.getInvestmentOptionId(),
                userInvestment.getQuantity(),
                userInvestment.getTotalInvested(),
                userInvestment.getPurchaseDate()
        );
    }

    // Read one user investment by id
    public UserInvestment getUserInvestmentById(Long id) {
        String sql = "SELECT * FROM user_investments WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(UserInvestment.class),
                    id
            );
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    // Update a user investment by id
    public int updateUserInvestment(Long id, UserInvestment userInvestment) {
        String sql = "UPDATE user_investments SET investment_option_id = ?, quantity = ?, total_invested = ?, purchase_date = ? WHERE id = ?";
        return jdbcTemplate.update(
                sql,
                userInvestment.getInvestmentOptionId(),
                userInvestment.getQuantity(),
                userInvestment.getTotalInvested(),
                userInvestment.getPurchaseDate(),
                id
        );
    }

    // Delete a user investment by id
    public int deleteUserInvestment(Long id) {
        String sql = "DELETE FROM user_investments WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
