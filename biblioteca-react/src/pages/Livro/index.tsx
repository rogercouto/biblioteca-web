import { Fragment, useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import Cookie from 'js-cookie';

import { Button, Tooltip, Paper } from '@material-ui/core';
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import LibraryBooksIcon from '@material-ui/icons/LibraryBooks';

import { BreadcrumbsMaker } from '../../components/breadcrumbs'
import { Livro } from '../../model';
import { LivroService, ExemplarService } from '../../services';

import './style.css';

export default function LivroPage(props : any){
    
    const livro = Livro.createLivroFromState(props.location.state);
        
    const history = useHistory();

    const canEdit = Cookie.get('isGerente');
    const [canDelete, setCanDelete] = useState(false);

    const [numExemplares, setNumExemplares] = useState(0);

    const bcMaker = new BreadcrumbsMaker(livro.titulo || '');
    bcMaker.addHrefBreadcrumb('Home', '/');
    bcMaker.addHrefBreadcrumb('Livros', '/livros');

    useEffect(()=>{
        if (livro.id){
            ExemplarService.findExemplares(livro.id).then((exemplares)=>{
                setNumExemplares(exemplares.length);
                setCanDelete(exemplares.length === 0);
            });
        }
    },[livro.id]);

    function _handleEdit(){
        history.push('/livros/edit', livro);
    }

    async function _handleDelete(){
        if (livro.id && window.confirm('Tem certeza que deseja remover o livro?')) {
            const resp = await LivroService.delete(livro.id);
            if (resp.done){
                history.push('/livros', { message : 'Livro excluído!'});
            }else{
                alert(resp.message);
            }
        } 
    }

    function _renderButtonsIfManager(){
        if (canEdit){
            return (
                <Fragment>
                    <Button variant="contained" onClick={_handleEdit}>
                        <EditIcon />
                        Editar
                    </Button>
                    <Tooltip title={canDelete? '' : 
                        'Não é possivel excluir um livro que tenha exemplares cadastrados'}>
                        <span>
                            <Button 
                                id="deleteButon"
                                variant="contained" 
                                style={canDelete?{ 
                                    backgroundColor: '#DD0000',
                                    color: '#EEE',
                                    marginLeft: '2.5%',
                                    width: '95%'
                                }:{                          
                                    marginLeft: '2.5%',
                                    width: '95%'
                                    
                                }} 
                                disabled={!canDelete}
                                onClick={_handleDelete}>
                                <DeleteIcon />
                                Excluir
                            </Button>
                        </span>
                    </Tooltip>
                </Fragment>
            );
        }
    }

    function _goToExemplares(){
        history.push('/exemplares', livro);
    }

    return(
        <Fragment>
            {bcMaker.render()}
            <Paper className="livroContainer">
                <div className="dataContainer">
                    <div className="field">
                        <div className="label">Título: </div>
                        <div className="value">{livro.titulo}</div>
                    </div>
                    <div className="field">
                        <div className="label">Autor(es):</div>
                        <div className="value">{livro.getNomesAutores()}</div>
                    </div>
                    <div className="field">
                        <div className="label">Resumo:</div>
                        <div className="value">{livro.resumo}</div>
                    </div>
                    <div className="field">
                        <div className="label">ISBN:</div>
                        <div className="value">{livro.isbn}</div>
                        <div className="label">Cutter:</div>
                        <div className="value">{livro.cutter}</div>
                    </div>
                    <div className="field">
                        <div className="label">Editora:</div>
                        <div className="value">{livro.editora?.nome}</div>
                        <div className="label">Edição:</div>
                        <div className="value">{livro.edicao}</div>
                    </div>
                    <div className="field">
                        <div className="label">Volume:</div>
                        <div className="value">{livro.volume}</div>
                        <div className="label">Nº páginas:</div>
                        <div className="value">{livro.numPaginas}</div>
                    </div>
                    <div className="field">
                        <div className="label">Assunto:</div>
                        <div className="value">{livro.assunto?.descricao}</div>
                    </div>
                    <div className="field">
                        <div className="label">Ano publicação:</div>
                        <div className="value">{livro.anoPublicacao}</div>
                    </div>
                    <div className="field">
                        <div className="label">Categorias:</div>
                        <div className="value">
                            {livro.categorias?.map(
                                (c: any)=>{
                                    return c.descricao;
                                }
                            ).join('; ')}
                        </div>
                    </div>
                    <div className="field" id="firstButton">
                        <Button 
                            variant="contained" 
                            type="submit" 
                            onClick={_goToExemplares}>
                            <LibraryBooksIcon />
                            Exemplares ({numExemplares})
                        </Button>
                    </div>
                    <div className="field">
                        {_renderButtonsIfManager()}
                    </div>
                </div>
            </Paper>
        </Fragment>
    );

}