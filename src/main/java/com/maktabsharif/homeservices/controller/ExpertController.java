package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.Expert;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import com.maktabsharif.homeservices.dto.ExpertDto;
import com.maktabsharif.homeservices.mapper.ExpertMapper;
import com.maktabsharif.homeservices.service.ExpertService;
import com.maktabsharif.homeservices.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expert")
public class ExpertController {

    private final ExpertService expertService;
    private final UserService userService;
    public ExpertController(ExpertService expertService,
                            UserService userService) {
        this.expertService = expertService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public void create(@Valid @RequestBody ExpertDto expertDto) throws Exception {
        Expert expert = ExpertMapper.INSTANCE.dtoToModel(expertDto);
        expertService.create(expert);
    }

    @PostMapping("/signIn")
    public Expert signIn(@Valid @RequestBody ExpertDto expertDto) {
        Expert expert = ExpertMapper.INSTANCE.dtoToModel(expertDto);
        return expertService.singIn(expert.getUsername(), expert.getPassword());
    }

    @PutMapping("/update")
    public Expert update(@RequestBody ExpertDto expertDto) throws Exception {
        Expert expert = ExpertMapper.INSTANCE.dtoToModel(expertDto);
        return expertService.update(expert);
    }




}
