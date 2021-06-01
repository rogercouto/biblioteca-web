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
import { Editora } from '../../model';
import { EditoraService } from '../../services';

import './style.css';

export default function EditorasPage(props: any){

    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const [editoras, setEditoras] = useState(new Array<Editora>());
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);

    const [edtId, setEdtId] = useState(0);
    const [nome, setNome] = useState('');
    const [temErroNome, setTemErroNome] = useState(false);
    const [erroNome, setErroNome] = useState('');

    const bcMaker = new BreadcrumbsMaker('Editoras');

    useEffect(()=>{
        EditoraService.getList(pagNum).then((resp)=>{
            setEditoras(resp.list);
            setTotalPag(resp.totalPag);
        })
    },[pagNum])

    function _handlePageChange(event: React.ChangeEvent<unknown>, value: number){
        setEdtId(0);
        setPagNum(value);
        window.scrollTo({top: 0, behavior: 'smooth'});
    }

    function _handleEdit(editora : Editora){
        setTemErroNome(false);
        setErroNome('');
        if (editora.id){
            setNome(editora.nome || '');
            setEdtId(editora.id);
        }
    }

    function _handleInsert(){
        setTemErroNome(false);
        setErroNome('');
        setNome('');        
        setEdtId(-1);
    }

    function _validate() : boolean{
        if (nome.trim().length === 0){
            setTemErroNome(true);
            setErroNome('Descrição do editora não pode ficar em branco');
            return false;
        }
        setTemErroNome(false);
        setErroNome('');
        return true;
    }

    function indexOf(editora : Editora){
        const ids = editoras.map((a : Editora)=>{return a.id;});
        return ids.indexOf(editora.id);
    }
    
    async function _handleSave(){
        if (_validate()){
            let sEditora = new Editora();
            let insert = true;
            if (edtId > 0){
                sEditora.id = edtId;
                insert = false;
            }
            sEditora.nome = nome;
            const resp = await EditoraService.save(sEditora);
            if (resp.done){
                const savedEditora = new Editora(resp.data.id, resp.data.nome)
                if (insert){
                    const array = [...editoras, savedEditora];
                    setEditoras(array);
                }else{
                    const array = [...editoras];
                    const index = indexOf(savedEditora);
                    array[index] = savedEditora;
                    setEditoras(array);
                }
                setEdtId(0);
            }
        }
    }

    async function _handleDelete(editora : Editora){
        if (window.confirm('Tem certeza que deseja remover o editora?')) {
            const resp = await EditoraService.delete(editora);
            if (resp.done){
                const array = editoras.filter((a: Editora)=>{
                    return a.id !== editora.id;
                });
                setEditoras(array);
            }else{
                alert(resp.message || 'Erro desconhecido!');
            }
        }
    }

    function _handleCancel(){
        setEdtId(0);

    }

    function _renderButtons(editora : Editora){
        if (edtId === 0){
            return (
                <Fragment>
                    <Button variant="contained" onClick={(e)=>{
                        _handleEdit(editora);
                    }}>
                        <EditIcon  style={{color: 'blue'}}/>
                    </Button>
                    <Button variant="contained" onClick={(e)=>{
                        _handleDelete(editora)
                    }}>
                        <DeleteIcon  style={{color: 'red'}}/>
                    </Button>
                </Fragment>
            );
        }else if (edtId === editora.id){
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

    function _renderRow(editora : Editora){
        if (editora.id && editora.id > 0 && edtId !== editora.id){
            return(
                <tr key={editora.id}>
                    <td>{editora.nome}</td>
                    <td>{_renderButtons(editora)}</td>
                </tr>
            );
        }else{
            return(
                <tr key={editora.id}>
                    <td>
                        <TextField
                            error={temErroNome}
                            helperText={erroNome}
                            type="text"
                            value={nome}
                            fullWidth
                            onChange={(e)=>{
                                setNome(e.target.value);
                            }}
                        />
                    </td>
                    <td>{_renderButtons(editora)}</td>
                </tr>
            );
        }
    }

    function _renderInsertRow(){
        if (edtId < 0){
            return _renderRow(new Editora(-1));
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
        return (<div className="editorasContainer"><h1>Não autorizado!</h1></div>);
    }

    bcMaker.addHrefBreadcrumb('Home', '/');

    return (
        <div className="editorasContainer">
            {bcMaker.render()}
            <h2>Editoras</h2>
            <table>
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {editoras.map((a:Editora)=>{
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