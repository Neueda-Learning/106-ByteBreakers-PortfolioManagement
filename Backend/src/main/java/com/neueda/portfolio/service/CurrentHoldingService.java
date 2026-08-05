package com.neueda.portfolio.service;

import com.neueda.portfolio.dto.HoldingDetailsDTO;
import com.neueda.portfolio.entity.CurrentHolding;
import com.neueda.portfolio.repository.CurrentHoldingRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CurrentHoldingService {


    private final CurrentHoldingRepository repository;


    public CurrentHoldingService(CurrentHoldingRepository repository) {
        this.repository = repository;
    }



    public CurrentHolding create(CurrentHolding holding) {

        return repository.add(holding);
    }



    public CurrentHolding getById(Long id) {

        return repository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Current holding not found"
                        )
                );
    }



    public CurrentHolding getByOptionId(Long optionId) {

        return repository.findByOptionId(optionId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Holding not found for option"
                        )
                );
    }



    public List<CurrentHolding> getAll() {

        return repository.findAll();
    }



    public void update(CurrentHolding holding, String action) {
        if (action.equals("BUY")) {
            CurrentHolding existingHolding = repository.findByOptionId(holding.getOptionId())
                    .orElseThrow(() -> new RuntimeException("Holding not found for option"));

            existingHolding.setTotalQuantityOwned(existingHolding.getTotalQuantityOwned().add(holding.getTotalQuantityOwned()));
            existingHolding.setTotalInvested(existingHolding.getTotalInvested().add(holding.getTotalInvested()));

            repository.update(existingHolding);
        } else if (action.equals("SELL")){
            CurrentHolding existingHolding = repository.findByOptionId(holding.getOptionId())
                    .orElseThrow(() -> new RuntimeException("Holding not found for option"));

            if(existingHolding.getTotalQuantityOwned().compareTo(holding.getTotalQuantityOwned()) < 0) {
                throw new RuntimeException("Cannot sell more than owned");
            }

            existingHolding.setTotalQuantityOwned(existingHolding.getTotalQuantityOwned().subtract(holding.getTotalQuantityOwned()));
            existingHolding.setTotalInvested(existingHolding.getTotalInvested().subtract(holding.getTotalInvested()));

            repository.update(existingHolding);
        }
    }



    public void delete(Long id) {

        repository.deleteById(id);
    }

    public HoldingDetailsDTO getHoldingDetails(Long id){
        return repository.getHoldingDetails(id);
    }
}
