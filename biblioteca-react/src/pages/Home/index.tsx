import { Fragment, useState, useEffect } from 'react';
import Cookie from 'js-cookie';

import api from '../../services/api';

export default function HomePage(){

    const [username, setUsername] = useState('');
    const [token, setToken] = useState('');
    const [totalLivros, setTotalLivros] = useState(0);
    const [totalExemplares, setTotalExemplares] = useState(0);
    
    useEffect(()=>{
        const u = Cookie.get('username');
        const t = Cookie.get('token');
        
        async function fetchData(){
            setUsername(u ? u : '');
            setToken(t ? t : '');
            if (token && token !== ''){
                let response = await api.get('livros?page=0');
                setTotalLivros(response.headers['x-total-count']);
                response = await api.get('exemplares?page=0');
                setTotalExemplares(response.headers['x-total-count']);
            }
        }
        fetchData();
    },[username, token, totalLivros, totalExemplares]);

    function renderVisitante(){
        return (
            <h3>Bem vindo ao sistema</h3>
        );
      }
    
    function renderUsuario(){
        return (
            <Fragment>
                <p>Bem vindo, <strong>{username}</strong></p><br />
                <h3>Estatísticas:</h3>
                <p>Total de livros: <strong>{totalLivros}</strong></p>
                <p>Total de exemplares: <strong>{totalExemplares}</strong></p>
            </Fragment>
        );
    }

    return(
        <div className="content">
            {(token && token !== '') ? renderUsuario() : renderVisitante()}
        </div>
    );
}