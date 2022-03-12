package br.com.controledeponto.converter;

import br.com.controledeponto.model.Alocacao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlocacaoConverter {
	Alocacao converter(io.swagger.model.Alocacao alocacao);
	io.swagger.model.Alocacao converter(Alocacao alocacao);
}
