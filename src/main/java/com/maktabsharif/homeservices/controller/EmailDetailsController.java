package com.maktabsharif.homeservices.controller;


import com.maktabsharif.homeservices.domain.EmailDetailes;
import com.maktabsharif.homeservices.domain.Orders;
import com.maktabsharif.homeservices.dto.EmailDetailsDto;
import com.maktabsharif.homeservices.mapper.EmailDetailsMapper;
import com.maktabsharif.homeservices.mapper.OrdersMapper;
import com.maktabsharif.homeservices.service.EmailDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailDetailsController {


    private final EmailDetailsService emailDetailsService;

    public EmailDetailsController(EmailDetailsService emailDetailsService) {
        this.emailDetailsService = emailDetailsService;
    }

    @PostMapping("/sendEmail")
    public String sendEmail(@RequestBody EmailDetailsDto emailDetailsDto) throws Exception {
        EmailDetailes emailDetailes = EmailDetailsMapper.INSTANCE.dtoToModel(emailDetailsDto);
         emailDetailsService.sendEmail(emailDetailes);

         return "Email sent successfully.";
    }

}
