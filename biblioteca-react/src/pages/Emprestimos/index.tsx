
import { useEffect, useState} from 'react';
import Cookies from 'js-cookie';

import { Button, Tooltip, FormControlLabel, Switch, Snackbar } from '@material-ui/core';
import MuiAlert, { AlertProps } from '@material-ui/lab/Alert';
import { Pagination } from '@material-ui/lab';

import AssignmentReturnIcon from '@material-ui/icons/AssignmentReturn';
import UpdateIcon from '@material-ui/icons/Update';
import DeleteForeverIcon from '@material-ui/icons/DeleteForever';

import { BreadcrumbsMaker } from '../../components/breadcrumbs';

import { EmprestimoService } from '../../services';
import { Emprestimo, Exemplar, Usuario } from '../../model';

import './style.css';

import QuestionDialog from '../../components/questionDialog';
import DialogEmprestimo from '../../components/dialogEmprestimo';

const EmprestimosPage = () => {

    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const [emprestimos, setEmprestimos] = useState(new Array<Emprestimo>());
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);
    const [somenteAtivos, setSomenteAtivos] = useState(true);

    const [dialogOpen, setDialogOpen] = useState(false); //Inserção

    const [dialogDevolucaoOpen, setDialogDevolucaoOpen] = useState(false);
    const [dialogRenovacaoOpen, setDialogRenovacaoOpen] = useState(false);
    const [dialogDeleteOpen, setDialogDeleteOpen] = useState(false);
    const [dEmprestimo, setDEmprestimo] = useState<Emprestimo | undefined>(undefined); //devolução-renovacao-exclusão

    const [confOpen, setConfOpen] = useState(false);
    const [confMessage, setConfMessage] = useState('');
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    useEffect(()=>{
        EmprestimoService.findPage(pagNum, somenteAtivos).then(resp=>{
            setEmprestimos(resp.emprestimos);
            setTotalPag(resp.totalPag);
            window.scrollTo({top: 0, behavior: 'smooth'});
        });
    },[pagNum, somenteAtivos]);

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
  
    const daysDiff = (a : Date, b: Date) : number => {
        const utc1 = Date.UTC(a.getFullYear(), a.getMonth(), a.getDate());
        const utc2 = Date.UTC(b.getFullYear(), b.getMonth(), b.getDate());
        const _MS_PER_DAY = 1000 * 60 * 60 * 24;
        return Math.floor((utc2 - utc1) / _MS_PER_DAY);
    }

    const podeRenovar = (emprestimo : Emprestimo) : boolean => {
        if (emprestimo.exemplar && emprestimo.exemplar.situacao !== 'Emprestado'){
            return false;
        }
        if (!emprestimo.foiDevolvido() && emprestimo.prazo){
            const diff = daysDiff(new Date(), emprestimo.prazo);
            return (diff > 0 && diff < 6);    
        }
        return false;
    }

    const getRenTooltipText = (emprestimo : Emprestimo) : string => {
        if (emprestimo.exemplar && emprestimo.exemplar.situacao !== 'Emprestado'){
            return 'Exemplar já reservado!';
        }
        if (emprestimo.foiDevolvido()){
            return 'Exemplar já devolvido!';
        }else{
            if (emprestimo.prazo){
                const diff = daysDiff(new Date(), emprestimo.prazo);
                if (diff > 6){
                    return 'Ainda não é possível renovar o empréstimo!';
                }else if (diff < 0){
                    return 'Não é mais possível renovar o empréstimo!';
                }
            }
        }
        return 'Renovar empréstimo';
    }

    const handleQuestionDevolucao = (emprestimo : Emprestimo) => {
        setDEmprestimo(emprestimo);
        setDialogDevolucaoOpen(true);
    };

    const handleConfirmDevolucao = async() =>{
        if (dEmprestimo){
            const index = emprestimos.indexOf(dEmprestimo);
            const response = await EmprestimoService.devolucao(dEmprestimo);
            console.log(response);
            if (response.done){
                const updEmprestimo = response.object.emprestimo;
                const updEmprestimos = [...emprestimos];
                updEmprestimos[index] = updEmprestimo;
                setEmprestimos(updEmprestimos);
                setDEmprestimo(undefined);
                setConfMessage(response.object.message);
                setConfOpen(true);
            }
        }
        setDialogDevolucaoOpen(false);
    };

    const handleCancelDevolucao = () =>{
        setDEmprestimo(undefined);
        setDialogDevolucaoOpen(false);
    }

    const handleQuestionRenovacao = (emprestimo : Emprestimo) => {
        setDEmprestimo(emprestimo);
        setDialogRenovacaoOpen(true);
    };

    const handleConfirmRenovacao = async() =>{
        if (dEmprestimo){
            const index = emprestimos.indexOf(dEmprestimo);
            const updEmprestimo = await EmprestimoService.renovacao(dEmprestimo);
            const updEmprestimos = [...emprestimos];
            updEmprestimos[index] = updEmprestimo.object;
            setEmprestimos(updEmprestimos);
            setDEmprestimo(undefined);
        }
        setDialogRenovacaoOpen(false);
    };

    const handleCancelRenovacao = () =>{
        setDEmprestimo(undefined);
        setDialogRenovacaoOpen(false);
    }

    const handleQuestionDelete = (emprestimo : Emprestimo) =>{
        setDEmprestimo(emprestimo);
        setDialogDeleteOpen(true);
    };
    
    const handleConfirmDelete = async () =>{
        if (dEmprestimo){
            const resp = await EmprestimoService.delete(dEmprestimo);
            if (resp.done){
                const updEmprestimos = [...emprestimos].filter((e:Emprestimo)=>{
                    return e.id !== dEmprestimo.id;
                }); 
                setEmprestimos(updEmprestimos);    
            }
        }
        setDEmprestimo(undefined);
        setDialogDeleteOpen(false);
    }

    const handleCancelDelete = () => {
        setDEmprestimo(undefined);
        setDialogDeleteOpen(false);
    }
    
    const handleDialogOpen = () => {
        setDialogOpen(true);
    };

    const handleDialogSave = async (exemplar : Exemplar | undefined, usuario: Usuario | undefined) => {
        if (exemplar && usuario){
            try{
                if (exemplar.situacao === 'Disponível' || exemplar.situacao === 'Reservado'){
                    const emprestimo = new Emprestimo();
                    emprestimo.dataHora = new Date();
                    emprestimo.exemplar = exemplar;
                    emprestimo.usuario = usuario;
                    const resp = await EmprestimoService.insert(emprestimo);
                    if (resp.done){
                        const newEmp = resp.object;
                        const newList = [...emprestimos, newEmp];
                        setEmprestimos(newList);
                        setDialogOpen(false);
                        setConfMessage(`Exemplar emprestado até ${newEmp.prazo?.toLocaleDateString()}`);
                        setConfOpen(true);
                    }else{
                        setErrorMessage(resp.object.message);
                        setErrorOpen(true);
                    }
                }else{
                    setErrorMessage('Exemplar indisponível!');
                    setErrorOpen(true);
                }
            }catch(error){
                setErrorMessage('Erro desconhecido!');
                setErrorOpen(true);
                console.log(error);
            }
        }
    };
    
    const handleDialogClose = () => {
        setDialogOpen(false);
    };

    const bcMaker = new BreadcrumbsMaker('Empréstimos');

    bcMaker.addHrefBreadcrumb('Home', '/');

    if (!canEdit){
        return (<div className="emprestimosContainer"><h1>Não autorizado!</h1></div>);
    }

    return(
        <div className="emprestimosContainer">
            {bcMaker.render()}
            <h2>Empréstimos</h2>
            <p>
                <FormControlLabel
                    control={
                    <Switch
                        checked={somenteAtivos}
                        onChange={handleSwitch}
                        name="checkedSomenteAtivos"
                        color="primary"
                    />
                    }
                    label="Somente empréstimos ativos"
                />
            </p>
            <Button variant="contained" onClick={handleDialogOpen}>
                <AssignmentReturnIcon className="flipH"/>
                Novo empréstimo
            </Button>
            <table>
                <thead>
                    <tr>
                        <th>Data/Hora</th>
                        <th>Nº Exemplar</th>
                        <th>Título do livro</th>
                        <th>Usuário</th>
                        <th>Prazo</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {emprestimos.map(emprestimo=>{
                        return(
                            <tr key={emprestimo.id}>
                                <td>{emprestimo.dataHora?.toLocaleString()}</td>
                                <td>{emprestimo.exemplar?.numRegistro}</td>
                                <td>{emprestimo.exemplar?.livro?.titulo}</td>
                                <td>{emprestimo.usuario?.nome}</td>
                                <td style={emprestimo.getTdStyle()}>{emprestimo.dataHoraDevolucao? 'Devolvido' : emprestimo.prazo?.toLocaleDateString() } </td>
                                <td>
                                    <Tooltip title={!emprestimo.foiDevolvido()?'Registrar devolução':'Exemplar já devolvido!'}>
                                        <span>
                                            <Button 
                                                disabled={emprestimo.foiDevolvido()}
                                                variant="contained" 
                                                onClick={(e: any)=>{handleQuestionDevolucao(emprestimo);}}>
                                                <AssignmentReturnIcon />
                                            </Button>                                  
                                        </span>
                                    </Tooltip>
                                    <Tooltip title={getRenTooltipText(emprestimo)}>
                                        <span>
                                            <Button
                                                disabled={!podeRenovar(emprestimo)} 
                                                variant="contained" 
                                                onClick={(e: any)=>{handleQuestionRenovacao(emprestimo);}}>
                                                <UpdateIcon />
                                            </Button>                                  
                                        </span>
                                    </Tooltip>
                                    <Tooltip title={!emprestimo.foiDevolvido()?'Excluir empréstimo':'Exemplar já devolvido!'}>
                                        <span>
                                            <Button 
                                                disabled={emprestimo.foiDevolvido()}
                                                variant="contained" 
                                                onClick={(e: any)=>{handleQuestionDelete(emprestimo);}}>
                                                <DeleteForeverIcon />
                                            </Button>                                  
                                        </span>
                                    </Tooltip>
                                </td>
                            </tr>
                        );
                    })}
                </tbody>
            </table>
            <div className="paginationContainer">
                <Pagination color="primary" 
                            count={totalPag} page={pagNum} onChange={handlePageChange}/>
            </div>
            <DialogEmprestimo 
                title="Novo empréstimo"
                message="Número de registro está localizado na etiqueta do exemplar"
                open={dialogOpen}
                onClose={handleDialogClose}
                onSave={handleDialogSave}
            />
            <QuestionDialog 
                title="Atenção!"
                message="Confirma devolução do exemplar?"
                open={dialogDevolucaoOpen}
                onConfirm={handleConfirmDevolucao}
                onClose={handleCancelDevolucao}
            />
            <QuestionDialog 
                title="Atenção!"
                message="Confirma renovação do empréstimo?"
                open={dialogRenovacaoOpen}
                onConfirm={handleConfirmRenovacao}
                onClose={handleCancelRenovacao}
            />
            <QuestionDialog 
                title="Atenção!"
                message="Confirma exclusão do empréstimo?"
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

export default EmprestimosPage;