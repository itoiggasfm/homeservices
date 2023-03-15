package com.maktabsharif.homeservices.dto;


import com.maktabsharif.homeservices.domain.User;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Component
public class EmailDetailsDto {

    Long id;
    String recipient;
    String msgBody;
    String subject;
    String attachment;
    User user;
}
