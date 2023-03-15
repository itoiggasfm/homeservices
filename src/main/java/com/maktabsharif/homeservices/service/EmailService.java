package com.maktabsharif.homeservices.service;

import com.maktabsharif.homeservices.domain.EmailDetailes;

public interface EmailService {

    // To send a simple email
    String sendSimpleMail(EmailDetailes emailDetailes);

    // To send an email with attachment
    String sendMailWithAttachment(EmailDetailes emailDetailes);
}
