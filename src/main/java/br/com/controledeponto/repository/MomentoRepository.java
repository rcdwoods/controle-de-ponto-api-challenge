package br.com.controledeponto.repository;

import br.com.controledeponto.model.Momento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MomentoRepository extends JpaRepository<Momento, Long> {
}
