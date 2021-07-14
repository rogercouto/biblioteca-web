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
import { Origem } from '../../model';
import { OrigemService } from '../../services';

import './style.css';

const OrigensPage = (props: any) => {
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

    const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
        setEdtId(0);
        setPagNum(value);
        window.scrollTo({top: 0, behavior: 'smooth'});
    };

    const handleEdit = (origem : Origem) => {
        setTemErroDescricao(false);
        setErroDescricao('');
        if (origem.id){
            setDescricao(origem.descricao || '');
            setEdtId(origem.id);
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
            setErroDescricao('Descrição do origem não pode ficar em branco');
            return false;
        }
        setTemErroDescricao(false);
        setErroDescricao('');
        return true;
    };

    const indexOf = (origem : Origem) => {
        const ids = origens.map((a : Origem)=>{return a.id;});
        return ids.indexOf(origem.id);
    };

    const handleSave = async () => {
        if (validate()){
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
    };

    const handleDelete = async (origem : Origem) => {
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
    };

    const handleCancel = () => {
        setEdtId(0);
    };

    const renderButtons = (origem : Origem) => {
        if (edtId === 0){
            return (
                <Fragment>
                    <Button variant="contained" onClick={(e)=>{
                        handleEdit(origem);
                    }}>
                        <EditIcon  style={{color: 'blue'}}/>
                    </Button>
                    <Button variant="contained" onClick={(e)=>{
                        handleDelete(origem)
                    }}>
                        <DeleteIcon  style={{color: 'red'}}/>
                    </Button>
                </Fragment>
            );
        }else if (edtId === origem.id){
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

    const renderRow = (origem : Origem) => {
        if (origem.id && origem.id > 0 && edtId !== origem.id){
            return(
                <tr key={origem.id}>
                    <td>{origem.descricao}</td>
                    <td>{renderButtons(origem)}</td>
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
                    <td>{renderButtons(origem)}</td>
                </tr>
            );
        }
    };

    const renderInsertRow = () => {
        if (edtId < 0){
            return renderRow(new Origem(-1));
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
        return (<div className="origensContainer"><h1>Não autorizado!</h1></div>);
    }

    bcMaker.addHrefBreadcrumb('Home', '/');

    return (
        <Fragment>
            {bcMaker.render()}
            <Paper className="origensContainer">
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

export default OrigensPage;