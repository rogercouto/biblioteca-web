import Api from './api';

import { Secao } from '../model';

type SecoesResp = {
    list: Secao[];
    totalPag: number;
}

export class SecaoService{

    public static async getList(numPag : number) : Promise<SecoesResp> {
        const pageIndex = numPag - 1;
        const url = `secoes?page=${pageIndex}`;
        const response = await Api.get(url);
        const totalSecoes = response.headers['x-total-count'];
        const limit = 10;
        const totalPag = +(totalSecoes / limit).toFixed(0);
        return {
            list: response.data.map((d:any)=>{
                return new Secao(d.id, d.nome);
            }), 
            totalPag
        }
    }

    public static async save(secao : Secao): Promise<any>{
        try{
            let response;
            let url;
            if (secao.id && secao.id > 0){
                url = `secoes/${secao.id}`;
                response = await Api.put(url, secao);
            }else{
                url = 'secoes';
                response = await Api.post(url, secao);
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

    public static async delete(secao : Secao): Promise<any>{
        const url = `secoes/${secao.id}`;
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
                        message: 'Secao em uso, impossível remover!'
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



