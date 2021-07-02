
import { useEffect, useState} from 'react';
import Cookies from 'js-cookie';

import { Button, Tooltip, FormControlLabel, Switch, Snackbar } from '@material-ui/core';
import MuiAlert, { AlertProps } from '@material-ui/lab/Alert';
import { Pagination } from '@material-ui/lab';

import AssignmentReturnIcon from '@material-ui/icons/AssignmentReturn';
import UpdateIcon from '@material-ui/icons/Update';
import DeleteForeverIcon from '@material-ui/icons/DeleteForever';

import { BreadcrumbsMaker } from '../../components/breadcrumbs';

import { EmprestimoService, ReservaService } from '../../services';
import { Reserva, Exemplar, Usuario, Emprestimo } from '../../model';

import './style.css';

import QuestionDialog from '../../components/questionDialog';
import DialogEmprestimo from '../../components/dialogEmprestimo';
import DialogReserva from '../../components/dialogReserva';
import SelectorUser from '../../components/selectorUser';

const ReservasPage = () => {

    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const userId = Cookies.get('userId');
    
    const [reservas, setReservas] = useState(new Array<Reserva>());
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);
    const [somenteAtivos, setSomenteAtivos] = useState(true);

    const [sUserId, setSUserId] = useState<number | null>(canEdit? null : (userId ? +userId : null)); 

    const [dialogOpen, setDialogOpen] = useState(false); 
    const [dReserva, setDReserva] = useState<Reserva | undefined>(undefined); // lançar reserva

    const [dialogEmpOpen, setDialogEmpOpen] = useState(false);
    const [dEmpNumReg, setDEmpNumReg] = useState<number | undefined>(undefined);
    const [dEmpIdUsuario, setDEmpIdUsuario] = useState<number | undefined>(undefined);
    
    const [dialogDeleteOpen, setDialogDeleteOpen] = useState(false);

    const [confOpen, setConfOpen] = useState(false);
    const [confMessage, setConfMessage] = useState('');
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(()=>{
        if (sUserId){
            ReservaService.findUsuarioPage(sUserId, pagNum, somenteAtivos).then(resp=>{
                setReservas(resp.reservas);
                setTotalPag(resp.totalPag);
                window.scrollTo({top: 0, behavior: 'smooth'});
            });
        }else{
            ReservaService.findPage(pagNum, somenteAtivos).then(resp=>{
                setReservas(resp.reservas);
                setTotalPag(resp.totalPag);
                window.scrollTo({top: 0, behavior: 'smooth'});
            });
        }
    },[sUserId, pagNum, somenteAtivos]);

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
  
    const handleQuestionDelete = (reserva : Reserva) =>{
        setDReserva(reserva);
        setDialogDeleteOpen(true);
    };
    
    const handleConfirmDelete = async () =>{
        if (dReserva){
            const resp = await ReservaService.delete(dReserva);
            if (resp.done){
                const updReservas = [...reservas].filter((e:Reserva)=>{
                    return e.id !== dReserva.id;
                }); 
                setReservas(updReservas);    
            }
        }
        setDReserva(undefined);
        setDialogDeleteOpen(false);
    }

    const handleCancelDelete = () => {
        setDReserva(undefined);
        setDialogDeleteOpen(false);
    }
    

    const handleDialogOpen = () => {
        setDialogOpen(true);

    };

    const handleDialogSave = async (exemplar : Exemplar | undefined, usuario: Usuario | undefined) => {
        if (exemplar && usuario){
            const reserva = new Reserva();
            reserva.exemplar = exemplar;
            reserva.usuario = usuario;
            reserva.dataHora = new Date();
            reserva.ativa = true;
            const response = await ReservaService.insert(reserva);
            if (response.done){
                const newReserva : Reserva = response.object;
                setReservas([...reservas, newReserva]);
                setConfMessage(`Reserva confirmada, usuário deve retirar até até ${newReserva.dataLimite?.toLocaleDateString()}`);
                setConfOpen(true);
            }else{
                const message : string = response.object;
                setErrorMessage(message);
                setErrorOpen(true);    
            }
            setDialogOpen(false);
        }else{
            setErrorMessage('Erro desconhecido!');
            setErrorOpen(true);
        }
    };

    const handleDialogClose = () => {
        setDialogOpen(false);
    };

    const handleDialogEmpOpen = (reserva : Reserva) => {
        if (reserva.exemplar && reserva.exemplar.numRegistro && reserva.usuario && reserva.usuario.id){
            setDReserva(reserva);
            setDEmpNumReg(reserva.exemplar.numRegistro);
            setDEmpIdUsuario(reserva.usuario.id);
            setDialogEmpOpen(true);
        }
    };

    const handleDialogEmpSave = async (exemplar : Exemplar | undefined, usuario: Usuario | undefined) => {
        if (exemplar && usuario){
            const emprestimo = new Emprestimo();
            emprestimo.exemplar = exemplar;
            emprestimo.usuario = usuario;
            emprestimo.dataHora = new Date();
            const response = await EmprestimoService.insert(emprestimo);
            if (response.done){
                const newEmp : Emprestimo = response.object;
                setConfMessage(`Exemplar emprestado até ${newEmp.prazo?.toLocaleDateString()}`);
                setConfOpen(true);
                if (dReserva){
                    dReserva.ativa = false;
                    const newResvas = reservas.map((r:Reserva)=>{
                        if (r.id !== dReserva.id){
                            return r;
                        }else{
                            return dReserva;
                        }
                    });
                    setReservas(newResvas);
                }
            }else{
                const message : string = response.object;
                setErrorMessage(message);
                setErrorOpen(true);
            }
            setDialogEmpOpen(false);
        }else{
            setErrorMessage('Erro desconhecido!');
            setErrorOpen(true);
        }
        setDReserva(undefined);
    };

    const handleDialogEmpClose = () => {
        setDialogEmpOpen(false);
    };

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

    const renderLastTh = () => {
        if (canEdit){
            return (<th></th>);
        }
    };

    const renderLastTd = (reserva : Reserva) => {
        if (canEdit){
            return (
                <td>
                    <Tooltip title={reserva.ativa?'Registrar empréstimo':'Reserva Inativa!'}>
                        <span>
                            <Button 
                                disabled={!reserva.ativa}
                                variant="contained" 
                                onClick={(e: any)=>{handleDialogEmpOpen(reserva);}}>
                                <AssignmentReturnIcon className="flipH" />
                            </Button>                                  
                        </span>
                    </Tooltip>
                    <Tooltip title={reserva.ativa ? 'Excluir reserva':'Reserva inativa!'}>
                        <span>
                            <Button 
                                disabled={!reserva.ativa}
                                variant="contained" 
                                onClick={(e: any)=>{handleQuestionDelete(reserva);}}>
                                <DeleteForeverIcon />
                            </Button>                                  
                        </span>
                    </Tooltip>
                </td>
            );
        }
    };

    const bcMaker = new BreadcrumbsMaker('Reservas');

    bcMaker.addHrefBreadcrumb('Home', '/');

    return(
        <div className="reservasContainer">
            {bcMaker.render()}
            <h2>Reservas</h2>
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
                    label="Somente reservas ativas"
                />
                <span />
                {renderSelectorUser()}
            </div>
            <Button variant="contained" onClick={handleDialogOpen}>
                <UpdateIcon className="flipH"/>
                Nova reserva
            </Button>
            <table>
                <thead>
                    <tr>
                        <th>Data/Hora</th>
                        <th>Nº Exemplar</th>
                        <th>Título do livro</th>
                        <th>Usuário</th>
                        <th>Ativa</th>
                        <th>Retirar até</th>
                        {renderLastTh()}
                    </tr>
                </thead>
                <tbody>
                    {reservas.map(reserva=>{
                        return(
                            <tr key={reserva.id}>
                                <td>{reserva.dataHora?.toLocaleString()}</td>
                                <td>{reserva.exemplar?.numRegistro}</td>
                                <td>{reserva.exemplar?.livro?.titulo}</td>
                                <td>{reserva.usuario?.nome}</td>
                                <td>{reserva.ativa? 'Sim' : 'Não' } </td>
                                <td>{reserva.ativa? reserva.dataLimite?.toLocaleDateString() : '-'} </td>
                                {renderLastTd(reserva)}
                            </tr>
                        );
                    })}
                </tbody>
            </table>
            <div className="paginationContainer">
                <Pagination color="primary" 
                            count={totalPag} page={pagNum} onChange={handlePageChange}/>
            </div>
            <DialogReserva
                title="Nova reserva"
                message="Número de registro está localizado na etiqueta do exemplar"
                open={dialogOpen}
                onClose={handleDialogClose}
                onSave={handleDialogSave}
            />
            <DialogEmprestimo 
                title="Registrar empréstimo"
                message="Número de registro está localizado na etiqueta do exemplar"
                numReg={dEmpNumReg}
                idUsuario={dEmpIdUsuario}
                open={dialogEmpOpen}
                onClose={handleDialogEmpClose}
                onSave={handleDialogEmpSave}
                canChangeNumReg={false}
                canChangeUser={false}
            />
            <QuestionDialog 
                title="Atenção!"
                message="Confirma exclusão da reserva?"
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

export default ReservasPage;