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
import { Secao } from '../../model';
import { SecaoService } from '../../services';

import './style.css';

const SecoesPage = (props: any) => {
    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const [secoes, setSecoes] = useState(new Array<Secao>());
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);

    const [edtId, setEdtId] = useState(0);
    const [nome, setNome] = useState('');
    const [temErroNome, setTemErroNome] = useState(false);
    const [erroNome, setErroNome] = useState('');

    const bcMaker = new BreadcrumbsMaker('Secoes');

    useEffect(()=>{
        SecaoService.getList(pagNum).then((resp)=>{
            setSecoes(resp.list);
            setTotalPag(resp.totalPag);
        })
    },[pagNum])

    const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
        setEdtId(0);
        setPagNum(value);
        window.scrollTo({top: 0, behavior: 'smooth'});
    };

    const handleEdit = (secao : Secao) => {
        setTemErroNome(false);
        setErroNome('');
        if (secao.id){
            setNome(secao.nome || '');
            setEdtId(secao.id);
        }
    };

    const handleInsert = () => {
        setTemErroNome(false);
        setErroNome('');
        setNome('');        
        setEdtId(-1);
    };

    const validate = (): boolean => {
        if (nome.trim().length === 0){
            setTemErroNome(true);
            setErroNome('Descrição do secao não pode ficar em branco');
            return false;
        }
        setTemErroNome(false);
        setErroNome('');
        return true;
    };

    const indexOf = (secao : Secao) => {
        const ids = secoes.map((a : Secao)=>{return a.id;});
        return ids.indexOf(secao.id);
    };

    const handleSave = async () => {
        if (validate()){
            let sSecao = new Secao();
            let insert = true;
            if (edtId > 0){
                sSecao.id = edtId;
                insert = false;
            }
            sSecao.nome = nome;
            const resp = await SecaoService.save(sSecao);
            if (resp.done){
                const savedSecao = new Secao(resp.data.id, resp.data.nome)
                if (insert){
                    const array = [...secoes, savedSecao];
                    setSecoes(array);
                }else{
                    const array = [...secoes];
                    const index = indexOf(savedSecao);
                    array[index] = savedSecao;
                    setSecoes(array);
                }
                setEdtId(0);
            }
        }
    };

    const handleDelete = async (secao : Secao) => {
        if (window.confirm('Tem certeza que deseja remover o secao?')) {
            const resp = await SecaoService.delete(secao);
            if (resp.done){
                const array = secoes.filter((a: Secao)=>{
                    return a.id !== secao.id;
                });
                setSecoes(array);
            }else{
                alert(resp.message || 'Erro desconhecido!');
            }
        }
    };

    const handleCancel = () => {
        setEdtId(0);

    };

    const renderButtons = (secao : Secao) => {
        if (edtId === 0){
            return (
                <Fragment>
                    <Button variant="contained" onClick={(e)=>{
                        handleEdit(secao);
                    }}>
                        <EditIcon  style={{color: 'blue'}}/>
                    </Button>
                    <Button variant="contained" onClick={(e)=>{
                        handleDelete(secao)
                    }}>
                        <DeleteIcon  style={{color: 'red'}}/>
                    </Button>
                </Fragment>
            );
        }else if (edtId === secao.id){
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

    const renderRow = (secao : Secao) => {
        if (secao.id && secao.id > 0 && edtId !== secao.id){
            return(
                <tr key={secao.id}>
                    <td>{secao.nome}</td>
                    <td>{renderButtons(secao)}</td>
                </tr>
            );
        }else{
            return(
                <tr key={secao.id}>
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
                    <td>{renderButtons(secao)}</td>
                </tr>
            );
        }
    };

    const renderInsertRow = () => {
        if (edtId < 0){
            return renderRow(new Secao(-1));
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
        return (<div className="secoesContainer"><h1>Não autorizado!</h1></div>);
    }

    bcMaker.addHrefBreadcrumb('Home', '/');

    return (
        <Fragment>
            {bcMaker.render()}
            <Paper className="secoesContainer">
                <h2>Secoes</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {secoes.map((a:Secao)=>{
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

export default SecoesPage;