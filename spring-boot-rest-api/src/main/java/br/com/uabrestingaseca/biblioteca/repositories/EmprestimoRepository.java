package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Emprestimo;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Integer> {

    @Query("SELECT e FROM Emprestimo e WHERE e.dataHoraDevolucao IS NULL ORDER BY e.dataHoraDevolucao ASC, e.prazo ASC")
    Page<Emprestimo> findAtivos(Pageable pageable);

    @Query("SELECT e FROM Emprestimo e ORDER BY e.dataHoraDevolucao ASC, e.prazo ASC")
    Page<Emprestimo> findDevolvidos(Pageable pageable);

    @Query("SELECT e FROM Emprestimo e ORDER BY e.dataHoraDevolucao ASC, e.prazo ASC")
    Page<Emprestimo> findAll(Pageable pageable);

    @Query("SELECT e FROM Emprestimo e WHERE e.exemplar = :exemplar")
    Page<Emprestimo> findFromExemplar(@Param("exemplar") Exemplar exemplar, Pageable pageable);

    @Query("SELECT e FROM Emprestimo e WHERE e.usuario = :usuario")
    Page<Emprestimo> findFromUsuario(@Param("usuario") Usuario usuario, Pageable pageable);

    @Query("SELECT e FROM Emprestimo e WHERE e.usuario = :usuario AND e.dataHoraDevolucao IS NULL")
    Page<Emprestimo> findAtivosPageFromUsuario(@Param("usuario") Usuario usuario, Pageable pageable);

    @Query("SELECT e FROM Emprestimo e WHERE e.usuario = :usuario AND e.dataHoraDevolucao IS NOT NULL")
    Page<Emprestimo> findInativosPageFromUsuario(@Param("usuario") Usuario usuario, Pageable pageable);

    @Query("SELECT e FROM Emprestimo e WHERE e.usuario = :usuario AND e.dataHoraDevolucao IS NULL")
    @Deprecated
    List<Emprestimo> findAtivosFromUsuarioList(@Param("usuario") Usuario usuario);

}
