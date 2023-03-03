package com.maktabsharif.homeservices.mapper;

import com.maktabsharif.homeservices.domain.Services;
import com.maktabsharif.homeservices.dto.ServicesDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ServicesMapper {

    ServicesMapper INSTANCE = Mappers.getMapper(ServicesMapper.class);
    Services dtoToModel(ServicesDto servicesDto);

}
