package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Pendencia;
import br.com.uabrestingaseca.biblioteca.repositories.PendenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PendenciaService {

    @Autowired
    private PendenciaRepository repository;

    public Pendencia save(Pendencia pendencia){
        return repository.save(pendencia);
    }

}
