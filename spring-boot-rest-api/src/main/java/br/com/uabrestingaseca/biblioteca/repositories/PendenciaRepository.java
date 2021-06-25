package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Pendencia;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PendenciaRepository extends JpaRepository<Pendencia, Integer> {

    @Query("SELECT p FROM Pendencia p WHERE p.dataHoraPagamento IS NULL")
    Page<Pendencia> findAtivasPage(Pageable pageable);

    @Query("SELECT p FROM Pendencia p WHERE p.dataHoraPagamento IS NOT NULL")
    Page<Pendencia> findInativasPage(Pageable pageable);

    @Query("SELECT p FROM Pendencia p WHERE p.usuario = :usuario")
    List<Pendencia> findListFromUsuario(@Param("usuario") Usuario usuario);


    @Query("SELECT p FROM Pendencia p WHERE p.usuario = :usuario AND p.dataHoraPagamento IS NULL")
    List<Pendencia> findAtivasListFromUsuario(@Param("usuario") Usuario usuario);

    @Query("SELECT p FROM Pendencia p WHERE p.usuario = :usuario AND p.dataHoraPagamento IS NOT NULL")
    List<Pendencia> findInativasListFromUsuario(@Param("usuario") Usuario usuario);
}
