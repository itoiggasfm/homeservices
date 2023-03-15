
package com.maktabsharif.homeservices.dto;

import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class ClientDto{

    Long id;

    String name;

    String familyName;

    String username;

    String password;

    @Email
    String email;


}

