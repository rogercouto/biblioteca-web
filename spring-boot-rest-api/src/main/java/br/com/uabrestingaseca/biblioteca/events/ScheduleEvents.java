package br.com.uabrestingaseca.biblioteca.events;

import br.com.uabrestingaseca.biblioteca.model.Reserva;
import br.com.uabrestingaseca.biblioteca.services.ReservaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ScheduleEvents {

    private static final Logger log = LoggerFactory.getLogger(ScheduleEvents.class);

    @Autowired
    private ReservaService reservaService;

    @Scheduled(cron = "0 0 10 * * ?")
    public void checkReservasAtivas() {
        List<Reserva> reservasAtivas = reservaService.findListFiltered(true);
        reservasAtivas.forEach(reserva -> {
            LocalDate today = LocalDate.now();
            LocalDate dataLimite = reserva.getDataLimite();
            if (today.isAfter(dataLimite)){
                reserva.setAtiva(false);
                reservaService.update(reserva);
                log.info("Reserva expirada do exemplar com Nº de reigstro = {}", reserva.getExemplar().getNumRegistro());
            }
        });
    }

}
