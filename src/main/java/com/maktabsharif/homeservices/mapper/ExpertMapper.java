package com.maktabsharif.homeservices.mapper;

import com.maktabsharif.homeservices.domain.Expert;
import com.maktabsharif.homeservices.dto.ExpertDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ExpertMapper {

    ExpertMapper INSTANCE = Mappers.getMapper(ExpertMapper.class);
    Expert dtoToModel(ExpertDto expertDto);

}
