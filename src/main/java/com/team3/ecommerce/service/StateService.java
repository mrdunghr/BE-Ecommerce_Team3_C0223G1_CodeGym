package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.State;
import com.team3.ecommerce.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateService {
    @Autowired
    private StateRepository stateRepository;

    public List<State> getStates() {
        return stateRepository.findAll();
    }
}
