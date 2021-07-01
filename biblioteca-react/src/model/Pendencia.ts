import { Emprestimo, Usuario } from ".";
import DateTimeUtil from "../util/DateTimeUtil";

export class Pendencia{

    constructor(
        public id?:number,
        public usuario?: Usuario,
        public valor?: number,
        public descricao?: string,
        public emprestimo?: Emprestimo,
        public dataHoraLancamento?: Date,
        public dataHoraPagamento?: Date
    ){}

    public static createFromData(data : any):Pendencia{
        const pendencia = new Pendencia();
        pendencia.id = data.id;
        pendencia.usuario = Usuario.createFromData(data.usuario);
        pendencia.valor = Number(data.valor).valueOf();
        pendencia.descricao = data.descricao;
        pendencia.emprestimo = Emprestimo.createFromData(data.emprestimo);
        pendencia.dataHoraLancamento = DateTimeUtil.fromAPIDateTime(data.dataHoraLancamento);
        pendencia.dataHoraPagamento = DateTimeUtil.fromAPIDateTime(data.dataHoraPagamento);
        return pendencia;
    }

    public foiPaga():boolean{
        return this.dataHoraPagamento? true : false;
    }
    
}