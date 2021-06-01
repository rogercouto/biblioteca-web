import api from './api';

import { Exemplar, Secao, Origem } from '../model';

class ExemplarService{

    public static async findExemplares(livroId : number): Promise<Array<Exemplar>> {
        const url = `exemplares?livroId=${livroId}`;
        const response = await api.get(url);
        return response.data.map((d : any)=>{
            return Exemplar.createExemplar(d);
        });
    }

    public static async findSecoes(): Promise<Array<Secao>>{
        const url = 'secoes?page=0&limit=100000';
        const response = await api.get(url);
        return response.data.map((d : any)=>{
            return new Secao(d.id, d.nome);
        });
    }

    public static async findOrigens(): Promise<Array<Origem>>{
        const url = 'origens?page=0&limit=100000';
        const response = await api.get(url);
        return response.data.map((d : any)=>{
            return new Origem(d.id, d.descricao);
        });
    }

    public static async insert(exemplar : Exemplar): Promise<any>{
        const url = 'exemplares';
        try{
            const response = await api.post(url, exemplar);
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
    
    public static async update(exemplar : Exemplar): Promise<any>{
        const url = `exemplares/${exemplar.numRegistro}`;
        try{
            const response = await api.put(url, exemplar);
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

    public static async delete(exemplar : Exemplar) : Promise<any>{
        const url = `exemplares/${exemplar.numRegistro}`;
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
                        message: 'Exemplar em uso, impossível remover!'
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

export default ExemplarService;
