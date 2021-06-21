import DateTimeUtil from "../util/DateTimeUtil";
import { Exemplar, Usuario } from "./";

export class Reserva{

    constructor(
        public id?: number,
        public exemplar?: Exemplar,
        public usuario?: Usuario,
        public dataHora?: Date,
        public dataLimite?: Date,
        public dataHoraRetirada?: Date,
        public ativa?: boolean
    ){};

    public static createFromData(data : any) : Reserva{
        const reserva = new Reserva();
        reserva.id = data.id;
        reserva.exemplar = Exemplar.createFromData(data.exemplar);
        reserva.usuario = Usuario.createFromData(data.usuario);
        reserva.dataHora = DateTimeUtil.fromAPIDateTime(data.dataHora);
        reserva.dataLimite = DateTimeUtil.fromAPIDate(data.dataLimite);
        reserva.dataHoraRetirada = DateTimeUtil.fromAPIDateTime(data.dataHoraRetirada);
        reserva.ativa = data.ativa;
        return reserva;
    }

}