import { useState } from 'react';
import { useHistory } from 'react-router-dom';

import Cookie from 'js-cookie';

import { TextField, Button, Paper } from '@material-ui/core';

import api from '../../services/api';

import Loader from '../../components/loader';

import './style.css';

export default function LoginPage(){

    const history = useHistory();
    
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [isLoading, setIsLoading] = useState(false);
    
    async function _handleSubmit(e : any){
        e.preventDefault();
        try {
            setIsLoading(true);
            const response =  await api.post('auth/signin',{ email, senha});
            Cookie.set('userId', response.data.usuario.id)
            Cookie.set('username' , response.data.usuario.nome);
            Cookie.set('token', response.data.token);
            Cookie.set('isGerente', response.data.usuario.gerente);
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
        <Paper className="loginContainer">
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
        </Paper>
    );
}