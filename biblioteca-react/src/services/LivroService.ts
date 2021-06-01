import api from './api';

import { Assunto, Livro } from '../model';

type LivrosResp = {
    list: Livro[];
    totalPag: number;
}

export class LivroService{

    public static async findLivros(numPag : number, textoBusca : string = '') : Promise<LivrosResp> {
        const pageIndex = numPag - 1;
        let url;
        if (textoBusca !== ''){
            url = `livros?page=${pageIndex}&find=${textoBusca}`;
        }else{
            url = `livros?page=${pageIndex}`;
        }
        const response = await api.get(url);
        const totalLivros = response.headers['x-total-count'];
        const limit = 10;
        const totalPag = +(totalLivros / limit).toFixed(0);
        return {
            list: Livro.createLivros(response), 
            totalPag
        }
    }

    public static async findAllNomesEditoras(texto : string){
        const url = `editoras?find=${texto}&limit=100000`;
        const response = await api.get(url);
        const editoras = response.data.map((d : any) => {
            return d.nome
        });
        return editoras;
    }

    public static async findAllDescrsAssuntos(){
        const url = 'assuntos?limit=100000';
        const response = await api.get(url);
        const assuntos = response.data.map((d : any) => {
            return d.descricao
        });
        return assuntos;
    }

    public static async findAllAssuntos(): Promise<Array<Assunto>>{
        const url = 'assuntos?limit=100000';
        const response = await api.get(url);
        const assuntos: Array<Assunto> = response.data.map((d : any) => {
            return {
                id: d.id, descricao: d.descricao, cores: d.cores, cdu: d.cdu
            }
        });
        return assuntos;
    }
    
    
    public static async findAllDescrsCategorias(texto : string){
        const url = `categorias?find=${texto}&limit=100000`;
        const response = await api.get(url);
        const categorias = response.data.map((d : any) => {
            return d.descricao
        });
        return categorias;
    }
    
    public static async findNomesAutores(texto : string){
        const url = `autores?find=${texto}&limit=100000`;
        const response = await api.get(url);
        const autores = response.data.map((d : any) => {
            return `${d.sobrenome.toUpperCase()}, ${d.nome}`;
        });
        return autores;
    }
    
    public static async insertLivro(livro : Livro): Promise<any>{
        const url = 'livros';
        try{
            const response = await api.post(url, livro);
            return response.data;
        }catch(error){
            if (error.response) {
                console.log(error.response.data.errors);
            } 
        }
    }
    
    public static async updateLivro(livro : Livro): Promise<any>{
        const url = `livros/${livro.id}`;
        try{
            const response = await api.put(url, livro);
            return response.data;
        }catch(error){
            if (error.response) {
                console.log(error.response.data.errors);
            } 
        }
    }
    
}



