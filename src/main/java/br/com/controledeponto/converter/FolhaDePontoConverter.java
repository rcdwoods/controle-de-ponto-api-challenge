package br.com.controledeponto.converter;

import br.com.controledeponto.model.FolhaDePonto;
import io.swagger.model.Relatorio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {AlocacaoConverter.class, RegistroDeTrabalhoConverter.class})
public interface FolhaDePontoConverter {
	@Mappings({
		@Mapping(target = "mes", expression = ("java(folhaDePonto.getMes().toString())")),
		@Mapping(source = "registrosDeTrabalho", target = "registros")
	})
	Relatorio converter(FolhaDePonto folhaDePonto);
}
