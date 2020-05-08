package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Assunto;
import br.com.uabrestingaseca.biblioteca.model.Editora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AssuntoRepository extends JpaRepository<Assunto, Integer> {

    @Query("select a from Assunto a where lower(a.descricao) = lower(:descricao)")
    Assunto findByDescricao(@Param("descricao") String descricao);

}
