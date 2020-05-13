package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Origem;
import br.com.uabrestingaseca.biblioteca.model.Editora;
import br.com.uabrestingaseca.biblioteca.model.Origem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrigemRepository extends JpaRepository<Origem, Integer> {

    @Query("SELECT o FROM Origem o WHERE LOWER(o.descricao) = LOWER(TRIM(:descricao))")
    List<Origem> findByDescricao(@Param("descricao") String descricao);
    
    @Query("SELECT o FROM Origem o WHERE LOWER(o.descricao) = LOWER(CONCAT('%',TRIM(:text),'%'))")
    Page<Origem> find(@Param("text") String text, Pageable pageable);
    
    @Modifying
    @Query("DELETE FROM Origem o WHERE o.id = :id")
    int delete(@Param("id")int id);

}
