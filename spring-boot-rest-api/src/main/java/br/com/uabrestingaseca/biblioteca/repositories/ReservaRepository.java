package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Reserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    @Query("SELECT r FROM Reserva r WHERE r.ativa = :ativa")
    Page<Reserva> findFiltered(@Param("ativa") boolean ativa, Pageable pageable);

    @Query("SELECT r FROM Reserva r WHERE r.ativa = true AND r.exemplar = :exemplar")
    List<Reserva> findFromExemplar(@Param("exemplar") Exemplar exemplar);

}
