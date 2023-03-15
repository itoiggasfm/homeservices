package com.maktabsharif.homeservices.controller;

import com.maktabsharif.homeservices.domain.Client;
import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.domain.enumeration.Role;
import com.maktabsharif.homeservices.dto.ClientDto;
import com.maktabsharif.homeservices.mapper.ClientMapper;
import com.maktabsharif.homeservices.service.ClientService;
import com.maktabsharif.homeservices.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final UserService userService;
    public ClientController(ClientService clientService,
                            UserService userService) {
        this.clientService = clientService;
        this.userService = userService;
    }


    @PostMapping("/create")
    public void create(@Valid @RequestBody ClientDto clientDto) throws Exception {
        Client client = ClientMapper.INSTANCE.dtoToModel(clientDto);
        clientService.create(client);
    }

    @PostMapping("/signIn")
    public Client signIn(@Valid @RequestBody ClientDto clientDto){
        User user = ClientMapper.INSTANCE.dtoToModel(clientDto);
        return clientService.singIn(user.getUsername(), user.getPassword());
    }

    @PutMapping("/update")
    public Client update(@RequestBody ClientDto clientDto) throws Exception {
        Client client = ClientMapper.INSTANCE.dtoToModel(clientDto);
        return clientService.update(client);
    }



}
