package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.Admin;
import com.maktabsharif.homeservices.domain.Expert;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.dto.AdminDto;
import com.maktabsharif.homeservices.dto.UserDto;
import com.maktabsharif.homeservices.mapper.AdminMapper;
import com.maktabsharif.homeservices.mapper.UserMapper;
import com.maktabsharif.homeservices.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

        @PostMapping("/signIn")
    public User signIn(@Valid @RequestBody AdminDto adminDto){
        Admin admin = AdminMapper.INSTANCE.dtoToModel(adminDto);
        return adminService.singIn(admin.getUsername(), admin.getPassword());
    }

    @PutMapping("/update")
    public Admin update(@RequestBody AdminDto adminDto) throws Exception {
        Admin admin = AdminMapper.INSTANCE.dtoToModel(adminDto);
        return adminService.update(admin);
    }

    @GetMapping("/approveNewExperts/{userId}")
    public Expert approveNewExperts(@PathVariable Long userId){
        return adminService.approveNewExperts(userId);
    }

    @PostMapping("/search")
    public List<User> search(@Valid @RequestBody UserDto userDto,
                             @RequestParam(value = "subserviceTitle")String subserviceTitle,
                             @RequestParam(value = "lowExpertPoint")Integer lowExpertPoint,
                             @RequestParam(value = "highExpertPoint")Integer highExpertPoint){
        User user = UserMapper.INSTANCE.dtoToModel(userDto);
        return adminService.findUserByUserRoleAndNameAndFamilyNameAndEmailAndExpertPoint(user, subserviceTitle, lowExpertPoint, highExpertPoint);
    }

    @GetMapping("/addExpertToServices/{expertId}")
    public String addExpertToServices(@PathVariable Long expertId, @RequestParam(value = "servicesId") Long servicesId) {
        adminService.addExpertToServices(expertId, servicesId);
        return "Done";
    }

        @GetMapping("/removeExpertFromServices/{expertId}")
    public String removeExpertFromServices(@PathVariable Long expertId, @RequestParam(value = "servicesId") Long servicesId){
        adminService.removeExpertFromServices(expertId, servicesId);
        return "Done";
    }

}
