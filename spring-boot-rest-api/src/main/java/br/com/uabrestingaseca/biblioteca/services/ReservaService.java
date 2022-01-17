package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.exceptions.ModelValidationException;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Reserva;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.repositories.ReservaRepository;
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
public class ReservaService {

    @Autowired
    private ReservaRepository repository;

    @Autowired
    private ExemplarService exemplarService;

    @Autowired
    private ParametroService parametroService;

    @Autowired
    private UsuarioService usuarioService;

    public Reserva findById(int id){
        return repository.findById(id).orElse(null);
    }

    public Page<Reserva> findAll(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Reserva> findFiltered(boolean ativa, Pageable pageable){
        return repository.findFiltered(ativa, pageable);
    }

    public List<Reserva> findListFiltered(boolean ativa){
        return repository.findFilteredList(ativa);
    }

    public List<Reserva> findAtivasFromExemplar(Exemplar exemplar){
        return repository.findFromExemplar(exemplar);
    }

    public Page<Reserva> findFromExemplar(Exemplar exemplar, Pageable pageable){
        return repository.findFromExemplar(exemplar, pageable);
    }

    public Page<Reserva> findPageFromUsuario(Usuario usuario, Pageable pageable){
        return repository.findPageFromUsuario(usuario, pageable);
    }

    public Page<Reserva> findPageFromUsuario(Usuario usuario, Boolean ativa, Pageable pageable){
        if (ativa != null){
            return repository.findFilteredPageFromUsuario(usuario, ativa, pageable);
        }else{
            return repository.findPageFromUsuario(usuario, pageable);
        }
    }

    public Exemplar validateNew(Reserva reserva){
        Set<String> errors = new TreeSet<>();
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Reserva>> violations = validator.validate(reserva);
        violations.forEach(v ->{
            errors.add(v.getMessage());
        });
        if (reserva.getId() != null){
            errors.add("Id da reserva é gerado pela API");
        }
        if (reserva.getExemplar() == null){
            errors.add("Exemplar deve ser iformado");
        }
        if (reserva.getExemplar().getNumRegistro() != null && !reserva.getExemplar().onlyNumRegistroSet()){
            errors.add("Somente o campo numRegistro deve ser infomado para o exemplar");
        }
        if (!reserva.isAtiva()){
            errors.add("Não é possível incluir uma reserva inativa");
        }
        if (reserva.getUsuario().onlyIdSet()){
            Usuario usuario = usuarioService.findById(reserva.getUsuario().getId());
            if (usuario == null){
                errors.add(String.format(
                        "Usuario com id = %d não existe",
                        reserva.getUsuario().getId()));
            }else{
                reserva.setUsuario(usuario);
            }
            int totalAtivas = repository.findAtivasFromUsuarioList(usuario).size();
            int limiteReservas = parametroService.getLimiteReservas();
            if (totalAtivas >= limiteReservas){
                errors.add("Não é possivel realizar a reserva, limite excedido!");
            }
        }
        Exemplar exemplar = null;
        if (errors.size() == 0){
            exemplar = exemplarService.findByNumRegistro(reserva.getExemplar().getNumRegistro());
            if (exemplar == null) {
                errors.add(String.format(
                        "Exemplar com número de registro = %d não existe",
                        reserva.getExemplar().getNumRegistro()));
            }else if(reserva.isAtiva() && exemplar.isFixo()){
                errors.add("Exemplar não pode ser reservado/emprestado");
            }else if (reserva.isAtiva() && !exemplar.isDisponivel()){
                errors.add("Exemplar indisponível");
            }else if (reserva.isAtiva() && exemplar.isReservado()){
                errors.add("Exemplar já reservado");
            }
        }
        if (errors.size()  > 0){
            throw new ModelValidationException(
                    "Erro ao incluir reserva",
                    new LinkedList<>(errors));
        }
        return exemplar;
    }
    
    @Transactional
    public Reserva create(Reserva reserva){
        if (reserva.getUsuario() == null && reserva.getUsuarioId() != null){
            reserva.setUsuario(usuarioService.findById(reserva.getUsuarioId()));
        }
        if (reserva.getExemplar() == null && reserva.getExemplarNumRegistro() != null){
            reserva.setExemplar(new Exemplar(reserva.getExemplarNumRegistro()));
        }
        Exemplar exemplar = validateNew(reserva);
        exemplar.setReservado(true);
        exemplar = exemplarService.save(exemplar);
        if (reserva.getDataHora() == null)
            reserva.setDataHora(LocalDateTime.now());
        if (reserva.getDataLimite() == null){
            int diasReserva = parametroService.getDiasReserva();
            reserva.setDataLimite(LocalDate.now().plusDays(diasReserva));
        }
        reserva.setExemplar(exemplar);
        return repository.save(reserva);
    }

    public Exemplar validateExistent(Reserva reserva){
        Set<String> errors = new TreeSet<>();
        if (reserva.getId() == null){
            errors.add("Id da reserva deve ser informado");
        }
        if (reserva.getExemplar() != null && reserva.getExemplarNumRegistro() != null){
            errors.add("Exemplar não pode ser alterado");
        }
        if (reserva.getDataHoraRetirada() != null && reserva.isAtiva()){
            reserva.setAtiva(false);
        }
        Reserva old = repository.findById(reserva.getId()).orElse(null);
        Exemplar exemplar = null;
        if (old == null){
            errors.add(String.format("Reserva com Id=%d não existe",
                    reserva.getId()));
        }else{
            //somente atualiza o exemplar se a reserva mudar o status
            if (old.isAtiva() != reserva.isAtiva()){
                exemplar = old.getExemplar();
            }
            reserva.setExemplar(old.getExemplar());
            reserva.setUsuario(old.getUsuario());
            if (reserva.getDataHora() == null){
                reserva.setDataHora(old.getDataHora());
            }
            if (reserva.getDataLimite() == null){
                reserva.setDataLimite(old.getDataLimite());
            }
        }
        if (errors.size()  > 0){
            throw new ModelValidationException(
                    "Erro ao atualizar reserva",
                    new LinkedList<>(errors));
        }
        return exemplar;
    }

    @Transactional
    public Reserva update(Reserva reserva){
        Exemplar exemplar = validateExistent(reserva);
        if (exemplar != null){
            exemplar.setReservado(reserva.isAtiva());
            exemplar = exemplarService.save(exemplar);
            reserva.setExemplar(exemplar);
        }
        return repository.save(reserva);
    }

    @Transactional
    public void delete(int id){
        Reserva reserva = repository.findById(id).orElse(null);
        if (reserva == null){
            throw new ModelValidationException("Erro na exclusão da reserva",
                    String.format("Reserva com Id=%d não existe",id));
        }
        Exemplar exemplar = reserva.getExemplar();
        repository.delete(reserva);
        exemplar.setReservado(false);
        exemplarService.save(exemplar);
    }

}
