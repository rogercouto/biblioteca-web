import { Secao } from './Secao';
import { Origem } from './Origem';

export class Exemplar{

    constructor(
        public numRegistro?:number,
        public secao?: Secao,
        public dataAquisicao?: Date,
        public origem?: Origem,
        public situacao?: string,
        public fixo?: boolean,
        public livro?: { id: number}
    ){}

    static createExemplar(data : any) : Exemplar{
        const exemplar = new Exemplar();
        exemplar.numRegistro = data.numRegistro;        
        exemplar.secao = new Secao(data.secao.id, data.secao.nome);
        exemplar.dataAquisicao = this.getDataAquisicao(data.dataAquisicao);
        exemplar.origem = new Origem(data.origem.id, data.origem.descricao);
        exemplar.situacao = data.situacao;
        return exemplar;
    }

    public static getDataAquisicao(strDate : string): Date{
        const year = +strDate.substr(0, 4);
        const month = +strDate.substr(5, 2);
        const day = +strDate.substr(8, 2);
        return new Date(year, month-1, day);
    }

    getDataAquisicaoAsString():string{
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