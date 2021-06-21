import { Exemplar, Reserva, Usuario } from '../model';
import DateTimeUtil from '../util/DateTimeUtil';
import api from './api';

export class ReservaService{

    public static async insert( reserva : Reserva ) : Promise<{ done: boolean, object: any }> {
        try {
            const newRes = {
                exemplar: { numRegistro: reserva.exemplar?.numRegistro},
                usuario: {id: reserva.usuario?.id },
                dataHora: DateTimeUtil.toAPIDateTime( reserva.dataHora )
            }
            console.log(newRes);
            const response = await api.post('reservas', newRes);
            return {
                done: true,
                object: Reserva.createFromData(response.data)
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

    public static async findByUsuarioId( usuarioId : number ) : Promise<Array<Reserva>>{
        const url = `reservas/usuario/${usuarioId}`;
        const response = await api.get(url);
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