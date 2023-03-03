package com.maktabsharif.homeservices.mapper;

import com.maktabsharif.homeservices.domain.Suggestions;
import com.maktabsharif.homeservices.dto.SuggestionsDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SuggestionsMapper {

    SuggestionsMapper INSTANCE = Mappers.getMapper(SuggestionsMapper.class);
    Suggestions dtoToModel(SuggestionsDto suggestionsDto);

}
