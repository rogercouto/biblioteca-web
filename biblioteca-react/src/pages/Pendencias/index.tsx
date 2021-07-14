
import { Fragment, useEffect, useState} from 'react';
import Cookies from 'js-cookie';

import { Button, Tooltip, FormControlLabel, Switch, Snackbar, Paper } from '@material-ui/core';
import MuiAlert, { AlertProps } from '@material-ui/lab/Alert';
import { Pagination } from '@material-ui/lab';

import PaymentIcon from '@material-ui/icons/Payment';
import MoneyOffIcon from '@material-ui/icons/MoneyOff';
import DeleteForeverIcon from '@material-ui/icons/DeleteForever';

import { BreadcrumbsMaker } from '../../components/breadcrumbs';

import { PendenciaService } from '../../services';
import { Pendencia } from '../../model';

import './style.css';

import QuestionDialog from '../../components/questionDialog';
import SelectorUser from '../../components/selectorUser';

const PendenciasPage = () => {

    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const userId = Cookies.get('userId');
    
    const [sUserId, setSUserId] = useState<number | null>(canEdit? null : (userId ? +userId : null)); 

    const canDel = false;
    
    const [pendencias, setPendencias] = useState(new Array<Pendencia>());
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);
    const [somenteAtivos, setSomenteAtivos] = useState(true);

    const [tPendencia, setTPendencia] = useState<Pendencia | undefined>(undefined);
    const [dialogPayOpen, setDialogPayOpen] = useState(false);   
    const [dialogDeleteOpen, setDialogDeleteOpen] = useState(false);

    const [confOpen, setConfOpen] = useState(false);
    const [confMessage, setConfMessage] = useState('');
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(()=>{
        if (sUserId){
            PendenciaService.findUsuarioPage(sUserId, pagNum, somenteAtivos).then(resp=>{
                setPendencias(resp.pendencias);
                setTotalPag(resp.totalPag);
                window.scrollTo({top: 0, behavior: 'smooth'});
            });
        }else{
            PendenciaService.findPage(pagNum, somenteAtivos).then(resp=>{
                setPendencias(resp.pendencias);
                setTotalPag(resp.totalPag);
                window.scrollTo({top: 0, behavior: 'smooth'});
            });
        }
    },[  sUserId, pagNum, somenteAtivos]);

    const Alert = (props: AlertProps) => {
        return <MuiAlert elevation={6} variant="filled" {...props} />;
    };

    const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
        setPagNum(value);
    };

    const handleSwitch = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPagNum(1);
        setSomenteAtivos(event.target.checked);
    }

    // Pagar
  
    const handleQuestionPay = (pendencia : Pendencia) =>{
        setTPendencia(pendencia);
        setDialogPayOpen(true);
    };
    
    const handleConfirmPay = async () =>{
        if (tPendencia){
            const resp = await PendenciaService.alteraPagamento(tPendencia);
            if (resp.done){
                const uPendencia = resp.object;
                const updPendencias = pendencias.map((e:Pendencia)=>{
                    if (e.id !== tPendencia.id){
                        return e;
                    }else{
                        return uPendencia;
                    }
                }); 
                setPendencias(updPendencias); 
                setConfMessage('Pagamento confirmado!');
                setConfOpen(true);   
            }
        }
        setTPendencia(undefined);
        setDialogPayOpen(false);
    }

    const handleCancelPay = () => {
        setTPendencia(undefined);
        setDialogPayOpen(false);
    }

    //Delete

    const handleQuestionDelete = (pendencia : Pendencia) =>{
        setTPendencia(pendencia);
        setDialogDeleteOpen(true);
    };
    
    const handleConfirmDelete = async () =>{
        if (tPendencia){
            const resp = await PendenciaService.delete(tPendencia);
            if (resp.done){
                const updPendencias = [...pendencias].filter((e:Pendencia)=>{
                    return e.id !== tPendencia.id;
                }); 
                setPendencias(updPendencias); 
                setConfMessage('Pendência excluída!');
                setConfOpen(true);
            }
        }
        setTPendencia(undefined);
        setDialogDeleteOpen(false);
    }

    const handleCancelDelete = () => {
        setTPendencia(undefined);
        setDialogDeleteOpen(false);
    }

    const bcMaker = new BreadcrumbsMaker('Pendências');
    bcMaker.addHrefBreadcrumb('Home', '/');

    const renderPayIcon = (pendencia : Pendencia) => {
        if (pendencia.foiPaga()){
            return <MoneyOffIcon />
        }else{
            return <PaymentIcon />;
        }
    };

    const renderDelButton = (pendencia : Pendencia) => {
        if (canDel){
            <Tooltip title={!pendencia.foiPaga() ? 'Excluir pendência':'Pendencia inativa!'}>
                <span>
                    <Button 
                        disabled={pendencia.foiPaga()}
                        variant="contained" 
                        onClick={(e: any)=>{handleQuestionDelete(pendencia);}}>
                        <DeleteForeverIcon />
                    </Button>                                  
                </span>
            </Tooltip>
        }
    }

    const renderLastThIfGerente = () => {
        if (canEdit){
            return (
                <th></th>
            );
        }
    }

    const renderLastTdIfGerente = (pendencia : Pendencia) => {
        if (canEdit){
            return (
                <td>
                    <Tooltip title={pendencia.foiPaga()?'Cancelar pagamento':'Registrar pagamento!'}>
                        <span>
                            <Button 
                                variant="contained" 
                                onClick={(e: any)=>{handleQuestionPay(pendencia);}}>
                                {renderPayIcon(pendencia)}
                            </Button>                                  
                        </span>
                    </Tooltip>
                    {renderDelButton(pendencia)}
                </td>
            );
        }
    }

    const changeUser = (userId : number | null) => {
        setSUserId(userId);
    };

    const renderSelectorUser = ()  => {
        if (canEdit){
            return (
                <SelectorUser 
                    incAdmin={canEdit}
                    onChange={changeUser}
                />
            );
        }
    };

    const renderPagination = () => {
        if (totalPag > 1){
            return(
                <div className="paginationContainer">
                    <Pagination color="primary" 
                                count={totalPag} page={pagNum} onChange={handlePageChange}/>
                </div>
            );
        }else{
            return(
                <div className="paginationContainer" />
            );
        }
    };

    if (!userId){
        return (<div className="reservasContainer"><h1>Não autorizado!</h1></div>);
    }

    return(
        <Fragment>
            {bcMaker.render()}
            <Paper className="pendenciasContainer">
                <h2>Pendencias</h2>
                <div className="filterDiv">
                    <FormControlLabel
                        control={
                        <Switch
                            checked={somenteAtivos}
                            onChange={handleSwitch}
                            name="checkedSomenteAtivos"
                            color="primary"
                        />
                        }
                        label="Somente pendencias ativas"
                    />
                    <span />
                    {renderSelectorUser()}
                </div>
                <table>
                    <thead>
                        <tr>
                            <th style={{ width: '15%'}}>Data/Hora</th>
                            <th style={{ width: '40%'}}>Descrição</th>
                            <th>Usuário</th>
                            <th>Valor</th>
                            <th>Pago</th>
                            {renderLastThIfGerente()}
                        </tr>
                    </thead>
                    <tbody>
                        {pendencias.map(pendencia=>{
                            return(
                                <tr key={pendencia.id}>
                                    <td>{pendencia.dataHoraLancamento?.toLocaleString()}</td>
                                    <td>{pendencia.descricao}</td>
                                    <td>{pendencia.usuario?.nome}</td>
                                    <td>{pendencia.valor?.toFixed(2).replace('.',',')}</td>        
                                    <td>{pendencia.dataHoraPagamento? 'Sim' : 'Não' } </td>
                                    {renderLastTdIfGerente(pendencia)}
                                </tr>
                            );
                        })}
                    </tbody>
                </table>
                {renderPagination()}
                <QuestionDialog 
                    title="Atenção!"
                    message={tPendencia && tPendencia.foiPaga() ? 'Cancela pagamento da pendência?' : 'Confirma pagamento da pendência?'}
                    open={dialogPayOpen}
                    onConfirm={handleConfirmPay}
                    onClose={handleCancelPay}
                />
                <QuestionDialog 
                    title="Atenção!"
                    message="Confirma exclusão da pendencia?"
                    open={dialogDeleteOpen}
                    onConfirm={handleConfirmDelete}
                    onClose={handleCancelDelete}
                />
                <Snackbar open={confOpen} autoHideDuration={10000} onClose={(e)=>{
                    setConfOpen(false);
                    setConfMessage('');
                }}>
                    <Alert onClose={(e)=>{
                            setConfOpen(false);
                            setConfMessage('');
                        }} severity="success">
                        {confMessage}
                    </Alert>
                </Snackbar>
                <Snackbar open={errorOpen} autoHideDuration={10000} onClose={(e)=>{
                    setErrorOpen(false);
                    setErrorMessage('');
                }}>
                    <Alert onClose={(e)=>{
                            setErrorOpen(false);
                            setErrorMessage('');
                        }} severity="error">
                        {errorMessage}
                    </Alert>
                </Snackbar>
            </Paper>
        </Fragment>
    );
};

export default PendenciasPage;