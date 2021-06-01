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
import { Origem } from '../../model';
import { OrigemService } from '../../services';

import './style.css';

export default function OrigensPage(props: any){

    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const [origens, setOrigens] = useState(new Array<Origem>());
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);

    const [edtId, setEdtId] = useState(0);
    const [descricao, setDescricao] = useState('');
    const [temErroDescricao, setTemErroDescricao] = useState(false);
    const [erroDescricao, setErroDescricao] = useState('');

    const bcMaker = new BreadcrumbsMaker('Origens');

    useEffect(()=>{
        OrigemService.getList(pagNum).then((resp)=>{
            setOrigens(resp.list);
            setTotalPag(resp.totalPag);
        })
    },[pagNum])

    function _handlePageChange(event: React.ChangeEvent<unknown>, value: number){
        setEdtId(0);
        setPagNum(value);
        window.scrollTo({top: 0, behavior: 'smooth'});
    }

    function _handleEdit(origem : Origem){
        setTemErroDescricao(false);
        setErroDescricao('');
        if (origem.id){
            setDescricao(origem.descricao || '');
            setEdtId(origem.id);
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
            setErroDescricao('Descrição do origem não pode ficar em branco');
            return false;
        }
        setTemErroDescricao(false);
        setErroDescricao('');
        return true;
    }

    function indexOf(origem : Origem){
        const ids = origens.map((a : Origem)=>{return a.id;});
        return ids.indexOf(origem.id);
    }
    
    async function _handleSave(){
        if (_validate()){
            let sOrigem = new Origem();
            let insert = true;
            if (edtId > 0){
                sOrigem.id = edtId;
                insert = false;
            }
            sOrigem.descricao = descricao;
            const resp = await OrigemService.save(sOrigem);
            if (resp.done){
                const savedOrigem = new Origem(resp.data.id, resp.data.descricao)
                if (insert){
                    const array = [...origens, savedOrigem];
                    setOrigens(array);
                }else{
                    const array = [...origens];
                    const index = indexOf(savedOrigem);
                    array[index] = savedOrigem;
                    setOrigens(array);
                }
                setEdtId(0);
            }
        }
    }

    async function _handleDelete(origem : Origem){
        if (window.confirm('Tem certeza que deseja remover o origem?')) {
            const resp = await OrigemService.delete(origem);
            if (resp.done){
                const array = origens.filter((a: Origem)=>{
                    return a.id !== origem.id;
                });
                setOrigens(array);
            }else{
                alert(resp.message || 'Erro desconhecido!');
            }
        }
    }

    function _handleCancel(){
        setEdtId(0);

    }

    function _renderButtons(origem : Origem){
        if (edtId === 0){
            return (
                <Fragment>
                    <Button variant="contained" onClick={(e)=>{
                        _handleEdit(origem);
                    }}>
                        <EditIcon  style={{color: 'blue'}}/>
                    </Button>
                    <Button variant="contained" onClick={(e)=>{
                        _handleDelete(origem)
                    }}>
                        <DeleteIcon  style={{color: 'red'}}/>
                    </Button>
                </Fragment>
            );
        }else if (edtId === origem.id){
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

    function _renderRow(origem : Origem){
        if (origem.id && origem.id > 0 && edtId !== origem.id){
            return(
                <tr key={origem.id}>
                    <td>{origem.descricao}</td>
                    <td>{_renderButtons(origem)}</td>
                </tr>
            );
        }else{
            return(
                <tr key={origem.id}>
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
                    <td>{_renderButtons(origem)}</td>
                </tr>
            );
        }
    }

    function _renderInsertRow(){
        if (edtId < 0){
            return _renderRow(new Origem(-1));
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
        return (<div className="origensContainer"><h1>Não autorizado!</h1></div>);
    }

    bcMaker.addHrefBreadcrumb('Home', '/');

    return (
        <div className="origensContainer">
            {bcMaker.render()}
            <h2>Origens</h2>
            <table>
                <thead>
                    <tr>
                        <th>Descricao</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {origens.map((a:Origem)=>{
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