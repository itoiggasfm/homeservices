package com.maktabsharif.homeservices.mapper;

import com.maktabsharif.homeservices.domain.User;
import com.maktabsharif.homeservices.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper  {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    User dtoToModel(UserDto userDto);

}
