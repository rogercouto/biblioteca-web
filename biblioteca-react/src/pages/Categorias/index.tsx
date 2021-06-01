import { Fragment, useState, useEffect } from 'react';
import Cookies from 'js-cookie';

import { TextField, Button } from '@material-ui/core';
import Pagination from '@material-ui/lab/Pagination';

import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import SaveIcon from '@material-ui/icons/Save';
import CancelIcon from '@material-ui/icons/Cancel';

import { BreadcrumbsMaker } from '../../components/breadcrumbs'
import { Categoria } from '../../model';
import { CategoriaService } from '../../services';

import './style.css';

export default function CategoriasPage(props: any){

    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const [categorias, setCategorias] = useState(new Array<Categoria>());
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);

    const [edtId, setEdtId] = useState(0);
    const [descricao, setDescricao] = useState('');
    const [temErroDescricao, setTemErroDescricao] = useState(false);
    const [erroDescricao, setErroDescricao] = useState('');

    const bcMaker = new BreadcrumbsMaker('Categorias');

    useEffect(()=>{
        CategoriaService.getList(pagNum).then((resp)=>{
            setCategorias(resp.list);
            setTotalPag(resp.totalPag);
        })
    },[pagNum])

    function _handlePageChange(event: React.ChangeEvent<unknown>, value: number){
        setEdtId(0);
        setPagNum(value);
        window.scrollTo({top: 0, behavior: 'smooth'});
    }

    function _handleEdit(categoria : Categoria){
        setTemErroDescricao(false);
        setErroDescricao('');
        if (categoria.id){
            setDescricao(categoria.descricao || '');
            setEdtId(categoria.id);
        }
    }

    function _handleInsert(){
        setTemErroDescricao(false);
        setErroDescricao('');
        setDescricao('');        
        setEdtId(-1);
    }

    function _validate() : boolean{
        if (descricao.trim().length === 0){
            setTemErroDescricao(true);
            setErroDescricao('Descrição do categoria não pode ficar em branco');
            return false;
        }
        setTemErroDescricao(false);
        setErroDescricao('');
        return true;
    }

    function indexOf(categoria : Categoria){
        const ids = categorias.map((a : Categoria)=>{return a.id;});
        return ids.indexOf(categoria.id);
    }
    
    async function _handleSave(){
        if (_validate()){
            let sCategoria = new Categoria();
            let insert = true;
            if (edtId > 0){
                sCategoria.id = edtId;
                insert = false;
            }
            sCategoria.descricao = descricao;
            const resp = await CategoriaService.save(sCategoria);
            if (resp.done){
                const savedCategoria = new Categoria(resp.data.id, resp.data.descricao)
                if (insert){
                    const array = [...categorias, savedCategoria];
                    setCategorias(array);
                }else{
                    const array = [...categorias];
                    const index = indexOf(savedCategoria);
                    array[index] = savedCategoria;
                    setCategorias(array);
                }
                setEdtId(0);
            }
        }
    }

    async function _handleDelete(categoria : Categoria){
        if (window.confirm('Tem certeza que deseja remover o categoria?')) {
            const resp = await CategoriaService.delete(categoria);
            if (resp.done){
                const array = categorias.filter((a: Categoria)=>{
                    return a.id !== categoria.id;
                });
                setCategorias(array);
            }else{
                alert(resp.message || 'Erro desconhecido!');
            }
        }
    }

    function _handleCancel(){
        setEdtId(0);

    }

    function _renderButtons(categoria : Categoria){
        if (edtId === 0){
            return (
                <Fragment>
                    <Button variant="contained" onClick={(e)=>{
                        _handleEdit(categoria);
                    }}>
                        <EditIcon  style={{color: 'blue'}}/>
                    </Button>
                    <Button variant="contained" onClick={(e)=>{
                        _handleDelete(categoria)
                    }}>
                        <DeleteIcon  style={{color: 'red'}}/>
                    </Button>
                </Fragment>
            );
        }else if (edtId === categoria.id){
            return (
                <Fragment>
                    <Button variant="contained" onClick={_handleSave}>
                        <SaveIcon  style={{color: 'green'}}/>
                    </Button>
                    <Button variant="contained" onClick={_handleCancel}>
                        <CancelIcon />
                    </Button>
                </Fragment>
            );
        } 
    }

    function _renderRow(categoria : Categoria){
        if (categoria.id && categoria.id > 0 && edtId !== categoria.id){
            return(
                <tr key={categoria.id}>
                    <td>{categoria.descricao}</td>
                    <td>{_renderButtons(categoria)}</td>
                </tr>
            );
        }else{
            return(
                <tr key={categoria.id}>
                    <td>
                        <TextField
                            error={temErroDescricao}
                            helperText={erroDescricao}
                            type="text"
                            value={descricao}
                            fullWidth
                            onChange={(e)=>{
                                setDescricao(e.target.value);
                            }}
                        />
                    </td>
                    <td>{_renderButtons(categoria)}</td>
                </tr>
            );
        }
    }

    function _renderInsertRow(){
        if (edtId < 0){
            return _renderRow(new Categoria(-1));
        }
    }

    function _renderInsertButton(){
        if (edtId === 0){
            return (
                <Button variant="contained" onClick={_handleInsert}>
                    <AddCircleOutlineIcon  style={{color: 'blue'}}/>
                    Inserir
                </Button>
            );
        }else{
            return <div style={{height: '3rem'}}></div>
        }
    }

    if (!canEdit){
        return (<div className="categoriasContainer"><h1>Não autorizado!</h1></div>);
    }

    bcMaker.addHrefBreadcrumb('Home', '/');

    return (
        <div className="categoriasContainer">
            {bcMaker.render()}
            <h2>Categorias</h2>
            <table>
                <thead>
                    <tr>
                        <th>Descricao</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {categorias.map((a:Categoria)=>{
                        return _renderRow(a);
                    })}
                    {_renderInsertRow()}
                </tbody>
            </table>
            {_renderInsertButton()}
            <div className="paginationContainer">
                <Pagination color="primary" 
                            count={totalPag} page={pagNum} onChange={_handlePageChange}/>
            </div>
        </div>
    );
}