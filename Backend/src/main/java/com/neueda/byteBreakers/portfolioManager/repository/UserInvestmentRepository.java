package com.neueda.byteBreakers.portfolioManager.repository;

import com.neueda.byteBreakers.portfolioManager.entity.UserInvestment;
import org.springframework.beans.factory.annotation.Autowired;
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
}
