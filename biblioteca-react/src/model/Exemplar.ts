import { Secao } from './Secao';
import { Origem } from './Origem';

import DateTimeUtil from '../util/DateTimeUtil';

export class Exemplar{

    constructor(
        public numRegistro?:number,
        public secao?: Secao,
        public dataAquisicao?: Date,
        public origem?: Origem,
        public situacao?: string,
        public fixo?: boolean,
        public livro?: { id: number, titulo: string}
    ){}

    static createFromData(data : any) : Exemplar{
        const exemplar = new Exemplar();
        exemplar.numRegistro = data.numRegistro;        
        exemplar.secao = new Secao(data.secao.id, data.secao.nome);
        exemplar.dataAquisicao = DateTimeUtil.fromAPIDate(data.dataAquisicao);
        exemplar.origem = new Origem(data.origem.id, data.origem.descricao);
        exemplar.situacao = data.situacao;
        exemplar.livro = { id: data.livroId, titulo: data.tituloLivro };
        return exemplar;
    }   

    getDataAquisicaoAsStringDepreciated():string{
        if (this.dataAquisicao){
            let res = '';
            res += this.dataAquisicao.getFullYear();
            res += '-';
            if (this.dataAquisicao.getMonth() < 10){
                res += '0';
            }
            res += this.dataAquisicao.getMonth()+1;
            res += '-';
            if (this.dataAquisicao.getUTCDate() < 10){
                res += '0';
            }
            res += this.dataAquisicao.getUTCDate();
            return res;
        }
        return '';
    }

}