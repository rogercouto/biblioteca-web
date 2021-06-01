
import { Fragment, useState, useEffect } from 'react';
import Cookies from 'js-cookie';

import HomeIcon from '@material-ui/icons/Home';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import FaceIcon from '@material-ui/icons/Face';
import CollectionsBookmarkIcon from '@material-ui/icons/CollectionsBookmark';
import PersonPinIcon from '@material-ui/icons/PersonPin';
import GroupIcon from '@material-ui/icons/Group';
import BusinessIcon from '@material-ui/icons/Business';
import CategoryIcon from '@material-ui/icons/Category';
import GetAppIcon from '@material-ui/icons/GetApp';
import BookmarksIcon from '@material-ui/icons/Bookmarks';
import SettingsEthernetIcon from '@material-ui/icons/SettingsEthernet';
import CompareArrowsIcon from '@material-ui/icons/CompareArrows';
import ArrowForwardIcon from '@material-ui/icons/ArrowForward';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import HistoryIcon from '@material-ui/icons/History';

import './styles.css';

export default function Header(){

    const canEdit : boolean = Cookies.get('isGerente') === 'true';
    
    const [username, setUsername] = useState('');

    useEffect(()=>{
        async function getUsername(){
            const tmp = await Cookies.get('username');
            setUsername(tmp || '');
        }
        getUsername();
    },[username]);

    function _logout(){
        Cookies.remove('username');
        Cookies.remove('token');
        Cookies.remove('isGerente');
        setUsername('');
    }

    function _renderUserButton(){
        if (username && username !== ''){
            return (
                <div>
                    <FaceIcon />
                    <span>{username}</span>
                    <a onClick={_logout} href="/"><ExitToAppIcon /> Sair</a>
                </div>
            );
        }else{
            return (<a href="/login"><AccountCircleIcon /> Entrar</a>);
        }
    }

    function _renderGerenteLinks(){
        if (canEdit){
            return (
                <Fragment>
                    <div className="dropdown">
                        <button className="dropbtn"><CompareArrowsIcon />Movimentação</button>
                        <div className="dropdown-content">
                            <a href="/"><ArrowForwardIcon />Empréstimos</a>
                            <a href="/"><ArrowBackIcon />Devoluções</a>
                            <a href="/"><HistoryIcon />Reservas</a>
                        </div>
                    </div>
                    <div className="dropdown">
                        <button className="dropbtn"><SettingsEthernetIcon />Gerenciar</button>
                        <div className="dropdown-content">
                            <a href="/assuntos"><CollectionsBookmarkIcon />Assuntos</a>
                            <a href="/autores"><PersonPinIcon />Autores</a>
                            <a href="/editoras"><BusinessIcon />Editoras</a>
                            <a href="/categorias"><CategoryIcon />Categorias</a>
                            <a href="/secoes"><BookmarksIcon />Seções</a>
                            <a href="/origens"><GetAppIcon />Origens</a>
                            <a href="/usuarios"><GroupIcon />Usuários</a>
                        </div>
                    </div>
                </Fragment>
            );
        }
    }

    return(
        <header>
            <div className="headerContainer">
                <h2>
                    Sistema de Gerenciamento de Biblioteca
                </h2>
                <img src="/logo.png" alt="Logotipo"/>
            </div>
            <div className="toolbar">
                <ul>
                    <li>
                        <a href="/"><HomeIcon />Início</a>
                    </li>
                    <li>
                        <a href="/livros"><MenuBookIcon />Livros</a>
                    </li>
                    {_renderGerenteLinks()}
                    <li className="separator">
                    </li>
                    <li>
                        {_renderUserButton()}
                    </li>
                </ul>
            </div>
        </header>
    );
}