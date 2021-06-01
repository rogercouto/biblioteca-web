import api from './api';

import { Categoria } from '../model';

type CategoriasResp = {
    list: Categoria[];
    totalPag: number;
}

export class CategoriaService{

    public static async getList(numPag : number) : Promise<CategoriasResp> {
        const pageIndex = numPag - 1;
        const url = `categorias?page=${pageIndex}`;
        const response = await api.get(url);
        const totalCategorias = response.headers['x-total-count'];
        const limit = 10;
        const totalPag = +(totalCategorias / limit).toFixed(0);
        return {
            list: response.data.map((d:any)=>{
                return new Categoria(d.id, d.descricao);
            }), 
            totalPag
        }
    }

    public static async save(categoria : Categoria): Promise<any>{
        try{
            let response;
            let url;
            if (categoria.id && categoria.id > 0){
                url = `categorias/${categoria.id}`;
                response = await api.put(url, categoria);
            }else{
                url = 'categorias';
                response = await api.post(url, categoria);
            }
            return {
                done: true,
                data: response.data
            };
        }catch(error){
            if (error.response) {
                return {
                    done: false,
                    errors: error.response.data.errors
                }
            } else{
                return {
                    done: false, 
                    errors: ['Erro desconhecido!']
                }
            }
        }
    }

    public static async delete(categoria : Categoria): Promise<any>{
        const url = `categorias/${categoria.id}`;
        try{
            const response = await api.delete(url);
            return {
                done: true,
                data: response.data
            };
        }catch(error){
            if (error.response) {
                if (error.response.status === 400){
                    return {
                        done: false,
                        message: 'Categoria em uso, impossível remover!'
                    }
                }
                return {
                    done: false,
                    message: error.response.data.message
                }
            }else{
                return {
                    done: false,
                    message: 'Erro desconhecido!'
                }
            }
        }
    }
    
}



