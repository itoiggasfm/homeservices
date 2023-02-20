package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.Exceptions.*;
import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.ExpertStatus;
import com.maktabsharif.homeservices.domain.enumeration.UserRole;
import com.maktabsharif.homeservices.repository.ServicesRepository;
import org.hibernate.type.SerializationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ServicesService {

    private final ServicesRepository servicesRepository;
    private final UserService userService;
    public ServicesService(ServicesRepository servicesRepository, UserService userService) {
        this.servicesRepository = servicesRepository;
        this.userService = userService;
    }


    public Services create(Services services) throws Exception{

        if(services.getServiceTitle() == null)
            throw  new NotDefinedServiceTitleException("Service title not defined.");
        if(services.getSubserviceTitle() == null){
            Optional<Services> foundService = servicesRepository.findByServiceTitle(services.getServiceTitle());
            if(foundService.isPresent())
                throw new DuplicateServiceTitleException(services.getServiceTitle() + " already exists.");
            services.setSubserviceTitle(null);
            services.setBasePrice(null);
            services.setDescription(null);
            return servicesRepository.save(services);
        }
        else{
            Optional<Services> foundSubservice = servicesRepository.findByServiceTitleAndAndSubserviceTitle(services.getServiceTitle(), services.getSubserviceTitle());
            if(foundSubservice.isPresent())
                throw new DuplicateSubserviceTitleException(services.getSubserviceTitle() +
                        " already exists as a subcategory of service " +
                        foundSubservice.get().getServiceTitle()
                );
            if(services.getBasePrice() == null)
                throw new NotDefinedServiceBasePriceException("Service base price not defined.");
            if(services.getDescription() == null)
                throw new NotDefinedServiceBasePriceException("Service description not defined.");
            return servicesRepository.save(services);
        }
    }

    public Services findByServiceTitle(String serviceTitle){
        return servicesRepository.findByServiceTitle(serviceTitle).
                orElseThrow(() -> new ServicesNotFoundException("Service not found."));
    }

    public Services findBySubserviceTitle(String subserviceTitle){
        return servicesRepository.findBySubserviceTitle(subserviceTitle).
                orElseThrow(() -> new ServicesNotFoundException("Subservice not found."));
    }

    public Services findByServiceTitleAndSubserviceTitle(String serviceTitle, String subserviceTitle){
        return servicesRepository.findByServiceTitleAndAndSubserviceTitle(serviceTitle, subserviceTitle).
                orElseThrow(() -> new ServicesNotFoundException("Service not found."));
    }

    public Services findById(Long id){
        return servicesRepository.findById(id)
                .orElseThrow(() -> new ServicesNotFoundException("Service not found."));
    }

    public List<Services> findAll(){
        return servicesRepository.findAll();
    }

    public void deleteById(Long serviceId){
        servicesRepository.findById(serviceId)
                .orElseThrow(() -> new ServicesNotFoundException("Service not found."));
        servicesRepository.deleteById(serviceId);
    }

    public Services editServices(Services underEditServices, String newServiceTitle, String newSubserviceTitle, Double newBasePrice, String newDescription){
        Optional<Services> foundService = servicesRepository.findByServiceTitleAndAndSubserviceTitle(underEditServices.getServiceTitle(), underEditServices.getSubserviceTitle());
        if(foundService.isPresent()){
            if(foundService.get().getSubserviceTitle() == null){
                if(newServiceTitle != null)
                    foundService.get().setServiceTitle(newServiceTitle);
            }
            else {
                if(newServiceTitle != null)
                    foundService.get().setServiceTitle(newServiceTitle);
                if(newSubserviceTitle != null)
                    foundService.get().setSubserviceTitle(newSubserviceTitle);
                if(newBasePrice != null)
                    foundService.get().setBasePrice(newBasePrice);
                if(newDescription != null)
                    foundService.get().setDescription(newDescription);
            }
            return servicesRepository.save(foundService.get());
        }
        else
            throw new ServicesNotFoundException("Service not found.");
    }


    public User addExpertToServices(Long servicesId, Long userId){
        User expert = userService.findById(userId);

        if(!expert.getUserRole().equals(UserRole.EXPERT))
            throw new NotExpertUserException(expert.getName() + " " + expert.getFamilyName()
                    + " has not registered as an expert.");
        if(!expert.getExpertStatus().equals(ExpertStatus.APPROVED))
            throw new NotApprovedExpertException(expert.getName() + " " + expert.getFamilyName()
                    + " has not been approved as expert."
            );
        List<Services> servicesList = servicesRepository.findAll();
        List<Services> foundUserServices = expert.getServices();
        if(!(servicesList.stream().filter(s -> Objects.equals(s.getId(), servicesId)).findFirst()).isPresent())
            throw new ServicesNotFoundException("Service with ID " +
                    servicesId +
                    " not found."
            );

        if((foundUserServices.stream().filter(us -> Objects.equals(us.getId(), servicesId)).findFirst()).isPresent())
            throw new ServicesNotFoundException("Service with ID " +
                    servicesId +
                    " already assigned to " +
                    expert.getName() + " " + expert.getFamilyName()
            );
        foundUserServices.add(Services.builder()
                .id(servicesId)
                .build());
        expert.setServices(foundUserServices);

        return userService.updateUser(expert);
    }


    public User removeExpertFromServices(Long servicesId, Long userId){
        User expert = userService.findById(userId);

        if(!expert.getUserRole().equals(UserRole.EXPERT))
            throw new NotExpertUserException(expert.getName() + " " + expert.getFamilyName()
                    + " has not registered as an expert.");
        if(!expert.getExpertStatus().equals(ExpertStatus.APPROVED))
            throw new NotApprovedExpertException(expert.getName() + " " + expert.getFamilyName()
                    + " has not been approved as expert.");
        List<Services> servicesList = servicesRepository.findAll();
        List<Services> foundUserServices = expert.getServices();
        if(!(servicesList.stream().filter(s -> Objects.equals(s.getId(), servicesId)).findFirst()).isPresent())
            throw new ServicesNotFoundException("Service with ID " +
                    servicesId +
                    " not found."
            );
        if(!(foundUserServices.stream().filter(us -> Objects.equals(us.getId(), servicesId)).findFirst()).isPresent())
            throw new ServicesNotFoundException("Service with ID " +
                    servicesId +
                    " has not been assigned to " +
                    expert.getName() + " " + expert.getFamilyName()
            );
        foundUserServices.remove((foundUserServices.stream().filter(t -> Objects.equals(t.getId(), servicesId)).findFirst()).get());
        expert.setServices(foundUserServices);

        return userService.updateUser(expert);
    }

}
