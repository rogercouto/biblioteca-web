import Api from './api';

import { Origem } from '../model';

type OrigensResp = {
    list: Origem[];
    totalPag: number;
}

export class OrigemService{

    public static async getList(numPag : number) : Promise<OrigensResp> {
        const pageIndex = numPag - 1;
        const url = `origens?page=${pageIndex}`;
        const response = await Api.get(url);
        const totalOrigens = response.headers['x-total-count'];
        const limit = 10;
        const totalPag = +(totalOrigens / limit).toFixed(0);
        return {
            list: response.data.map((d:any)=>{
                return new Origem(d.id, d.descricao);
            }), 
            totalPag
        }
    }

    public static async save(categoria : Origem): Promise<any>{
        try{
            let response;
            let url;
            if (categoria.id && categoria.id > 0){
                url = `origens/${categoria.id}`;
                response = await Api.put(url, categoria);
            }else{
                url = 'origens';
                response = await Api.post(url, categoria);
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

    public static async delete(categoria : Origem): Promise<any>{
        const url = `origens/${categoria.id}`;
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
                        message: 'Origem em uso, impossível remover!'
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



