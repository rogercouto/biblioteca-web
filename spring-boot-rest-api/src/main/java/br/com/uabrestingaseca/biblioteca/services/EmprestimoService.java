package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Emprestimo;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Reserva;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.repositories.EmprestimoRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Service
public class EmprestimoService {

    @Autowired
    private EmprestimoRepository repository;

    @Autowired
    private ExemplarService exemplarService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private ParametroService parametroService;

    public Emprestimo findById(int id){
        return repository.findById(id).orElse(null);
    }

    public Page<Emprestimo> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Emprestimo> findFiltered(boolean ativos, Pageable pageable){
        return ativos ?
                repository.findAtivos(pageable) :
                repository.findDevolvidos(pageable);
    }

    public Page<Emprestimo> findFromExemplar(Exemplar exemplar, Pageable pageable){
        return repository.findFromExemplar(exemplar, pageable);
    }

    public Page<Emprestimo> findFromUsuario(Usuario usuario, Pageable pageable){
        return repository.findFromUsuario(usuario, pageable);
    }

    public Exemplar validateNew(Emprestimo emprestimo){
        Set<String> errors = new TreeSet<>();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Emprestimo>> violations = validator.validate(emprestimo);
        violations.forEach(v ->{
            errors.add(v.getMessage());
        });
        if (emprestimo.getId() != null){
            errors.add("Id da emprestimo é gerado pela API");
        }
        if (emprestimo.getExemplar() == null){
            errors.add("Exemplar deve ser informado");
        }
        if (emprestimo.getExemplar().getNumRegistro() != null && !emprestimo.getExemplar().onlyNumRegistroSet()){
            errors.add("Somente o campo numRegistro deve ser infomado para o exemplar");
        }
        if (emprestimo.getDataHoraDevolucao() != null){
            errors.add("Não é possível incluir uma emprestimo já devolvido");
        }
        if (emprestimo.getUsuario().onlyIdSet()){
            Usuario usuario = usuarioService.findById(emprestimo.getUsuario().getId());
            if (usuario == null){
                errors.add(String.format(
                        "Usuario com id = %d não existe",
                        emprestimo.getUsuario().getId()));
            }else{
                emprestimo.setUsuario(usuario);
            }
            int totalAtivos = repository.findAtivosFromUsuarioList(usuario).size();
            int limiteEmprestimos = parametroService.getLimiteEmprestimos();
            if (totalAtivos >= limiteEmprestimos){
                errors.add("Não é possivel realizar o empréstimo, limite excedido!");
            }
        }
        Exemplar exemplar = null;
        if (errors.size() == 0){
            exemplar = exemplarService.findByNumRegistro(emprestimo.getExemplar().getNumRegistro());
            if (exemplar == null) {
                errors.add(String.format(
                        "Exemplar com número de registro = %d não existe",
                        emprestimo.getExemplar().getNumRegistro()));
            }else if(exemplar.getFixo()){
                errors.add("Exemplar não pode ser reservado/emprestado");
            }else if (!exemplar.getDisponivel()){
                errors.add("Exemplar indisponível");
            }else if (exemplar.getEmprestado()){
                errors.add("Exemplar já emprestimodo");
            }
        }
        if (errors.size()  > 0){
            throw new ModelValidationException(
                    "Erro ao incluir emprestimo",
                    new LinkedList<>(errors));
        }
        return exemplar;
    }

