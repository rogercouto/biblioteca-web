import { useHistory } from 'react-router-dom';
import Cookie from 'js-cookie';

import { Button } from '@material-ui/core';
import EditIcon from '@material-ui/icons/Edit';
import LibraryBooksIcon from '@material-ui/icons/LibraryBooks';

import { BreadcrumbsMaker } from '../../components/breadcrumbs'
import { Livro } from '../../model';

import './style.css';

export default function LivroPage(props : any){
    
    const livro = Livro.createLivroFromState(props.location.state);
    
    const history = useHistory();

    const canEdit = Cookie.get('isGerente');

    const bcMaker = new BreadcrumbsMaker(livro.titulo || '');
    bcMaker.addHrefBreadcrumb('Home', '/');
    bcMaker.addHrefBreadcrumb('Livros', '/livros');

    function _handleEdit(){
        history.push('/livros/edit', livro);
    }

    function _renderButtomIfManager(){
        if (canEdit){
            return (
                <Button variant="contained" type="submit" onClick={_handleEdit}>
                    <EditIcon />
                    Editar
                </Button>
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
                                Exemplares
                            </Button>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div className="editContainer">
                {_renderButtomIfManager()}
            </div>
        </div>
    );

}