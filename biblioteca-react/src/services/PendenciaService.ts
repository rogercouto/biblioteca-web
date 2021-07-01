import { Pendencia } from '../model';
import DateTimeUtil from '../util/DateTimeUtil';
import Api from './api';

export class PendenciaService{

    public static async findPendencias(somenteAtivas : boolean = false): Promise<Pendencia>{
        const url =  somenteAtivas ? 'pendencias/adm?filter=active' : 'pendencias';
        const response = await Api.get(url);
        return response.data.map((d : any)=>{
            return Pendencia.createFromData(d);
        });
    }

    public static async findPage(pageNum : number = 1, somenteAtivos: boolean = false, limit : number = 10): Promise<{pendencias: Array<Pendencia>, totalPag: number}>{
        const pageIndex = pageNum - 1;
        const url =  somenteAtivos ? `pendencias/adm?page=${pageIndex}&limit=10&filter=active` : `pendencias/adm?page=${pageIndex}&limit=10`;
        const response = await Api.get(url);
        const total = response.headers['x-total-count'];
        const totalPages = +(total / limit);
        const totalPagesFixed = +(total / limit).toFixed(0);
        const totalPag = totalPagesFixed < totalPages ? totalPagesFixed + 1 : totalPagesFixed;
        const pendencias = response.data.map((d : any)=>{
            return Pendencia.createFromData(d);
        });
        return { pendencias, totalPag }
    }

    public static async findUsuarioPage(usuarioId : number, pageNum : number = 1, somenteAtivos: boolean = false, limit : number = 10): Promise<{pendencias: Array<Pendencia>, totalPag: number}>{
        const pageIndex = pageNum - 1;
        const url =  somenteAtivos ? `pendencias/usuario/${usuarioId}?page=${pageIndex}&limit=10&filter=active` : `pendencias/usuario/${usuarioId}?page=${pageIndex}&limit=10`;
        const response = await Api.get(url);
        const total = response.headers['x-total-count'];
        const totalPages = +(total / limit);
        const totalPagesFixed = +(total / limit).toFixed(0);
        const totalPag = totalPagesFixed < totalPages ? totalPagesFixed + 1 : totalPagesFixed;
        const pendencias = response.data.map((d : any)=>{
            return Pendencia.createFromData(d);
        });
        return { pendencias, totalPag }
    }

    public static async alteraPagamento(pendencia : Pendencia): Promise<{done: boolean, object: any}>{
        try {
            const data = {
                dataHoraPagamento: pendencia.foiPaga() ? null : DateTimeUtil.toAPIDateTime(new Date())
            }
            const url = `pendencias/adm/${pendencia.id}`;
            const response = await Api.put(url, data);
            return {
                done: true,
                object: Pendencia.createFromData(response.data)
            }
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

    public static async delete(pendencia : Pendencia): Promise<{done: boolean, object: any}>{
        try {
            const url = `pendencias/adm/${pendencia.id}`;
            await Api.delete(url);
            return {
                done: true,
                object: {}
            }
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


} 