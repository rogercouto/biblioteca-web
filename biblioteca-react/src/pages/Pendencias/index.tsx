
import { useEffect, useState} from 'react';
import Cookies from 'js-cookie';

import { TextField, Button, Tooltip, FormControlLabel, Switch, Snackbar } from '@material-ui/core';
import MuiAlert, { AlertProps } from '@material-ui/lab/Alert';
import { Autocomplete, Pagination } from '@material-ui/lab';

import PaymentIcon from '@material-ui/icons/Payment';
import MoneyOffIcon from '@material-ui/icons/MoneyOff';
import DeleteForeverIcon from '@material-ui/icons/DeleteForever';

import { BreadcrumbsMaker } from '../../components/breadcrumbs';

import { PendenciaService, UsuarioService } from '../../services';
import { Pendencia, Usuario } from '../../model';

import './style.css';

import QuestionDialog from '../../components/questionDialog';

const PendenciasPage = () => {

    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const userId = Cookies.get('userId');
    
    const userName = Cookies.get('username') || '';

    const canDel = false;
    const fixedUser = !canEdit;

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

    const [usuarios, setUsuarios] = useState<Array<Usuario>>([]);
    const [nomesUsuarios, setNomesUsuarios] = useState<Array<string>>([]);
    const [nomeUsuario, setNomeUsuario] = useState<string | null>(fixedUser ? userName : '');

    useEffect(()=>{
        if (nomeUsuario === null || nomeUsuario.trim().length === 0){
            PendenciaService.findPage(pagNum, somenteAtivos).then(resp=>{
                setPendencias(resp.pendencias);
                setTotalPag(resp.totalPag);
                window.scrollTo({top: 0, behavior: 'smooth'});
            });
        }else{
            if (usuarios.length > 0 && nomesUsuarios.length > 0){
                const index = nomesUsuarios.indexOf(nomeUsuario);
                if (index >= 0){
                    const usuario = usuarios[index];
                    if (usuario && usuario.id){
                        PendenciaService.findUsuarioPage(usuario.id, pagNum, somenteAtivos).then(resp=>{
                            setPendencias(resp.pendencias);
                            setTotalPag(resp.totalPag);
                            window.scrollTo({top: 0, behavior: 'smooth'});
                        });
                    }
                }
            }else if (userId){
                const id = +userId;
                PendenciaService.findUsuarioPage(id, pagNum, somenteAtivos).then(resp=>{
                    setPendencias(resp.pendencias);
                    setTotalPag(resp.totalPag);
                    window.scrollTo({top: 0, behavior: 'smooth'});
                });
            }
        }
    },[pagNum, somenteAtivos, nomeUsuario, usuarios, nomesUsuarios, userId]);

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

    const buscaUsuarios = (value : string) => {
        UsuarioService.find(value, canEdit).then((usuarios : Array<Usuario>)=>{
            setUsuarios(usuarios);
            const nomes = usuarios.map((u : Usuario)=>{return u.nome || ''});
            setNomesUsuarios(nomes);
        });
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

    const renderBuscaUsuarioIfGerente = () => {
        if (canEdit){
            return (
                <Autocomplete
                    options={nomesUsuarios}
                    value={nomeUsuario}
                    onChange={(e, value)=>{
                        setNomeUsuario(value);
                    }}
                    renderInput={(params) => (
                        <TextField {...params} 
                            variant="outlined" 
                            label="Usuário" 
                            className="formControl"
                            onChange={(e)=>buscaUsuarios(e.target.value)}
                        />
                    )}
                />
            );
        }
    }

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
        <div className="pendenciasContainer">
            {bcMaker.render()}
            <h2>Pendencias</h2>
            <div className="formDiv">
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
                {renderBuscaUsuarioIfGerente()}
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
        </div>
    );
};

export default PendenciasPage;