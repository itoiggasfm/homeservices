package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.dto.ServicesDto;
import com.maktabsharif.homeservices.mapper.ServicesMapper;
import com.maktabsharif.homeservices.service.ServicesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServicesController {

    private final ServicesService servicesService;

    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }


    @PostMapping("/create")
    public Services create(@RequestBody ServicesDto servicesDto) throws Exception {
        Services services = ServicesMapper.INSTANCE.dtoToModel(servicesDto);
        return servicesService.create(services);
    }


    @GetMapping("/findByServiceTitle")
    public Services findByServiceTitle(@RequestParam(value = "serviceTitle", required = false)String serviceTitle) throws Exception {
        return servicesService.findByServiceTitle(serviceTitle);
    }

    @GetMapping("/edit/{userId}")
    public Services edit(@PathVariable Long serviceId, @RequestParam(value = "newServiceTitle", required = false)String newServiceTitle) throws Exception {
        return servicesService.edit(serviceId, newServiceTitle);
    }

    @PutMapping("/update")
    public Services update(@RequestBody ServicesDto servicesDto) throws Exception {
        Services services = ServicesMapper.INSTANCE.dtoToModel(servicesDto);
        return servicesService.update(services);
    }

    @GetMapping("/findById/{userId}")
    public Services findById(@PathVariable Long userId) throws Exception {
        return servicesService.findById(userId);
    }

    @GetMapping("/findAll")
    public List<Services> findAll() throws Exception {
        return servicesService.findAll();
    }

    @DeleteMapping("/deleteById/{userId}")
    public String deleteById(@PathVariable Long userId) throws Exception {
        servicesService.deleteById(userId);
        return "OK";
    }

}
