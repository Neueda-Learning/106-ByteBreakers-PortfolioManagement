package com.neueda.portfolio.repository;

import com.neueda.portfolio.dto.HoldingDetailsDTO;
import com.neueda.portfolio.dto.InvestmentDetailDTO;
import com.neueda.portfolio.entity.CurrentHolding;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class CurrentHoldingRepository {


    private final JdbcTemplate jdbcTemplate;


    public CurrentHoldingRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private final RowMapper<CurrentHolding> rowMapper = (rs, rowNum) -> {

        CurrentHolding holding = new CurrentHolding();

        holding.setId(rs.getLong("id"));
        holding.setOptionId(rs.getLong("option_id"));
        holding.setTotalQuantityOwned(
                rs.getBigDecimal("total_quantity_owned")
        );
        holding.setTotalInvested(
                rs.getBigDecimal("total_invested")
        );

        return holding;
    };


    public CurrentHolding add(CurrentHolding holding) {

        String sql = """
                INSERT INTO current_holdings
                (option_id, total_quantity_owned, total_invested)
                VALUES (?, ?, ?)
                """;


        jdbcTemplate.update(
                sql,
                holding.getOptionId(),
                holding.getTotalQuantityOwned(),
                holding.getTotalInvested()
        );

        return holding;
    }



    public Optional<CurrentHolding> findById(Long id) {

        String sql = """
                SELECT *
                FROM current_holdings
                WHERE id = ?
                """;


        return jdbcTemplate.query(
                        sql,
                        rowMapper,
                        id
                )
                .stream()
                .findFirst();
    }



    public Optional<CurrentHolding> findByOptionId(Long optionId) {

        String sql = """
                SELECT *
                FROM current_holdings
                WHERE option_id = ?
                """;


        return jdbcTemplate.query(
                        sql,
                        rowMapper,
                        optionId
                )
                .stream()
                .findFirst();
    }



    public List<CurrentHolding> findAll() {

        String sql = """
                SELECT *
                FROM current_holdings
                """;


        return jdbcTemplate.query(
                sql,
                rowMapper
        );
    }



    public void update(CurrentHolding holding) {

        String sql = """
                UPDATE current_holdings
                SET total_quantity_owned = ?,
                    total_invested = ?
                WHERE id = ?
                """;


        jdbcTemplate.update(
                sql,
                holding.getTotalQuantityOwned(),
                holding.getTotalInvested(),
                holding.getId()
        );
    }



    public void deleteById(Long id) {

        String sql = """
                DELETE FROM current_holdings
                WHERE id = ?
                """;


        jdbcTemplate.update(sql, id);
    }

    public HoldingDetailsDTO getHoldingDetails(Long id)
    {
        String sql = """
                SELECT
                    ch.total_quantity_owned AS quantityOwned,
                    ch.total_invested AS totalInvested,
                    (io.current_price * ch.total_quantity_owned - ch.total_invested)
                        AS currentProfitLoss
                FROM current_holdings ch
                JOIN investment_options io
                    ON ch.option_id = io.id
                WHERE io.id = ?
                """;

        return jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(HoldingDetailsDTO.class),
                id
        );
    }

    public InvestmentDetailDTO getInvestmentDetails(Long id)
    {
        String sql = """
            SELECT
                io.id,
                io.name,
                io.category,
                io.current_price AS currentPrice,
                io.trend,
                io.estimated_return AS estimatedReturn,
                ch.total_quantity_owned AS quantityOwned,
                ch.total_invested AS totalInvested,
                (io.current_price * ch.total_quantity_owned - ch.total_invested) AS currentProfitLoss
            FROM current_holdings ch
            JOIN investment_options io ON ch.option_id = io.id
            WHERE io.id = ?
            """;

        return jdbcTemplate.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(InvestmentDetailDTO.class),
                id
        );
    }
}
