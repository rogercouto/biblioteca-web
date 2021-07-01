import Api from './api';

import { Autor } from '../model';

type AutorsResp = {
    list: Autor[];
    totalPag: number;
}

export class AutorService{

    public static async getList(numPag : number) : Promise<AutorsResp> {
        const pageIndex = numPag - 1;
        const url = `autores?page=${pageIndex}`;
        const response = await Api.get(url);
        const totalAutors = response.headers['x-total-count'];
        const limit = 10;
        const totalPag = +(totalAutors / limit).toFixed(0);
        return {
            list: response.data.map((d:any)=>{
                return Autor.createFromData(d);
            }), 
            totalPag
        }
    }

    public static async save(autor : Autor): Promise<any>{
        try{
            let response;
            let url;
            if (autor.id && autor.id > 0){
                url = `autores/${autor.id}`;
                response = await Api.put(url, autor);
            }else{
                url = 'autores';
                response = await Api.post(url, autor);
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

    public static async delete(autor : Autor): Promise<any>{
        const url = `autores/${autor.id}`;
        try{
            const response = await Api.delete(url);
            return {
                done: true,
                data: response.data
            };
        }catch(error){
            if (error.response) {
                if (error.response.status === 400){
                    return {
                        done: false,
                        message: 'Assunto em uso, impossível remover!'
                    }
                }
                return {
                    done: false,
                    message: error.response.data.message
                }
            } else{
                return {
                    done: false, 
                    message: 'Erro desconhecido!'
                }
            }
        }
    }
    
}



