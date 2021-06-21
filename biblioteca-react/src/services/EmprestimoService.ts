import api from './api';
import { Emprestimo } from '../model';

import DateTimeUtil from '../util/DateTimeUtil';

export class EmprestimoService{

    public static async findEmprestimos(somenteAtivos : boolean = false): Promise<Array<Emprestimo>> {
        const url =  somenteAtivos ? 'emprestimos?filter=active' : 'emprestimos';
        const response = await api.get(url);
        return response.data.map((d : any)=>{
            return Emprestimo.createFromData(d);
        });
    }

    public static async findPage(pageNum : number = 1, somenteAtivos: boolean = false, limit : number = 10): Promise<{emprestimos: Array<Emprestimo>, totalPag: number}>{
        const pageIndex = pageNum - 1;
        const url =  somenteAtivos ? `emprestimos?page=${pageIndex}&limit=10&filter=active` : `emprestimos?page=${pageIndex}&limit=10`;
        const response = await api.get(url);
        const total = response.headers['x-total-count'];
        const totalPages = +(total / limit);
        const totalPagesFixed = +(total / limit).toFixed(0);
        const totalPag = totalPagesFixed < totalPages ? totalPagesFixed + 1 : totalPagesFixed;
        const emprestimos = response.data.map((d : any)=>{
            return Emprestimo.createFromData(d);
        });
        return { emprestimos, totalPag }
    }

    public static async insert(emprestimo : Emprestimo) : Promise<{done: boolean, object: any}>{
        try {
            const newEmp = {
                dataHora : DateTimeUtil.toAPIDateTime(emprestimo.dataHora || new Date()),
                exemplar: emprestimo.exemplar?.numRegistro,
                usuario: { id: emprestimo.usuario?.id}
            }
            console.log(newEmp);
            const savedEmp = await api.post('emprestimos', newEmp);
            return {
                done: true,
                object: Emprestimo.createFromData(savedEmp.data)
            };
        } catch (error) {
            let message = 'Erro desconhecido!';
            if (error.response) {
                message = error.response.data.error;
            } 
            return {
                done: false,
                object: { message }
            };
        }
    }

    public static async devolucao(emprestimo : Emprestimo) : Promise<{done: boolean, object: any}>{
        try {
            const updEmp = {
                dataHoraDevolucao : DateTimeUtil.toAPIDateTime(new Date()),
            }
            const savedEmp = await api.put(`emprestimos/${emprestimo.id}`, updEmp);
            return {
                done: true,
                object: Emprestimo.createFromData(savedEmp.data)
            };
        } catch (error) {
            let message = 'Erro desconhecido!';
            if (error.response) {
                message = error.response.data.error;
            } 
            return {
                done: false,
                object: { message }
            };
        }
    }

    public static async renovacao(emprestimo : Emprestimo) : Promise<{done: boolean, object: any}>{
        try {
            const numRenovacoes = emprestimo.numRenovacoes ? emprestimo.numRenovacoes + 1 : 1;
            const updEmp = {
                numRenovacoes,
            }
            const savedEmp = await api.put(`emprestimos/${emprestimo.id}`, updEmp);
            return {
                done: true,
                object: Emprestimo.createFromData(savedEmp.data)
            };
        } catch (error) {
            let message = 'Erro desconhecido!';
            if (error.response) {
                message = error.response.data.error;
            } 
            return {
                done: false,
                object: { message }
            };
        }
    }

    public static async delete(emprestimo : Emprestimo){
        const url = `emprestimos/${emprestimo.id}`;
        try{
            const response = await api.delete(url);
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

}