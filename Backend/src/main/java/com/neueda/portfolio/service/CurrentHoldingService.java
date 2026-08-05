package com.neueda.portfolio.service;

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



    public void update(CurrentHolding holding) {

        repository.update(holding);
    }



    public void delete(Long id) {

        repository.deleteById(id);
    }
}
