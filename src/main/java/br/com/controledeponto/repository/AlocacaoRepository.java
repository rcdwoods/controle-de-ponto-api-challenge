package br.com.controledeponto.repository;

import br.com.controledeponto.model.Alocacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AlocacaoRepository extends JpaRepository<Alocacao, Long> {
	List<Alocacao> findAllByDia(LocalDate dia);
}
