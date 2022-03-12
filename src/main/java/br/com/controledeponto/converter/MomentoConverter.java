package br.com.controledeponto.converter;

import br.com.controledeponto.model.Momento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MomentoConverter {
	@Mapping(target = "registroDeTrabalho", ignore = true)
	Momento converter(io.swagger.model.Momento momento);
	io.swagger.model.Momento converter(Momento momento);
}
