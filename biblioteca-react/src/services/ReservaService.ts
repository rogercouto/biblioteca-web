import { Exemplar, Reserva, Usuario } from '../model';
import DateTimeUtil from '../util/DateTimeUtil';
import Api from './api';

export class ReservaService{

    public static async find(somenteAtivos : boolean = false): Promise<Array<Reserva>> {
        const url =  somenteAtivos ? 'reserva?filter=active' : 'reserva';
        const response = await Api.get(url);
        return response.data.map((d : any)=>{
            return Reserva.createFromData(d);
        });
    }

    public static async findPage(pageNum : number = 1, somenteAtivos: boolean = false, limit : number = 10): Promise<{reservas: Array<Reserva>, totalPag: number}>{
        const pageIndex = pageNum - 1;
        const url =  somenteAtivos ? `reservas?page=${pageIndex}&limit=10&filter=active` : `reservas?page=${pageIndex}&limit=10`;
        const response = await Api.get(url);
        const total = response.headers['x-total-count'];
        const totalPages = +(total / limit);
        const totalPagesFixed = +(total / limit).toFixed(0);
        const totalPag = totalPagesFixed < totalPages ? totalPagesFixed + 1 : totalPagesFixed;
        const reservas = response.data.map((d : any)=>{
            return Reserva.createFromData(d);
        });
        return { reservas, totalPag }
    }

    public static async insert( reserva : Reserva ) : Promise<{ done: boolean, object: any }> {
        try {
            const newRes = {
                exemplar: { numRegistro: reserva.exemplar?.numRegistro},
                usuario: {id: reserva.usuario?.id },
                dataHora: DateTimeUtil.toAPIDateTime( reserva.dataHora )
            }
            const response = await Api.post('reservas', newRes);
            return {
                done: true,
                object: Reserva.createFromData(response.data)
            };
        } catch (error) {
            console.log(error);
            let message = 'Erro desconhecido!';
            if (error.response) {
                if (error.response.data.error){
                    message = error.response.data.error;
                }else if (error.response.data.errors && error.response.data.errors.length > 0){
                    message = error.response.data.errors.join('\r');
                }
            } 
            return {
                done: false,
                object: { message }
            };
        }
    }

    public static async delete(reserva : Reserva){
        const url = `reservas/${reserva.id}`;
        try{
            const response = await Api.delete(url);
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

    public static async findByUsuarioId( usuarioId : number ) : Promise<Array<Reserva>>{
        const url = `reservas/usuario/${usuarioId}`;
        const response = await Api.get(url);
        return response.data.map((d : any)=>{
            return Reserva.createFromData(d);
        });
    }

    public static async verificaReserva( exemplar: Exemplar, usuario: Usuario ) : Promise<boolean>{
        if (usuario.id && exemplar.situacao === 'Reservado'){
            const array = await this.findByUsuarioId( usuario.id );
            const reservas = array.filter((r:Reserva)=>{
                if (r.exemplar && r.exemplar.numRegistro === exemplar.numRegistro){
                    return r.ativa;
                }
                return false;
            })
            return reservas.length === 1;
        }
        return true;
    }

}