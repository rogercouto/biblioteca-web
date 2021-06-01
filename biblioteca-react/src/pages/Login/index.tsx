import { useState } from 'react';
import { useHistory } from 'react-router-dom';

import Cookie from 'js-cookie';

import { TextField, Button } from '@material-ui/core';

import api from '../../services/api';

import Loader from '../../components/Loader';

import './style.css';

export default function LoginPage(){

    const history = useHistory();
    
    const [email, setEmail] = useState('admin@gmail.com');
    const [senha, setSenha] = useState('admin123');
    const [errorMessage, setErrorMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    
    async function _handleSubmit(e : any){
        e.preventDefault();
        try {
            setIsLoading(true);
            const response =  await api.post('auth/signin',{ email, senha});
            const username = response.data.usuario.nome;
            const isGerente = response.data.usuario.gerente;
            const token = response.data.token;
            Cookie.set('username' , username);
            Cookie.set('token', token);
            Cookie.set('isGerente', isGerente);
            setErrorMessage('');
            setIsLoading(false);
            history.push('/');
            window.location.reload();
        } catch (error) {
            if (error.response) {
                setErrorMessage(error.response.data.message);
            }else{
                history.push(
                    '/errors',
                    {error: 'Erro de comunicação com o servidor'},
                )
            } 
            setIsLoading(false);
        }
    }

    return(
        <div className="loginContainer">
            <form onSubmit={_handleSubmit}>
                <TextField 
                    InputProps={{ error: errorMessage !== '' }} 
                    InputLabelProps={{ error: errorMessage !== ''}} 
                    label="E-mail" 
                    variant="outlined" 
                    className="formControl"
                    type="email"
                    required
                    value={email}
                    onChange={(e)=>{setEmail(e.target.value)}}
                />
                <TextField 
                    InputProps={{ error: errorMessage !== '' }} 
                    InputLabelProps={{ error: errorMessage !== ''}} 
                    label="Senha" 
                    variant="outlined" 
                    type="password" 
                    className="formControl"
                    value={senha}
                    onChange={(e)=>{setSenha(e.target.value)}}
                />
                <span className="errorSpan">{errorMessage}</span>  
                <Button variant="contained" type="submit">
                    Entrar
                </Button>
            </form>
            <Loader open={isLoading} />
        </div>
    );
}