import { Fragment, useState, useEffect } from 'react';
import Cookies from 'js-cookie';

import { TextField, Button, Paper } from '@material-ui/core';
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

const CategoriasPage = (props: any) => {
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

    const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
        setEdtId(0);
        setPagNum(value);
        window.scrollTo({top: 0, behavior: 'smooth'});
    };

    const handleEdit = (categoria : Categoria) => {
        setTemErroDescricao(false);
        setErroDescricao('');
        if (categoria.id){
            setDescricao(categoria.descricao || '');
            setEdtId(categoria.id);
        }
    };

    const handleInsert = () => {
        setTemErroDescricao(false);
        setErroDescricao('');
        setDescricao('');        
        setEdtId(-1);
    };

    const validate = (): boolean => {
        if (descricao.trim().length === 0){
            setTemErroDescricao(true);
            setErroDescricao('Descrição do categoria não pode ficar em branco');
            return false;
        }
        setTemErroDescricao(false);
        setErroDescricao('');
        return true;
    };

    const indexOf = (categoria : Categoria) => {
        const ids = categorias.map((a : Categoria)=>{return a.id;});
        return ids.indexOf(categoria.id);
    };

    const handleSave = async () => {
        if (validate()){
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
    };

    const handleDelete = async (categoria : Categoria) => {
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
    };

    const handleCancel = () => {
        setEdtId(0);

    };

    const renderButtons = (categoria : Categoria) => {
        if (edtId === 0){
            return (
                <Fragment>
                    <Button variant="contained" onClick={(e)=>{
                        handleEdit(categoria);
                    }}>
                        <EditIcon  style={{color: 'blue'}}/>
                    </Button>
                    <Button variant="contained" onClick={(e)=>{
                        handleDelete(categoria)
                    }}>
                        <DeleteIcon  style={{color: 'red'}}/>
                    </Button>
                </Fragment>
            );
        }else if (edtId === categoria.id){
            return (
                <Fragment>
                    <Button variant="contained" onClick={handleSave}>
                        <SaveIcon  style={{color: 'green'}}/>
                    </Button>
                    <Button variant="contained" onClick={handleCancel}>
                        <CancelIcon />
                    </Button>
                </Fragment>
            );
        } 
    };

    const renderRow = (categoria : Categoria) => {
        if (categoria.id && categoria.id > 0 && edtId !== categoria.id){
            return(
                <tr key={categoria.id}>
                    <td>{categoria.descricao}</td>
                    <td>{renderButtons(categoria)}</td>
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
                    <td>{renderButtons(categoria)}</td>
                </tr>
            );
        }
    };

    const renderInsertRow = () => {
        if (edtId < 0){
            return renderRow(new Categoria(-1));
        }
    };

    const renderInsertButton = () => {
        if (edtId === 0){
            return (
                <Button variant="contained" onClick={handleInsert}>
                    <AddCircleOutlineIcon  style={{color: 'blue'}}/>
                    Inserir
                </Button>
            );
        }else{
            return <div style={{height: '3rem'}}></div>
        }
    };

    if (!canEdit){
        return (<div className="categoriasContainer"><h1>Não autorizado!</h1></div>);
    }

    bcMaker.addHrefBreadcrumb('Home', '/');

    return (
        <Fragment>
            {bcMaker.render()}
            <Paper className="categoriasContainer">
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
                            return renderRow(a);
                        })}
                        {renderInsertRow()}
                    </tbody>
                </table>
                {renderInsertButton()}
                <div className="paginationContainer">
                    <Pagination color="primary" 
                                count={totalPag} page={pagNum} onChange={handlePageChange}/>
                </div>
            </Paper>
        </Fragment>
    );
};

export default CategoriasPage;