package com.maktabsharif.homeservices.mapper;

import com.maktabsharif.homeservices.domain.Admin;
import com.maktabsharif.homeservices.dto.AdminDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminMapper {

    AdminMapper INSTANCE = Mappers.getMapper(AdminMapper.class);
    Admin dtoToModel(AdminDto adminDto);

}
