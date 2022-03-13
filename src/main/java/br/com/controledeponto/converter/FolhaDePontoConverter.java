package br.com.controledeponto.converter;

import br.com.controledeponto.model.FolhaDePonto;
import io.swagger.model.Relatorio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AlocacaoConverter.class, RegistroDeTrabalhoConverter.class})
public interface FolhaDePontoConverter {
	@Mapping(target = "mes", expression = ("java(folhaDePonto.getMes().toString())"))
	@Mapping(source = "registrosDeTrabalho", target = "registros")
	Relatorio converter(FolhaDePonto folhaDePonto);
}
