package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Assunto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssuntoRepository extends JpaRepository<Assunto, Integer> {

    @Query("SELECT a FROM Assunto a WHERE LOWER(a.descricao) = LOWER(TRIM(:descricao))")
    List<Assunto> findByDescricao(@Param("descricao") String descricao);

    @Query("SELECT a FROM Assunto a WHERE LOWER(a.descricao) = LOWER(CONCAT('%',TRIM(:text),'%'))")
    Page<Assunto> find(@Param("text") String text, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Assunto a WHERE a.id = :id")
    int delete(@Param("id")int id);

}
