package br.com.controledeponto.repository;

import br.com.controledeponto.model.RegistroDeTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RegistroDeTrabalhoRepository extends JpaRepository<RegistroDeTrabalho, Long> {
	Optional<RegistroDeTrabalho> findByDia(LocalDate dia);
}