    @Transactional
    public Emprestimo create(Emprestimo emprestimo){
        if (emprestimo.getUsuario() == null && emprestimo.getUsuarioId() != null){
            emprestimo.setUsuario(usuarioService.findById(emprestimo.getUsuarioId()));
        }
        if (emprestimo.getExemplar() == null && emprestimo.getExemplarNumRegistro() != null){
            emprestimo.setExemplar(new Exemplar(emprestimo.getExemplarNumRegistro()));
        }
        Exemplar exemplar = validateNew(emprestimo);
        if (exemplar.isReservado()){
            List<Reserva> reservas = reservaService.findAtivasFromExemplar(exemplar);
            if (reservas.size() == 1){
                Reserva reserva = reservas.get(0);
                reserva.setAtiva(false);
                reserva.setDataHoraRetirada(LocalDateTime.now());
                reservaService.update(reserva);
                exemplar.setReservado(false);
            }
        }
        exemplar.setEmprestado(true);
        exemplar = exemplarService.save(exemplar);
        if (emprestimo.getDataHora() == null){
            emprestimo.setDataHora(LocalDateTime.now());
        }
        emprestimo.setExemplar(exemplar);
        int diasEmprestimo = parametroService.getDiasEmprestimo();
        LocalDate prazo = emprestimo.getDataHora().plusDays(diasEmprestimo * (emprestimo.getNumRenovacoes()+1)).toLocalDate();
        emprestimo.setPrazo(prazo);
        return repository.save(emprestimo);
    }

    public Exemplar validateExistent(Emprestimo emprestimo){
        Set<String> errors = new TreeSet<>();
        if (emprestimo.getId() == null){
            errors.add("Id da emprestimo deve ser informado");
        }
        if (emprestimo.getExemplar() != null && emprestimo.getExemplarNumRegistro() != null){
            errors.add("Exemplar não pode ser alterado");
        }
        Emprestimo old = repository.findById(emprestimo.getId()).orElse(null);
        Exemplar exemplar = null;
        if (old == null){
            errors.add(String.format("Emprestimo com Id=%d não existe",
                    emprestimo.getId()));
        }else{
            //somente atualiza o exemplar se a emprestimo mudar o status
            boolean oldDevolvido = old.getDataHoraDevolucao() != null;
            boolean newDevolvido = emprestimo.getDataHoraDevolucao() != null;
            if (oldDevolvido != newDevolvido){
                exemplar = old.getExemplar();
            }
            emprestimo.setExemplar(old.getExemplar());
            emprestimo.setUsuario(old.getUsuario());
            if (emprestimo.getDataHora() == null){
                emprestimo.setDataHora(old.getDataHora());
            }
        }
        if (errors.size()  > 0){
            throw new ModelValidationException(
                    "Erro ao atualizar emprestimo",
                    new LinkedList<>(errors));
        }
        return exemplar;
    }
    
    public Emprestimo update(Emprestimo emprestimo){
        Exemplar exemplar = validateExistent(emprestimo);
        if (exemplar != null){
            exemplar.setEmprestado(emprestimo.getDataHoraDevolucao() == null);
            exemplar = exemplarService.save(exemplar);
            emprestimo.setExemplar(exemplar);
        }
        int diasEmprestimo = parametroService.getDiasEmprestimo();
        LocalDate prazo = emprestimo.getDataHora().plusDays(diasEmprestimo * (emprestimo.getNumRenovacoes()+1)).toLocalDate();
        emprestimo.setPrazo(prazo);
        return repository.save(emprestimo);
    }

    public Emprestimo devolucao(Emprestimo emprestimo){
        if (emprestimo.getDataHoraDevolucao() == null){
            emprestimo.setDataHoraDevolucao(LocalDateTime.now());
        }
        Exemplar exemplar = emprestimo.getExemplar();
        exemplar.setEmprestado(false);
        exemplar = exemplarService.save(exemplar);
        emprestimo.setExemplar(exemplar);
        return repository.save(emprestimo);
    }

    public void delete(int id){
        Emprestimo emprestimo = repository.findById(id).orElse(null);
        if (emprestimo == null){
            throw new ModelValidationException("Erro na exclusão do empréstimo",
                    String.format("Empréstimo com Id=%d não existe",id));
        }
        Exemplar exemplar = emprestimo.getExemplar();
        repository.delete(emprestimo);
        exemplar.setEmprestado(false);
        exemplarService.save(exemplar);
    }



}
