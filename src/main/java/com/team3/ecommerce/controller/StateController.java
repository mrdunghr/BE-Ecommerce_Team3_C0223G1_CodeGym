package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.State;
import com.team3.ecommerce.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/states")
@CrossOrigin("*")
public class StateController {
    @Autowired
    private StateService stateService;

    @GetMapping("")
    public ResponseEntity<List<State>> getCountryList() {
        List<State> stateList = stateService.getStates();
        return new ResponseEntity<>(stateList, HttpStatus.OK);
    }
}
