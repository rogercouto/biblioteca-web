import { Fragment, useEffect, useState } from 'react';
import { useHistory } from 'react-router-dom';
import Cookie from 'js-cookie';

import { Button, Tooltip } from '@material-ui/core';
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
                            style={canDelete?{ 
                                backgroundColor: '#DD0000',
                                color: '#EEE',
                                marginLeft: '0.5rem'
                            }:{                          
                                marginLeft: '0.5rem'
                            }} 
                            variant="contained" 
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
        <div className="livroContainer">
            {bcMaker.render()}
            <table>
                <tbody>
                    <tr>
                        <td className="rowLabel"> Título: </td>
                        <td className="rowValue" colSpan={3}>{livro.titulo}</td>
                    </tr>
                    <tr>
                        <td className="rowLabel">Autor(es): </td>
                        <td className="rowValue" colSpan={3}>{livro.getNomesAutores()}</td>
                    </tr>
                    <tr>
                        <td className="rowLabel">Resumo: </td>
                        <td className="rowValue" colSpan={3}>{livro.resumo}</td>
                    </tr>
                    <tr>
                        <td className="rowLabelSmall">ISBN: </td>
                        <td className="rowValueHalf">{livro.isbn}</td>
                        <td className="rowLabelSmall">Cutter: </td>
                        <td className="rowValueHalf">{livro.cutter}</td>
                    </tr>
                    <tr>
                        <td className="rowLabelSmall">Editora: </td>
                        <td className="rowValueHalf">{livro.editora?.nome}</td>
                        <td className="rowLabelSmall">Edição: </td>
                        <td className="rowValueHalf">{livro.edicao}</td>
                    </tr>
                    <tr>
                        <td className="rowLabelSmall">Volume: </td>
                        <td className="rowValueHalf">{livro.volume}</td>
                        <td className="rowLabelSmall">Nº páginas: </td>
                        <td className="rowValueHalf">{livro.numPaginas}</td>
                    </tr>
                    <tr>
                        <td className="rowLabel">Assunto: </td>
                        <td className="rowValue" colSpan={3}>{livro.assunto?.descricao}</td>
                    </tr>
                    <tr>
                        <td className="rowLabelSmall">Ano publicação: </td>
                        <td className="rowValueHalf">{livro.anoPublicacao}</td>
                    </tr>
                    <tr>
                        <td className="rowLabel">Categorias: </td>
                        <td className="rowValue" colSpan={3}>{
                            livro.categorias?.map(
                                (c: any)=>{
                                    return c.descricao;
                                }
                            ).join('; ')
                        }</td>
                    </tr>
                    <tr>
                        <td className="rowLabel"></td>
                        <td className="btnExemplares">
                            <Button variant="contained" type="submit" onClick={_goToExemplares}>
                                <LibraryBooksIcon />
                                Exemplares ({numExemplares})
                            </Button>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div className="editContainer">
                {_renderButtonsIfManager()}
            </div>
        </div>
    );

}