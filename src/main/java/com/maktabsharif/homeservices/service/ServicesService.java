package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.DuplicateServiceTitleException;
import com.maktabsharif.homeservices.Exceptions.ServicesNotFoundException;
import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.repository.ServicesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicesService {

    private final ServicesRepository servicesRepository;

    public ServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }


    public Services create(Services services){
        Optional<Services> foundServices = servicesRepository.findByServiceTitle(services.getServiceTitle());
        if(foundServices.isPresent())
            throw new DuplicateServiceTitleException("Service title already exists.");

        return servicesRepository.save(services);
    }

    public Services findById(Long serviceId){
        return servicesRepository.findById(serviceId)
                .orElseThrow(() -> new ServicesNotFoundException("Service not found."));
    }

    public Services findByServiceTitle(String servicesTitle){
        return servicesRepository.findByServiceTitle(servicesTitle).
                orElseThrow(() -> new ServicesNotFoundException("Service not found."));
    }

    public Services edit(Long serviceId, String newServiceTitle){
        Optional<Services> servicesWithNewServiceTitle = servicesRepository.findByServiceTitle(newServiceTitle);
        if(servicesWithNewServiceTitle.isPresent())
            throw new DuplicateServiceTitleException("Service already exists.");

        Optional<Services> underEditionServices = servicesRepository.findById(serviceId);
        underEditionServices.get().setServiceTitle(newServiceTitle);

        return servicesRepository.save(underEditionServices.get());
    }

    public void deleteById(Long servicesId){
        servicesRepository.deleteById(servicesId);
    }

    public List<Services> findAll(){
        return servicesRepository.findAll();
    }

    public Services update(Services services){
        return servicesRepository.save(services);
    }

}
