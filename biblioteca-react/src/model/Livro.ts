import { AxiosResponse } from 'axios';
import { Assunto }  from './Assunto';
import { Autor }  from './Autor';
import { Categoria }  from './Categoria';
import { Editora }  from './Editora';

export class Livro{

    constructor(
        public id?: number,
        public titulo?: string,
        public resumo?: string | null,
        public isbn?: number | null,
        public cutter?: string | null,
        public editora?: Editora,
        public edicao?: string | null,
        public volume?: string | null,
        public numPaginas?: number | null,
        public assunto?: Assunto,
        public anoPublicacao?: number | null,
        public autores?: Autor[],
        public categorias?: Categoria[]
    ){}

    private static _createLivro(data: any):Livro{
        const editora = new Editora();
        if (data.editora){
            editora.id = data.editora.id;
            editora.nome = data.editora.nome;
        }
        const assunto = new Assunto();
        if (data.assunto){
            assunto.id = data.assunto.id;
            assunto.descricao = data.assunto.descricao;
            assunto.cores = data.assunto.cores;
            assunto.cdu = data.assunto.cdu;
        }
        const autores = data.autores.map((a : any) =>{
            return new Autor(a.id, a.nome, a.sobrenome, a.info);
        });
        const categorias = data.categorias.map((c : any) => {
            return new Categoria(c.id, c.descricao);
        });
        return new Livro(
            data.id, data.titulo, data.resumo, data.isbn, data.cutter,
            editora, data.edicao, data.volume, data.numPaginas, assunto,
            data.anoPublicacao, autores, categorias
        );
    }

    public static createLivroFromState(state : any):Livro{
        if (!state)
            return new Livro();
        return this._createLivro(state);
    }

    public static createLivro(response: AxiosResponse<any>):Livro{
        const data = response.data;
        return this._createLivro(data);
    }

    public static createLivros(response: AxiosResponse<any>):Livro[]{
        const data = response.data;
        return data.map((d : any) => {
            return this._createLivro(d);
        });
    }

    public getNomesAutores():string{
        if (this.autores){
            return this.autores.map(a => {
                return `${a.sobrenome?.toUpperCase()}, ${a.nome}`          
            }).join('; ');
        }
        return '';
    }

    public getArrayNomesAutores(): Array<string>{
        if (this.autores){
            return this.autores.map(a => {
                return `${a.sobrenome?.toUpperCase()}, ${a.nome}`          
            });
        }
        return new Array<string>();
    }

}