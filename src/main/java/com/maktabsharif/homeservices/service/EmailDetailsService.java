package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.EmailDetailes;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.repository.EmailDetailsRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailDetailsService {

    private final EmailDetailsRepository emailDetailsRepository;
    private final EmailServiceImpl emailService;

    public EmailDetailsService(EmailDetailsRepository emailDetailsRepository,
                               EmailServiceImpl emailService) {
        this.emailDetailsRepository = emailDetailsRepository;
        this.emailService = emailService;
    }

    public void setActivationEmailAndSend(User user){

        EmailDetailes emailDetailes = new EmailDetailes();
        emailDetailes.setRecipient(user.getTempEmail());
        emailDetailes.setSubject("Account activation");
        emailDetailes.setMsgBody("HOME SERVICES TEAM\n\nWe are pleased you registered in our site. Please click on link below to activate your account.\nhttp://localhost:8080/user/activateAccount/" + user.getId());
        emailDetailes.setUser(user);

        sendEmail(emailDetailes);
    }

    public void sendEmail(EmailDetailes emailDetailes){
        if(emailService.sendSimpleMail(emailDetailes).equals("Mail Sent Successfully."))
            save(emailDetailes);
    }

    public void save(EmailDetailes emailDetailes){
        emailDetailsRepository.save(emailDetailes);
    }

}
