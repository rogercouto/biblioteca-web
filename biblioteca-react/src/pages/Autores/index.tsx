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
import { Autor } from '../../model';
import { AutorService } from '../../services';

import './style.css';

export default function AutoresPage(props: any){

    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const [autores, setAutores] = useState(new Array<Autor>());
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);

    const [edtId, setEdtId] = useState(0);
    const [nome, setNome] = useState('');
    const [temErroNome, setTemErroNome] = useState(false);
    const [erroNome, setErroNome] = useState('');
    const [sobrenome, setSobrenome] = useState('');
    const [temErroSobrenome, setTemErroSobrenome] = useState(false);
    const [erroSobrenome, setErroSobrenome] = useState('');
    const [info, setInfo] = useState('');

    const bcMaker = new BreadcrumbsMaker('Autores');

    useEffect(()=>{
        AutorService.getList(pagNum).then((resp)=>{
            setAutores(resp.list);
            setTotalPag(resp.totalPag);
        })
    },[pagNum])

    function _handlePageChange(event: React.ChangeEvent<unknown>, value: number){
        setEdtId(0);
        setPagNum(value);
        window.scrollTo({top: 0, behavior: 'smooth'});
    }

    function _handleEdit(autor : Autor){
        setTemErroNome(false);
        setErroNome('');
        if (autor.id){
            setNome(autor.nome || '');
            setSobrenome(autor.sobrenome || '');
            setInfo(autor.info || '');
            setEdtId(autor.id);
        }
    }

    function _handleInsert(){
        setTemErroNome(false);
        setErroNome('');
        setNome('');
        setSobrenome('');
        setInfo('');
        setEdtId(-1);
    }

    function _validate() : boolean{
        let valid = true;
        if (nome.trim().length === 0){
            setTemErroNome(true);
            setErroNome('Nome não pode ficar em branco');
            valid = false;
        }
        if (sobrenome.trim().length === 0){
            setTemErroSobrenome(true);
            setErroSobrenome('Sobrenome não pode ficar em branco');
            valid = false;
        }
        if (!valid){
            return false;
        }
        setTemErroNome(false);
        setErroNome('');
        return true;
    }

    function indexOf(autor : Autor){
        const ids = autores.map((a : Autor)=>{return a.id;});
        return ids.indexOf(autor.id);
    }
    
    async function _handleSave(){
        if (_validate()){
            let sAutor = new Autor();
            let insert = true;
            if (edtId > 0){
                sAutor.id = edtId;
                insert = false;
            }
            sAutor.nome = nome;
            sAutor.sobrenome = sobrenome;
            if (info && info.trim().length > 0){
                sAutor.info = info;
            }
            const resp = await AutorService.save(sAutor);
            if (resp.done){
                const savedAutor = Autor.createFromData(resp.data)
                if (insert){
                    const array = [...autores, savedAutor];
                    setAutores(array);
                }else{
                    const array = [...autores];
                    const index = indexOf(savedAutor);
                    array[index] = savedAutor;
                    setAutores(array);
                }
                setEdtId(0);
            }
        }
    }

    async function _handleDelete(autor : Autor){
        if (window.confirm('Tem certeza que deseja remover o autor?')) {
            const resp = await AutorService.delete(autor);
            if (resp.done){
                const array = autores.filter((a: Autor)=>{
                    return a.id !== autor.id;
                });
                setAutores(array);
            }
        }
    }

    function _handleCancel(){
        setEdtId(0);

    }

    function _renderButtons(autor : Autor){
        if (edtId === 0){
            return (
                <Fragment>
                    <Button variant="contained" onClick={(e)=>{
                        _handleEdit(autor);
                    }}>
                        <EditIcon  style={{color: 'blue'}}/>
                    </Button>
                    <Button variant="contained" onClick={(e)=>{
                        _handleDelete(autor)
                    }}>
                        <DeleteIcon  style={{color: 'red'}}/>
                    </Button>
                </Fragment>
            );
        }else if (edtId === autor.id){
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

    function _renderRow(autor : Autor){
        if (autor.id && autor.id > 0 && edtId !== autor.id){
            return(
                <tr key={autor.id}>
                    <td>{autor.nome}</td>
                    <td>{autor.sobrenome}</td>
                    <td>{autor.info}</td>
                    <td>{_renderButtons(autor)}</td>
                </tr>
            );
        }else{
            return(
                <tr key={autor.id}>
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
                    <td>
                        <TextField
                            error={temErroSobrenome}
                            helperText={erroSobrenome}
                            type="text"
                            value={sobrenome}
                            fullWidth
                            onChange={(e)=>{
                                setSobrenome(e.target.value);
                            }}
                        />
                    </td>
                    <td>
                        <TextField
                            type="text"
                            value={info}
                            fullWidth
                            onChange={(e)=>{
                                setInfo(e.target.value);
                            }}
                        />
                    </td>
                    <td>{_renderButtons(autor)}</td>
                </tr>
            );
        }
    }

    function _renderInsertRow(){
        if (edtId < 0){
            return _renderRow(new Autor(-1));
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
        return (<div className="autoresContainer"><h1>Não autorizado!</h1></div>);
    }

    bcMaker.addHrefBreadcrumb('Home', '/');

    return (
        <div className="autoresContainer">
            {bcMaker.render()}
            <h2>Autores</h2>
            <table>
                <thead>
                    <tr>
                        <th>Nome</th>
                        <th>Sobrenome</th>
                        <th>Info</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {autores.map((a:Autor)=>{
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