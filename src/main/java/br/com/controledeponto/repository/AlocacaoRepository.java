package br.com.controledeponto.repository;

import br.com.controledeponto.model.Alocacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AlocacaoRepository extends JpaRepository<Alocacao, Long> {
	List<Alocacao> findAllByDia(LocalDate dia);
	List<Alocacao> findAllByDiaContaining(String mes);
	Optional<Alocacao> findByDiaAndNomeProjeto(LocalDate dia, String nomeProjeto);
}
