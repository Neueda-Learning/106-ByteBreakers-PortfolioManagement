package com.neueda.portfolio.diversify.repository;

import com.neueda.portfolio.diversify.model.Option;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OptionRepository {

    private final JdbcTemplate jdbcTemplate;

    public OptionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Option> findAll() {
        String sql = "SELECT id, name, category, current_price, trend, estimated_return, volatility " +
                     "FROM investment_options";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Option.class));
    }
}
