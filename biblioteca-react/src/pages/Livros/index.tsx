import { Fragment, useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import Cookies from 'js-cookie';

import { TextField, Button, Snackbar, Paper } from '@material-ui/core';
import Pagination from '@material-ui/lab/Pagination';
import MuiAlert, { AlertProps } from '@material-ui/lab/Alert';

import SearchIcon from '@material-ui/icons/Search';
import VisibilityIcon from '@material-ui/icons/Visibility';
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';

import { BreadcrumbsMaker } from '../../components/breadcrumbs'
import { Livro } from '../../model';
import { LivroService } from '../../services';

import './style.css';

export default function LivrosPage( props: any ){

    const canEdit = Cookies.get('isGerente');

    const state = props.location.state;
    
    const history = useHistory();

    const [textoBusca, setTextoBusca] = useState('');
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);
    const [livros, setLivros] = useState(new Array<Livro>());

    const [confOpen, setConfOpen] = useState(false);
    const [confMessage, setConfMessage] = useState('');
    
    const bcMaker = new BreadcrumbsMaker('Livros');

    useEffect(()=>{
        if (state && state.message && state.message !== ''){
            setConfOpen(true);
            setConfMessage(state.message);
        }
    },[state]); 

    function Alert(props: AlertProps) {
        return <MuiAlert elevation={6} variant="filled" {...props} />;
    }

    const handleConfClose = (event: React.SyntheticEvent | React.MouseEvent, reason?: string) => {
        if (reason === 'clickaway') {
            return;
        }
        setConfMessage('');
        setConfOpen(false);
    };
    
    async function _handleFind(){
        setPagNum(1);
        const livrosResp = await LivroService.findLivros(pagNum, textoBusca);
        setLivros(livrosResp.list);
        setTotalPag(livrosResp.totalPag);
    }

    async function _handlePageChange(event: React.ChangeEvent<unknown>, value: number){
        setPagNum(value);
        const livrosResp = await LivroService.findLivros(pagNum, textoBusca);
        setLivros(livrosResp.list);
        setTotalPag(livrosResp.totalPag);
        window.scrollTo({top: 0, behavior: 'smooth'});
    }

    function _handleShow(livro : Livro){
        history.push({
            pathname: '/livros/show',
            state: livro
        });
    }

    const insert = () => {
        history.push('/livros/insert');
    }

    function _renderCreateButtonIfManager(){
        if (canEdit){
            return (
                <Button variant="contained" onClick={insert}>
                    <AddCircleOutlineIcon />
                    Inserir
                </Button>
            );
        }else{
            return (<Fragment />);
        }
    }

    function renderLivros(){
        if (livros.length === 0){
            if (textoBusca.trim().length === 0){
                return (<h3>Utilize o campo acima pra fazer uma busca</h3>);
            }else{
                return (<h3>Nenhum resultado encontrado</h3>);
            }
        }
        return (
            <div className="dataTable">
                <h2>Livros</h2>
                    <table cellSpacing={0}>
                        <thead>
                            <tr>
                                <th>Título</th>
                                <th>Editora</th>
                                <th>Edição</th>
                                <th>Volume</th>
                                <th>Páginas</th>
                                <th>Assunto</th>
                                <th>Autores</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {livros.map(l=>{
                                const strAutores = l.autores?.map(a => {
                                    return `${a.sobrenome?.toUpperCase()}, ${a.nome}`          
                                }).join('; ');
                                return(
                                    <tr key={l.id}>
                                        <td>{l.titulo}</td>
                                        <td>{l.editora?.nome}</td>
                                        <td>{l.edicao}</td>
                                        <td>{l.volume}</td>
                                        <td>{l.numPaginas}</td>
                                        <td>{l.assunto?.descricao}</td>
                                        <td>{strAutores}</td>
                                        <td>
                                            <Button onClick={e =>{
                                                    e.preventDefault();
                                                    _handleShow(l);
                                                }}>
                                                <VisibilityIcon />
                                            </Button>
                                        </td>
                                    </tr>
                                );
                            })}
                        </tbody>
                    </table>
                    <div className="paginationContainer">
                        <Pagination color="primary" 
                                    count={totalPag} page={pagNum} onChange={_handlePageChange}/>
                    </div>
            </div>
        );
    }

    bcMaker.addHrefBreadcrumb('Home', '/');

    return(
        <Fragment>
            {bcMaker.render()}
            <Paper>
                <div className="content">
                    <div className="row">
                        <TextField label="Buscar" variant="outlined" 
                                    className="text" 
                                    value={textoBusca}
                                    onChange={(e)=>setTextoBusca(e.target.value)}
                                    onKeyUp={(e)=>{
                                        if (e.code === 'Enter' || e.code === 'NumpadEnter') {
                                            _handleFind();
                                        }
                                    }}
                                    />
                        <Button variant="contained" onClick={_handleFind}>
                            <SearchIcon />
                        </Button>
                        {_renderCreateButtonIfManager()}
                    </div>
                </div>
            </Paper>
            <Paper>
                <div className="livrosContainer">
                    {renderLivros()}
                </div>
            </Paper>
            <Snackbar open={confOpen} autoHideDuration={10000} onClose={handleConfClose}>
                <Alert onClose={handleConfClose} severity="success">
                    {confMessage}
                </Alert>
            </Snackbar>
        </Fragment>
    );
}