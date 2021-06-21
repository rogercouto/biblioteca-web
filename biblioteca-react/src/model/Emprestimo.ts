import { Secao, Origem, Exemplar, Usuario } from './';

import DateTimeUtil from '../util/DateTimeUtil';

export class Emprestimo{

    constructor(
        public id?: number,
        public dataHora?: Date,
        public exemplar?: Exemplar,
        public usuario?: Usuario,
        public numRenovacoes?: number,
        public dataHoraDevolucao?: Date,
        public prazo?: Date,
    ){}

    public foiDevolvido() : boolean{
        if (this.dataHoraDevolucao){
            return this.dataHoraDevolucao !== null;
        }
        return false;
    }

    public static createFromData(data : any) : Emprestimo{
        return new Emprestimo(
            data.id,
            DateTimeUtil.fromAPIDateTime(data.dataHora),
            new Exemplar(
                data.exemplar.numRegistro,
                new Secao(data.exemplar.secao.id, data.exemplar.secao.nome),
                DateTimeUtil.fromAPIDate(data.exemplar.dataAquisicao),
                new Origem(data.exemplar.origem.id, data.exemplar.origem.descricao),
                data.exemplar.situacao,
                false,
                { id: 0, titulo: data.exemplar.tituloLivro}
            ),
            new Usuario(
                data.usuario.id,
                data.usuario.nome,
                data.usuario.email
            ),
            data.numRenovacoes,
            data.dataHoraDevolucao,
            DateTimeUtil.fromAPIDate(data.prazo)
        );
    }

    public getTdStyle() : any{
        if ((this.dataHoraDevolucao === null || this.dataHoraDevolucao === undefined) && this.prazo){
            if (new Date() > this.prazo){
                return {fontWeight: 'bold', color: '#990000'}
            }
        }
        return {fontWeight: 'normal'};
    }
}