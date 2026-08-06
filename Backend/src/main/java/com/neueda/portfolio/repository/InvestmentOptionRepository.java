package com.neueda.portfolio.repository;
import com.neueda.portfolio.dto.MyInvestmentsDTO;
import com.neueda.portfolio.entity.InvestmentOption;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public InvestmentOption findById(Long id) {
        String sql = "SELECT id, name, category, current_price, trend, estimated_return, volatility FROM investment_options WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public List<MyInvestmentsDTO> findAllMyInvestments() {
        String sql = "SELECT io.name, io.category, io.current_price, ch.total_quantity_owned, io.trend " +
                     "FROM investment_options io " +
                     "JOIN current_holdings ch ON io.id = ch.option_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new MyInvestmentsDTO(
                rs.getString("name"),
                rs.getString("category"),
                rs.getBigDecimal("current_price"),
                rs.getBigDecimal("total_quantity_owned"),
                rs.getString("trend")
        ));
    }

    public int createInvestmentOption(InvestmentOption investmentOption) {
        String sql = "INSERT INTO investment_options (name, category, current_price, trend, estimated_return, volatility) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                investmentOption.getName(),
                investmentOption.getCategory(),
                investmentOption.getCurrentPrice(),
                investmentOption.getTrend(),
                investmentOption.getEstimatedReturn(),
                investmentOption.getVolatility()
        );
    }

    public int updateInvestmentOption(Long id, InvestmentOption investmentOption) {
        String sql = "UPDATE investment_options SET name = ?, category = ?, current_price = ?, trend = ?, estimated_return = ?, volatility = ? WHERE id = ?";
        return jdbcTemplate.update(
                sql,
                investmentOption.getName(),
                investmentOption.getCategory(),
                investmentOption.getCurrentPrice(),
                investmentOption.getTrend(),
                investmentOption.getEstimatedReturn(),
                investmentOption.getVolatility(),
                id
        );
    }

    public int deleteInvestmentOption(Long id) {
        String sql = "DELETE FROM investment_options WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
