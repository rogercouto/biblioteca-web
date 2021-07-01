import Api from './api';

import { Assunto } from '../model';

type AssuntosResp = {
    list: Assunto[];
    totalPag: number;
}

export class AssuntoService{

    public static async getList(numPag : number) : Promise<AssuntosResp> {
        const pageIndex = numPag - 1;
        const url = `assuntos?page=${pageIndex}`;
        const response = await Api.get(url);
        const totalAssuntos = response.headers['x-total-count'];
        const limit = 10;
        const totalPag = +(totalAssuntos / limit).toFixed(0);
        return {
            list: response.data.map((d:any)=>{
                return Assunto.createFromData(d);
            }), 
            totalPag
        }
    }

    public static async save(assunto : Assunto): Promise<any>{
        try{
            let response;
            let url;
            if (assunto.id && assunto.id > 0){
                url = `assuntos/${assunto.id}`;
                response = await Api.put(url, assunto);
            }else{
                url = 'assuntos';
                response = await Api.post(url, assunto);
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
            }else{
                return {
                    done: false,
                    errors: ['Erro desconhecido!']
                }
            }
        }
    }

    public static async delete(assunto : Assunto): Promise<any>{
        const url = `assuntos/${assunto.id}`;
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



