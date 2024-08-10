package com.greenhouse.gh_backend.services;

import com.greenhouse.gh_backend.entities.Emission;
import com.greenhouse.gh_backend.entities.EmissionScope;
import com.greenhouse.gh_backend.entities.Location;
import com.greenhouse.gh_backend.repositories.EmissionRepository;
import com.greenhouse.gh_backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FootprintService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EmissionRepository emissionRepository;

}
