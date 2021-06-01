import api from './api';

import { Editora } from '../model';

type EditorasResp = {
    list: Editora[];
    totalPag: number;
}

export class EditoraService{

    public static async getList(numPag : number) : Promise<EditorasResp> {
        const pageIndex = numPag - 1;
        const url = `editoras?page=${pageIndex}`;
        const response = await api.get(url);
        const totalEditoras = response.headers['x-total-count'];
        const limit = 10;
        const totalPag = +(totalEditoras / limit).toFixed(0);
        return {
            list: response.data.map((d:any)=>{
                return new Editora(d.id, d.nome);
            }), 
            totalPag
        }
    }

    public static async save(editora : Editora): Promise<any>{
        try{
            let response;
            let url;
            if (editora.id && editora.id > 0){
                url = `editoras/${editora.id}`;
                response = await api.put(url, editora);
            }else{
                url = 'editoras';
                response = await api.post(url, editora);
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

    public static async delete(editora : Editora): Promise<any>{
        const url = `editoras/${editora.id}`;
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
                        message: 'Editora em uso, impossível remover!'
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



