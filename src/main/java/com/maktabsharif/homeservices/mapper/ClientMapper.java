package com.maktabsharif.homeservices.mapper;

import com.maktabsharif.homeservices.domain.Client;
import com.maktabsharif.homeservices.dto.ClientDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);
    Client dtoToModel(ClientDto clientDto);

}
