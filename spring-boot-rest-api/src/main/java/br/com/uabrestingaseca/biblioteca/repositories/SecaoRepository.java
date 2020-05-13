package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Secao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SecaoRepository extends JpaRepository<Secao, Integer> {

    @Query("SELECT s FROM Secao s WHERE LOWER(s.nome) = LOWER(TRIM(:nome))")
    List<Secao> findByNome(@Param("nome") String nome);
    
    @Query("SELECT s FROM Secao s WHERE LOWER(s.nome) = LOWER(CONCAT('%',TRIM(:text),'%'))")
    Page<Secao> find(@Param("text") String text, Pageable pageable);
    
    @Modifying
    @Query("DELETE FROM Secao s WHERE s.id = :id")
    int delete(@Param("id")int id);

}
