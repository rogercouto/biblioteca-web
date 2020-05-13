package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Assunto;
import br.com.uabrestingaseca.biblioteca.model.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    @Query("SELECT c FROM Categoria c WHERE LOWER(c.descricao) = LOWER(TRIM(:descricao))")
    List<Categoria> findByDescricao(@Param("descricao") String descricao);

    @Query("SELECT c FROM Categoria c WHERE " +
            "LOWER(c.descricao) LIKE CONCAT('%',TRIM(:text),'%')")
    Page<Categoria> find(@Param("text") String text, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Categoria c WHERE c.id = :id")
    int delete(@Param("id")int id);

}
