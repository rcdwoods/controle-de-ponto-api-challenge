package br.com.controledeponto.converter;

import io.swagger.model.RegistroDeTrabalho;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistroDeTrabalhoConverter {
	@Mapping(target = "tempo", expression = ("java(registroDeTrabalho.getHorasTrabalhadas().toString())"))
	RegistroDeTrabalho converter(br.com.controledeponto.model.RegistroDeTrabalho registroDeTrabalho);
}
