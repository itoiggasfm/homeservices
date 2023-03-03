package com.maktabsharif.homeservices.mapper;

import com.maktabsharif.homeservices.domain.Subservices;
import com.maktabsharif.homeservices.dto.SubservicesDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubservicesMapper {

    SubservicesMapper INSTANCE = Mappers.getMapper(SubservicesMapper.class);
    Subservices dtoToModel(SubservicesDto subservicesDto);

}
