import Api from './api';

import { Exemplar, Secao, Origem } from '../model';

import DateTimeUtil from '../util/DateTimeUtil';

export class ExemplarService{

    public static async findById(numReg : number): Promise<Exemplar | null>{
        const url = `exemplares/${numReg}`;
        const response = await Api.get(url);
        if (response.data){
            return Exemplar.createFromData(response.data);
        }
        return null;
    }
    
    public static async findExemplares(livroId : number): Promise<Array<Exemplar>> {
        const url = `exemplares?livroId=${livroId}`;
        const response = await Api.get(url);
        return response.data.map((d : any)=>{
            return Exemplar.createFromData(d);
        });
    }

    public static async findSecoes(): Promise<Array<Secao>>{
        const url = 'secoes?page=0&limit=100000';
        const response = await Api.get(url);
        return response.data.map((d : any)=>{
            return new Secao(d.id, d.nome);
        });
    }

    public static async findOrigens(): Promise<Array<Origem>>{
        const url = 'origens?page=0&limit=100000';
        const response = await Api.get(url);
        return response.data.map((d : any)=>{
            return new Origem(d.id, d.descricao);
        });
    }

    public static async insert(exemplar : Exemplar): Promise<any>{
        const url = 'exemplares';
        try{
            const data = {
                numRegistro: exemplar.numRegistro,
                livro: { id: exemplar.livro?.id },
                secao: { id: exemplar.secao?.id },
                dataAquisicao: DateTimeUtil.toAPIDate( exemplar.dataAquisicao || new Date() ),
                origem: { id: exemplar.origem?.id}
            };
            const response = await Api.post(url, data);
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
            const data = {
                numRegistro: exemplar.numRegistro,
                livro: { id: exemplar.livro?.id },
                secao: { id: exemplar.secao?.id },
                dataAquisicao: DateTimeUtil.toAPIDate( exemplar.dataAquisicao || new Date() ),
                origem: { id: exemplar.origem?.id}
            };
            const response = await Api.put(url, data);
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

};
