package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.repository.SubservicesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubservicesService {

    private final SubservicesRepository subservicesRepository;
    private final ServicesService servicesService;
    public SubservicesService(SubservicesRepository subservicesRepository,
                              ServicesService servicesService) {
        this.subservicesRepository = subservicesRepository;
        this.servicesService = servicesService;
    }


    public Subservices create(Subservices subservices) throws Exception{

        Services foundService = servicesService.findById(subservices.getServices().getId());
        if(foundService == null)
            throw new ServicesNotFoundException("Parent service not found to add the subservice to.");

        Optional<Subservices> foundSubservice = subservicesRepository.findBySubserviceTitle(subservices.getSubserviceTitle());
        if(foundSubservice.isPresent() && foundSubservice.get().getServices().getServiceTitle().equals(foundService.getServiceTitle()))
            throw new DuplicateSubserviceTitleException(
                    foundSubservice.get().getSubserviceTitle() +
                    "already exists as a subcategory of service " +
                    foundService.getServiceTitle()
            );

        if(subservices.getBasePrice() == null)
                throw new NotDefinedServiceBasePriceException("Service base price not defined.");
            if(subservices.getDescription() == null)
                throw new NotDefinedServiceBasePriceException("Service description not defined.");
            return subservicesRepository.save(subservices);

    }

    public Subservices findBySubserviceTitle(String subserviceTitle){
        return subservicesRepository.findBySubserviceTitle(subserviceTitle).
                orElseThrow(() -> new ServicesNotFoundException("Subservice not found."));
    }

    public Subservices findBySubserviceTitleAndServicesId(String subserviceTitle, Long servicesId){
        return subservicesRepository.findBySubserviceTitleAndServices(subserviceTitle, servicesService.findById(servicesId))
                .orElseThrow(() -> new ServicesNotFoundException("Service not found."));
    }

    public Subservices findById(Long id){
        return subservicesRepository.findById(id)
                .orElseThrow(() -> new ServicesNotFoundException("Service not found."));
    }

    public List<Subservices> findAll(){
        return subservicesRepository.findAll();
    }

    public void deleteById(Long subserviceId){
        subservicesRepository.findById(subserviceId)
                .orElseThrow(() -> new ServicesNotFoundException("Service not found."));
        subservicesRepository.deleteById(subserviceId);
    }

    public Subservices edit(Long subseviceId, String newSubserviceTitle, Double newBasePrice, String newDescription){
        Optional<Subservices> underEditionSubservice = subservicesRepository.findById(subseviceId);
        if(underEditionSubservice.isEmpty())
            throw new ServicesNotFoundException("Service not found.");

        List<Subservices> subservices = subservicesRepository.findAll();
        if(newSubserviceTitle != null){
            if(subservices.stream()
                    .filter(s -> Objects.equals(s.getServices().getId(), underEditionSubservice.get().getServices().getId()) &&
                            Objects.equals( s.getSubserviceTitle(), newSubserviceTitle)).findFirst().isPresent())
                throw new DuplicateSubserviceTitleException(newDescription +
                        "already exists as a subcategory of service " +
                        underEditionSubservice.get().getServices().getServiceTitle()
                );
            else
                underEditionSubservice.get().setSubserviceTitle(newSubserviceTitle);
        }

        if(newBasePrice != null)
            underEditionSubservice.get().setBasePrice(newBasePrice);
        if(newDescription != null)
            underEditionSubservice.get().setDescription(newDescription);

        return subservicesRepository.save(underEditionSubservice.get());
    }

    public Subservices update(Subservices subservices){
        return subservicesRepository.save(subservices);
    }

}
