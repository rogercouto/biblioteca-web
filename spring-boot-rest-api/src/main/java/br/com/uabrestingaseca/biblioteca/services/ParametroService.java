package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.enums.ParamKeys;
import br.com.uabrestingaseca.biblioteca.model.Parametro;
import br.com.uabrestingaseca.biblioteca.repositories.ParametroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Optional;

@Service
public class ParametroService {

    @Autowired
    private ParametroRepository repository;

    public Parametro findByKey(ParamKeys key){
        return repository.findById(key.name()).orElse(null);
    }

    public String findValueByKey(ParamKeys key){
        Optional<Parametro> param = repository.findById(key.name());
        if (param.isPresent()){
            return param.get().getValor();
        }
        return null;
    }

    public Integer findIntValueByKey(ParamKeys key){
        Optional<Parametro> param = repository.findById(key.name());
        if (param.isPresent()){
            String value = param.get().getValor();
            DecimalFormat df = new DecimalFormat("0");
            try {
                return df.parse(value).intValue();
            }catch (ParseException e){}
        }
        return null;
    }

    public BigDecimal findDecimalValueByKey(ParamKeys key){
        Optional<Parametro> param = repository.findById(key.name());
        if (param.isPresent()){
            String value = param.get().getValor();
            DecimalFormat df = new DecimalFormat("0.00");
            try {
                Number number = df.parse(value);
                return new BigDecimal(number.doubleValue());
            } catch (ParseException e) {
            }
        }
        return new BigDecimal(0.0);
    }

    public int getDiasEmprestimo(){
        Integer diasEmprestimo = findIntValueByKey(ParamKeys.diasEmprestimo);
        if (diasEmprestimo != null){
            return diasEmprestimo.intValue();
        }
        return 7;
    }

    public int getDiasReserva(){
        Integer diasReserva = findIntValueByKey(ParamKeys.diasReserva);
        if (diasReserva != null){
            return diasReserva.intValue();
        }
        return 7;
    }

    public BigDecimal getMultaPorDiasAtrazo(){
        BigDecimal bd = findDecimalValueByKey(ParamKeys.multaPorDiaAtraso);
        if (bd != null){
            return bd;
        }
        return new BigDecimal(0.0);
    }

    public int getLimiteRenov(){
        Integer limite = findIntValueByKey(ParamKeys.limiteRenov);
        if (limite != null){
            return limite.intValue();
        }
        return 1;
    }

    public int getLimiteReservas(){
        Integer limite = findIntValueByKey(ParamKeys.limiteReservas);
        if (limite != null){
            return limite.intValue();
        }
        return 1;
    }

    public int getLimiteEmprestimos(){
        Integer limite = findIntValueByKey(ParamKeys.limiteEmprestimos);
        if (limite != null){
            return limite.intValue();
        }
        return 1;
    }

}
