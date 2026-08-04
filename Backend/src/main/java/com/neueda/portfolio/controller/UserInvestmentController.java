package com.neueda.portfolio.controller;

import com.neueda.portfolio.entity.UserInvestment;
import com.neueda.portfolio.service.UserInvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userInvestments")
@CrossOrigin("http://localhost:5173")
public class UserInvestmentController
{
    @Autowired
    private UserInvestmentService userInvestmentService;

    @GetMapping("/")
    public ResponseEntity<List<UserInvestment>> getAllUserInvestments() {
        return ResponseEntity.ok(userInvestmentService.getAllUserInvestments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserInvestment> getUserInvestmentById(@PathVariable Long id) {
        UserInvestment userInvestment = userInvestmentService.getUserInvestmentById(id);

        if (userInvestment == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userInvestment);
    }

    @PostMapping("/")
    public ResponseEntity<Void> createUserInvestment(@RequestBody UserInvestment userInvestment) {
        int rowsAffected = userInvestmentService.createUserInvestment(userInvestment);

        if (rowsAffected > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserInvestment(@PathVariable Long id, @RequestBody UserInvestment userInvestment) {
        UserInvestment existingUserInvestment = userInvestmentService.getUserInvestmentById(id);

        if (existingUserInvestment == null) {
            return ResponseEntity.notFound().build();
        }

        int rowsAffected = userInvestmentService.updateUserInvestment(id, userInvestment);

        if (rowsAffected > 0) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserInvestment(@PathVariable Long id) {
        UserInvestment existingUserInvestment = userInvestmentService.getUserInvestmentById(id);

        if (existingUserInvestment == null) {
            return ResponseEntity.notFound().build();
        }

        int rowsAffected = userInvestmentService.deleteUserInvestment(id);

        if (rowsAffected > 0) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
