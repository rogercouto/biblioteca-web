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
import { Assunto } from '../../model';
import { AssuntoService } from '../../services';

import './style.css';

export default function AssuntosPage(props: any){

    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const [assuntos, setAssuntos] = useState(new Array<Assunto>());
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);

    const [edtId, setEdtId] = useState(0);
    const [descricao, setDescricao] = useState('');
    const [temErroDescr, setTemErroDescr] = useState(false);
    const [erroDescr, setErroDescr] = useState('');
    const [cores, setCores] = useState('');
    const [cdu, setCdu] = useState('');

    const bcMaker = new BreadcrumbsMaker('Assuntos');

    useEffect(()=>{
        AssuntoService.getList(pagNum).then((resp)=>{
            setAssuntos(resp.list);
            setTotalPag(resp.totalPag);
        })
    },[pagNum])

    function _handlePageChange(event: React.ChangeEvent<unknown>, value: number){
        setEdtId(0);
        setPagNum(value);
        window.scrollTo({top: 0, behavior: 'smooth'});
    }

    function _handleEdit(assunto : Assunto){
        setTemErroDescr(false);
        setErroDescr('');
        if (assunto.id){
            setDescricao(assunto.descricao || '');
            setCores(assunto.cores || '');
            setCdu(assunto.cdu || '');
            setEdtId(assunto.id);
        }
    }

    function _handleInsert(){
        setTemErroDescr(false);
        setErroDescr('');
        setDescricao('');
        setCores('');
        setCdu('');
        setEdtId(-1);
    }

    function _validate() : boolean{
        if (descricao.trim().length === 0){
            setTemErroDescr(true);
            setErroDescr('Descrição do assunto não pode ficar em branco');
            return false;
        }
        setTemErroDescr(false);
        setErroDescr('');
        return true;
    }

    function indexOf(assunto : Assunto){
        const ids = assuntos.map((a : Assunto)=>{return a.id;});
        return ids.indexOf(assunto.id);
    }
    
    async function _handleSave(){
        if (_validate()){
            let sAssunto = new Assunto();
            let insert = true;
            if (edtId > 0){
                sAssunto.id = edtId;
                insert = false;
            }
            sAssunto.descricao = descricao;
            if (cores && cores.trim().length > 0){
                sAssunto.cores = cores;
            }
            if (cdu && cdu.trim().length > 0){
                sAssunto.cdu = cdu;
            }
            const resp = await AssuntoService.save(sAssunto);
            if (resp.done){
                const savedAssunto = Assunto.createFromData(resp.data)
                if (insert){
                    const array = [...assuntos, savedAssunto];
                    setAssuntos(array);
                }else{
                    const array = [...assuntos];
                    const index = indexOf(savedAssunto);
                    array[index] = savedAssunto;
                    setAssuntos(array);
                }
                setEdtId(0);
            }
        }
    }

    async function _handleDelete(assunto : Assunto){
        if (window.confirm('Tem certeza que deseja remover o assunto?')) {
            const resp = await AssuntoService.delete(assunto);
            if (resp.done){
                const array = assuntos.filter((a: Assunto)=>{
                    return a.id !== assunto.id;
                });
                setAssuntos(array);
            }else{
                alert(resp.message || 'Erro desconhecido!');
            }
        }
    }

    function _handleCancel(){
        setEdtId(0);

    }

    function _renderButtons(assunto : Assunto){
        if (edtId === 0){
            return (
                <Fragment>
                    <Button variant="contained" onClick={(e)=>{
                        _handleEdit(assunto);
                    }}>
                        <EditIcon  style={{color: 'blue'}}/>
                    </Button>
                    <Button variant="contained" onClick={(e)=>{
                        _handleDelete(assunto)
                    }}>
                        <DeleteIcon  style={{color: 'red'}}/>
                    </Button>
                </Fragment>
            );
        }else if (edtId === assunto.id){
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

    function _renderRow(assunto : Assunto){
        if (assunto.id && assunto.id > 0 && edtId !== assunto.id){
            return(
                <tr key={assunto.id}>
                    <td>{assunto.descricao}</td>
                    <td>{assunto.cores}</td>
                    <td>{assunto.cdu}</td>
                    <td>{_renderButtons(assunto)}</td>
                </tr>
            );
        }else{
            return(
                <tr key={assunto.id}>
                    <td>
                        <TextField
                            error={temErroDescr}
                            helperText={erroDescr}
                            type="text"
                            value={descricao}
                            fullWidth
                            onChange={(e)=>{
                                setDescricao(e.target.value);
                            }}
                        />
                    </td>
                    <td>
                        <TextField
                            type="text"
                            value={cores}
                            fullWidth
                            onChange={(e)=>{
                                setCores(e.target.value);
                            }}
                        />
                    </td>
                    <td>
                        <TextField
                            type="text"
                            value={cdu}
                            fullWidth
                            onChange={(e)=>{
                                setCdu(e.target.value);
                            }}
                        />
                    </td>
                    <td>{_renderButtons(assunto)}</td>
                </tr>
            );
        }
    }

    function _renderInsertRow(){
        if (edtId < 0){
            return _renderRow(new Assunto(-1));
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
        return (<div className="assuntosContainer"><h1>Não autorizado!</h1></div>);
    }

    bcMaker.addHrefBreadcrumb('Home', '/');

    return (
        <div className="assuntosContainer">
            {bcMaker.render()}
            <h2>Assuntos</h2>
            <table>
                <thead>
                    <tr>
                        <th>Descrição</th>
                        <th>Cores</th>
                        <th>Cdu</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {assuntos.map((a:Assunto)=>{
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