
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
import AssignmentReturnIcon from '@material-ui/icons/AssignmentReturn';
import HistoryIcon from '@material-ui/icons/History';
import MonetizationOnIcon from '@material-ui/icons/MonetizationOn';

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

    const logout = () => {
        Cookies.remove('username');
        Cookies.remove('token');
        Cookies.remove('isGerente');
        setUsername('');
    };

    const renderUserButton = () => {
        if (username && username !== ''){
            return (
                <div>
                    <FaceIcon />
                    <span>{username}</span>
                    <a onClick={logout} href="/"><ExitToAppIcon /> Sair</a>
                </div>
            );
        }else{
            return (<a href="/login"><AccountCircleIcon /> Entrar</a>);
        }
    };

    const renderGerenteLinks = () => {
        if (canEdit){
            return (
                <Fragment>
                    <div className="dropdown">
                        <button className="dropbtn"><CompareArrowsIcon />Movimentação</button>
                        <div className="dropdown-content">
                            <a href="/emprestimos"><AssignmentReturnIcon className="flipH" />Empréstimos</a>
                            <a href="/reservas"><HistoryIcon />Reservas</a>
                            <a href="/pendencias"><MonetizationOnIcon />Pendências</a>
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
    };

    const renderUserLinks = () => {
        if (!canEdit && username && username.trim().length > 0){
            return(
                <div className="dropdown">
                    <button className="dropbtn"><CompareArrowsIcon />Movimentação</button>
                    <div className="dropdown-content">
                        <a href="/emprestimos"><AssignmentReturnIcon className="flipH" />Empréstimos</a>
                        <a href="/reservas"><HistoryIcon />Reservas</a>
                        <a href="/pendencias"><MonetizationOnIcon />Pendências</a>
                    </div>
                </div>
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
                    {renderUserLinks()}
                    {renderGerenteLinks()}
                    <li className="separator">
                    </li>
                    <li>
                        {renderUserButton()}
                    </li>
                </ul>
            </div>
        </header>
    );
}