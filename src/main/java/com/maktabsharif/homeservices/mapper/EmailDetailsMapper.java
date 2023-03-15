package com.maktabsharif.homeservices.mapper;

import com.maktabsharif.homeservices.domain.EmailDetailes;
import com.maktabsharif.homeservices.dto.EmailDetailsDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmailDetailsMapper {

    EmailDetailsMapper INSTANCE = Mappers.getMapper(EmailDetailsMapper.class);

    EmailDetailes dtoToModel(EmailDetailsDto emailDetailsDto);

}
