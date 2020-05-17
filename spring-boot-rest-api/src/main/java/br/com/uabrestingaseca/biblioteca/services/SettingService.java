package br.com.uabrestingaseca.biblioteca.services;

import org.springframework.stereotype.Service;

@Service
public class SettingService {

    public int getDiasReserva(){
        return 7;
    }

    public int getDiasProrrogacao(){
        return 7;
    }

    public int getDiasEmprestimo(){
        return 7;
    }

}
