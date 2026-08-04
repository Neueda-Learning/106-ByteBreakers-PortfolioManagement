package com.neueda.portfolio.summary;


import com.neueda.portfolio.entity.UserInvestment;
import com.neueda.portfolio.repository.UserInvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.neueda.portfolio.summary.SummaryDTO;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Summaryservice {
    @Autowired
    private UserInvestmentRepository userInvestment;
    private SummaryDTO summaryDTO;

    @Value("${diversify.recent-investments-count:5}")
    private int recentInvestmentsCount;


    public SummaryDTO getSummary() {
        List<UserInvestment> holding=userInvestment.findAllWithOption();
        double total_investment=0;
        double current_value=0;
        Map<String, Double> valueByCategory = new HashMap<>();
        for (UserInvestment ui : holding) {
            double qty = ui.getQuantity().doubleValue();
            double bought = ui.getBoughtPrice().doubleValue();
            double current = ui.getInvestmentOption().getCurrentPrice().doubleValue();
            String category = ui.getInvestmentOption().getCategory();
            total_investment += qty * bought;
            double lotCurrentValue = qty * current;
            current_value += lotCurrentValue;
            valueByCategory.merge(category, lotCurrentValue, Double::sum);

        }
        double totalProfitLoss = current_value - total_investment;
        double totalProfitLossPct = total_investment == 0 ? 0.0 : totalProfitLoss / total_investment;
        List<RecentInvestmentDTO> recent = holding.stream()
                .sorted(Comparator.comparing(UserInvestment::getPurchaseDate).reversed())
                .limit(recentInvestmentsCount)
                .map(ui -> new RecentInvestmentDTO(
                        ui.getInvestmentOption().getName(),
                        ui.getInvestmentOption().getCategory(),
                        ui.getQuantity(),
                        ui.getBoughtPrice(),
                        ui.getPurchaseDate()
                ))
                .collect(Collectors.toList());
        return new SummaryDTO(
                round2(total_investment),
                round2(current_value),
                round2(totalProfitLoss),
                round4(totalProfitLossPct),
                valueByCategory,
                recent
        );

    }
    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private double round4(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }



}