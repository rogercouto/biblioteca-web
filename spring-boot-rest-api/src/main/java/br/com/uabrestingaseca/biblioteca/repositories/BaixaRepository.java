package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Baixa;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BaixaRepository extends JpaRepository<Baixa, Integer> {

    @Query("SELECT b FROM Baixa b WHERE b.exemplar = exemplar")
    List<Baixa> findBaixaFromExemplar(@Param("exemplar") Exemplar exemplar);

}
