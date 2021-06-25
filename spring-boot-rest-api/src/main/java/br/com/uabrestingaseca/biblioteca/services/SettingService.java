package br.com.uabrestingaseca.biblioteca.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Deprecated
public class SettingService {

    @Deprecated
    public int getDiasReserva(){
        return 7;
    }

    @Deprecated
    public int getDiasProrrogacao(){
        return 7;
    }

    @Deprecated
    public int getDiasEmprestimo(){
        return 7;
    }

    @Deprecated
    public BigDecimal getMultaDiaria(){
        return new BigDecimal(5);
    }

}
