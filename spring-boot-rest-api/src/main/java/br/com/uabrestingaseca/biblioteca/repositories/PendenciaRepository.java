package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Pendencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendenciaRepository extends JpaRepository<Pendencia, String> {
}
