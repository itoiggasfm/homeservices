package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.dto.SubservicesDto;
import com.maktabsharif.homeservices.mapper.ServicesMapper;
import com.maktabsharif.homeservices.mapper.SubservicesMapper;
import com.maktabsharif.homeservices.service.SubservicesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subservices")
public class SubservicesController {

    private final SubservicesService subservicesService;

    public SubservicesController(SubservicesService subservicesService) {
        this.subservicesService = subservicesService;
    }

    @PostMapping("/create")
    public Subservices create(@RequestBody SubservicesDto subservicesDto) throws Exception {
//        System.out.println(subservicesDto.getServicesDto().getServiceTitle());
//        Services services = ServicesMapper.INSTANCE.dtoToModel(subservicesDto.getServicesDto());
        Subservices subservices = SubservicesMapper.INSTANCE.dtoToModel(subservicesDto);
//        subservices.setServices(services);
        return subservicesService.create(subservices);
    }


    @GetMapping("/findByServiceTitle")
    public Subservices findBySubserviceTitle(@RequestParam(value = "serviceTitle", required = false)String serviceTitle) throws Exception {
        return subservicesService.findBySubserviceTitle(serviceTitle);
    }

    @GetMapping("/findBySubserviceTitle")
    public Subservices findBySubserviceTitleAndServiceId(@RequestParam(value = "subServiceTitle", required = false)String subServiceTitle, @RequestParam(value = "serviceId", required = false)Long serviceId) throws Exception {
        return subservicesService.findBySubserviceTitleAndServicesId(subServiceTitle, serviceId);
    }

    @PutMapping("/update")
    public Subservices update(@RequestBody SubservicesDto subservicesDto) throws Exception {
        Subservices subservices = SubservicesMapper.INSTANCE.dtoToModel(subservicesDto);
        return subservicesService.update(subservices);
    }

    @GetMapping("/findById/{userId}")
    public Subservices findById(@PathVariable Long userId) throws Exception {
        return subservicesService.findById(userId);
    }


    @GetMapping("/findAll")
    public List<Subservices> findAll() throws Exception {
        return subservicesService.findAll();
    }

    @DeleteMapping("/deleteById/{userId}")
    public String deleteById(@PathVariable Long userId) throws Exception {
        subservicesService.deleteById(userId);
        return "OK";
    }

}
