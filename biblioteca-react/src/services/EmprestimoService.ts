import Api from './api';
import { Emprestimo } from '../model';

import DateTimeUtil from '../util/DateTimeUtil';

export class EmprestimoService{

    public static async findEmprestimos(somenteAtivos : boolean = false): Promise<Array<Emprestimo>> {
        const url =  somenteAtivos ? 'emprestimos?filter=active' : 'emprestimos';
        const response = await Api.get(url);
        return response.data.map((d : any)=>{
            return Emprestimo.createFromData(d);
        });
    }

    public static async findPage(pageNum : number = 1, somenteAtivos: boolean = false, limit : number = 10): Promise<{emprestimos: Array<Emprestimo>, totalPag: number}>{
        const pageIndex = pageNum - 1;
        const url =  somenteAtivos ? `emprestimos?page=${pageIndex}&limit=10&filter=active` : `emprestimos?page=${pageIndex}&limit=10`;
        const response = await Api.get(url);
        const total = response.headers['x-total-count'];
        const totalPages = +(total / limit);
        const totalPagesFixed = +(total / limit).toFixed(0);
        const totalPag = totalPagesFixed < totalPages ? totalPagesFixed + 1 : totalPagesFixed;
        const emprestimos = response.data.map((d : any)=>{
            return Emprestimo.createFromData(d);
        });
        return { emprestimos, totalPag }
    }

    public static async findUsuarioPage(usuarioId : number, pageNum : number = 1, somenteAtivos: boolean = false, limit : number = 10): Promise<{emprestimos: Array<Emprestimo>, totalPag: number}>{
        const pageIndex = pageNum - 1;
        const url =  somenteAtivos ? `emprestimos/usuario/${usuarioId}?page=${pageIndex}&limit=10&filter=active` : `emprestimos/usuario/${usuarioId}?page=${pageIndex}&limit=10`;
        const response = await Api.get(url);
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
            const savedEmp = await Api.post('emprestimos', newEmp);
            return {
                done: true,
                object: Emprestimo.createFromData(savedEmp.data)
            };
        } catch (error) {
            let message = 'Erro desconhecido!';
            if (error.response) {
                if (error.response.data.error){
                    message = error.response.data.error;
                }
                if (error.response.data.errors.length > 0){
                    message = error.response.data.errors.join('\r');
                }
            } 
            return {
                done: false,
                object: { message }
            };
        }
    }

    public static async devolucao(emprestimo : Emprestimo) : Promise<{done: boolean, object: any}>{
        try {
            const response = await Api.put(`emprestimos/devolucao/${emprestimo.id}`, {});
            return {
                done: true,
                object: { 
                    emprestimo: Emprestimo.createFromData(response.data.emprestimo),
                    message: response.data.message
                }
            };
        } catch (error) {
            let message = 'Erro desconhecido!';
            if (error.response) {
                if (error.response.data.error){
                    message = error.response.data.error;
                }
                if (error.response.data.errors.length > 0){
                    message = error.response.data.errors.join('\r');
                }
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
            const savedEmp = await Api.put(`emprestimos/${emprestimo.id}`, updEmp);
            return {
                done: true,
                object: Emprestimo.createFromData(savedEmp.data)
            };
        } catch (error) {
            let message = 'Erro desconhecido!';
            if (error.response) {
                if (error.response.data.error){
                    message = error.response.data.error;
                }
                if (error.response.data.errors.length > 0){
                    message = error.response.data.errors.join('\r');
                }
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
            const response = await Api.delete(url);
            return {
                done: true,
                data: response.data
            };
        }catch(error){
            let message = 'Erro desconhecido!';
            if (error.response) {
                if (error.response.data.error){
                    message = error.response.data.error;
                }
                if (error.response.data.errors.length > 0){
                    message = error.response.data.errors.join('\r');
                }
            } 
            return {
                done: false,
                object: { message }
            };
        }
    }

}