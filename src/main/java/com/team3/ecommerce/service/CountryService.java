package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Country;
import com.team3.ecommerce.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public List<Country> getCountryList() {
        return (List<Country>) countryRepository.findAll();
    }
}
