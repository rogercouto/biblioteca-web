import api from './api';

import { Usuario } from '../model';

type UsuariosResp = {
    list: Usuario[];
    totalPag: number;
}

export class UsuarioService{

    public static async getList(numPag : number) : Promise<UsuariosResp> {
        const pageIndex = numPag - 1;
        const url = `auth/users?page=${pageIndex}`;
        const response = await api.get(url);
        const totalUsuarios = response.headers['x-total-count'];
        const limit = 10;
        const totalPag = +(totalUsuarios / limit).toFixed(0);
        return {
            list: response.data.map((d:any)=>{
                return Usuario.createFromData(d);
            }), 
            totalPag
        }
    }

    public static async save(usuario : Usuario): Promise<any>{
        try{
            let response;
            let url;
            if (usuario.id && usuario.id > 0){
                url = `auth/users/${usuario.id}`;
                response = await api.put(url, usuario);
            }else{
                url = 'auth/users';
                console.log(usuario);
                response = await api.post(url, usuario);
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

    public static async delete(usuario : Usuario){
        const url = `auth/users/${usuario.id}`;
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



