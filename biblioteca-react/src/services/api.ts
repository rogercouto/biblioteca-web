import axios, { AxiosResponse } from 'axios';
import Cookies from 'js-cookie';

export default class Api{

    private static startApi(){
        return axios.create({baseURL: 'http://localhost:8080/'});
    }

    private static async getConfig() : Promise<{ headers: any }>{
        const token = await Cookies.get('token');
        if (token){
            return { headers : {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${token}`
            }};  
        }else{
            return { headers : {
                'Content-Type': 'application/json'
            }};
        }
    }

    public static async get(url : string) : Promise<AxiosResponse<any>>{
        const api = this.startApi();
        const config = await this.getConfig();
        return await api.get(url, config);
    }

    public static async post(url : string, data : any) : Promise<AxiosResponse<any>>{
        const api = this.startApi();
        const config = await this.getConfig();
        return await api.post(url, data, config);
    }

    public static async put(url : string, data : any) : Promise<AxiosResponse<any>>{
        const api = this.startApi();
        const config = await this.getConfig();
        return await api.put(url, data, config);
    }

    public static async delete(url : string) : Promise<AxiosResponse<any>>{
        const api = this.startApi();
        const config = await this.getConfig();
        return await api.delete(url, config);
    }

}