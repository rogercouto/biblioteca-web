import { Fragment, useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import Cookies from 'js-cookie';

import { TextField, Button, FormControlLabel, Checkbox, Tooltip, Snackbar } from '@material-ui/core';
import MuiAlert, { AlertProps } from '@material-ui/lab/Alert';
import { Autocomplete } from '@material-ui/lab';

import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import SaveIcon from '@material-ui/icons/Save';
import CancelIcon from '@material-ui/icons/Cancel';
import UpdateIcon from '@material-ui/icons/Update';

import { BreadcrumbsMaker } from '../../components/breadcrumbs'
import { Exemplar, Secao, Origem, Reserva, Usuario } from '../../model';
import { ExemplarService, ReservaService } from '../../services';

import DateTimeUtil from '../../util/DateTimeUtil';
import QuestionDialog from '../../components/questionDialog';
import DialogReserva from '../../components/dialogReserva';

import './style.css';

const ExemplaresPage = (props : any) => {

    const livro = props.location.state;
    const history = useHistory();

    const canEdit = Cookies.get('isGerente') === 'true';
    const userId = Cookies.get('userId');


    const [exemplares, setExemplares] = useState(new Array<Exemplar>());

    const [secoes, setSecoes] = useState(new Array<Secao>());
    const [origens, setOrigens] = useState(new Array<Origem>());
    
    const [edtNumReg, setEdtNumReg] = useState(0);
    const [newNumReg, setNewNumReg] = useState('');
    const [temErroNumReg, setTemErroNumReg] = useState(false);
    const [erroNumReg, setErroNumReg] = useState('');
    
    const [edtSecao, setEdtSecao] = useState('');
    const [temErroSecao, setTemErroSecao] = useState(false);
    const [erroSecao, setErroSecao] = useState('');

    const [edtDataAquisicao, setEdtDataAquisicao] = useState<string | null>('');
    const [temErroDtAquis, setTemErroDtAquis] = useState(false);
    const [erroDtAquis, setErroDtAquis] = useState('');
    
    const [edtOrigem, setEdtOrigem] = useState('');
    const [temErroOrigem, setTemErroOrigem] = useState(false);
    const [erroOrigem, setErroOrigem] = useState('');

    const [edtFixo, setEdtFixo] = useState(false);

    const bcMaker = new BreadcrumbsMaker('Exemplares');

    const [qExemplar, setQExemplar] = useState<Exemplar | undefined>(undefined);
    const [qResOpen, setQResOpen] = useState(false);

    const [confOpen, setConfOpen] = useState(false);
    const [confMessage, setConfMessage] = useState('');
    const [errorOpen, setErrorOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState('');

    const [dialogOpen, setDialogOpen] = useState(false);
    const [dialogNumReg, setDialogNumReg] = useState<number | undefined>(undefined);

    useEffect(()=>{
        if (livro){
            ExemplarService.findExemplares(livro.id).then(list =>{
                setExemplares(list);
            });
            ExemplarService.findSecoes().then(list=>{
                setSecoes(list);
            });
            ExemplarService.findOrigens().then(list=>{
                setOrigens(list);
            });
        }
    },[livro]);

    const Alert = (props: AlertProps) => {
        return <MuiAlert elevation={6} variant="filled" {...props} />;
    };
    
    bcMaker.addHrefBreadcrumb('Home', '/');
    bcMaker.addHrefBreadcrumb('Livros', '/livros');
    
    const handleBack = () => {
        history.push({
            pathname: '/livros/show',
            state: livro
        });
    };
    
    if (livro && livro.id && livro.id > 0){
        bcMaker.addFunctionBreadcrumb(livro.titulo || 'Livro', handleBack);
    }

    const getIndex = (numRegistro : number | undefined) : number => {
        if (!numRegistro){
            return -1;
        }
        const nrs = exemplares.map((e)=>{
            return e.numRegistro;
        });           
        return nrs.indexOf(numRegistro);
    };

    const getSecao = (nome : string): Secao | undefined => {
        const l = secoes.filter((s)=>{
            return s.nome === nome
        });
        if (l.length > 0){
            return l[0];
        }
        if (nome.trim().length > 0){
            return new Secao(undefined, nome);
        }
        return undefined;
    };

    const getOrigem = (descr : string): Origem | undefined => {
        const l = origens.filter((o)=>{
            return o.descricao === descr;
        });
        if (l.length > 0){
            return l[0];
        }
        if (descr.trim().length > 0){
            return new Origem(undefined, descr);
        }
        return undefined;
    };

    const clearErrors = () => {
        setTemErroNumReg(false);
        setErroNumReg('');
        setTemErroSecao(false);
        setErroSecao('');
        setTemErroDtAquis(false);
        setErroDtAquis('');
        setTemErroOrigem(false);
        setErroOrigem('');
    };

    const handleEdit = (exemplar : Exemplar) => {
        if (exemplar !== null){
            clearErrors();
            if (exemplar.secao?.nome){
                setEdtSecao(exemplar.secao.nome);
            }
            if (exemplar.dataAquisicao){
                setEdtDataAquisicao(DateTimeUtil.toAPIDate(exemplar.dataAquisicao));
            }
            if (exemplar.origem?.descricao){
                setEdtOrigem(exemplar.origem.descricao);
            }
            if (exemplar.situacao){
                setEdtFixo(exemplar.situacao === 'Fixo');
            }
            if (exemplar.numRegistro){
                setEdtNumReg(exemplar.numRegistro);
            }
        }
    };

    const handleInsert = () => {
        clearErrors();
        setNewNumReg('');
        setEdtSecao('');
        setEdtDataAquisicao('');
        setEdtOrigem('');
        setEdtFixo(false);
        setEdtNumReg(-1);
    };

    const handleDelete = async (exemplar : Exemplar) => {
        if (window.confirm('Tem certeza que deseja remover o exemplar?')) {
            const resp = await ExemplarService.delete(exemplar);
            if (resp.done){
                const newArray = exemplares.filter((e)=>{
                    return e.numRegistro !== exemplar.numRegistro
                });
                setExemplares(newArray);
            }else{
                setErrorMessage(resp.message);
                setErrorOpen(true);
            }
        } 
    };

    const handleCancel = () =>{
        setEdtNumReg(0);
    };

    const validate =() => {
        let valid = true;
        if (edtNumReg < 0 && newNumReg.trim().length === 0){
            setTemErroNumReg(true);
            setErroNumReg('Número de registro deve ser informado!');
            valid = false;
        }else{
            setTemErroNumReg(false);
            setErroNumReg('');
        }
        if (edtSecao.trim().length === 0){
            setTemErroSecao(true);
            setErroSecao('Seção deve ser selecionada!');
            valid = false;
        }else{
            setTemErroSecao(false);
            setErroSecao('');
        }
        if (edtDataAquisicao === null || edtDataAquisicao.trim().length === 0){
            setTemErroDtAquis(true);
            setErroDtAquis('Seção deve ser selecionada!');
            valid = false;
        }else{
            setTemErroDtAquis(false);
            setErroDtAquis('');
        }
        if (edtOrigem.trim().length === 0){
            setTemErroOrigem(true);
            setErroOrigem('Origem deve ser selecionada!');
            valid = false;
        }else{
            setTemErroOrigem(false);
            setErroOrigem('');
        }
        return valid;
    };

    const handleSave = async () => {
        if (validate()){
            const exemplar = new Exemplar();
            exemplar.secao = getSecao(edtSecao);
            exemplar.dataAquisicao = DateTimeUtil.fromTextField(edtDataAquisicao);         
            exemplar.origem = getOrigem(edtOrigem);
            exemplar.fixo = edtFixo;
            exemplar.livro = { id: livro.id, titulo: livro.titulo};
            let resp;
            if (edtNumReg < 0){
                //save insert
                exemplar.numRegistro = +newNumReg;
                exemplar.situacao = edtFixo ? 'Fixo' : 'Disponível';
                resp = await ExemplarService.insert(exemplar);
                if (resp.done){
                    exemplares.push(exemplar);
                }
                
            }else if (edtNumReg > 0){
                //save edit
                exemplar.numRegistro = edtNumReg;
                resp = await ExemplarService.update(exemplar);
                if (resp.done){
                    let oldSituacao = exemplares[getIndex(edtNumReg)].situacao;
                    if (oldSituacao === 'Fixo' && !edtFixo){
                        oldSituacao = 'Disponível';
                    }
                    exemplar.situacao = edtFixo? 'Fixo' : oldSituacao;
                    exemplares[getIndex(edtNumReg)] = exemplar;
                }
            }
            if (resp.done){
                setNewNumReg('');
                setEdtSecao('');
                setEdtDataAquisicao('');
                setEdtOrigem('');
                setEdtFixo(false);
                setEdtNumReg(0);
            }else{
                setErrorMessage(resp.errors.join('\r'));
                setErrorOpen(true);
            }
        }
    };

    const handleQuestionReserva = (exemplar : Exemplar) => {
        if (canEdit){
            openForm(exemplar);
        }else{
            setQExemplar(exemplar);
            setQResOpen(true);
        }
    }

    const handleConfirmReserva = async () => {
        if (qExemplar && userId){
            const reserva = new Reserva();
            reserva.exemplar = qExemplar;
            reserva.usuario = new Usuario(+userId);
            reserva.dataHora = new Date();
            const response = await ReservaService.insert(reserva);
            if (response.done){
                const exemplar = response.object.exemplar;
                const newList = exemplares.map(e=>{
                    if (e.numRegistro === exemplar.numRegistro){
                        return exemplar;
                    }
                    return e;
                });
                setExemplares(newList);
                setConfMessage(`Exemplar reservado até ${response.object.dataLimite.toLocaleDateString()}`);
                setConfOpen(true);
            }else{
                setErrorMessage(response.object.message);
                setErrorOpen(true);
            }
            setQExemplar(undefined);
            setQResOpen(false);
        }
    }

    const handleCancelReserva = () => {
        setQExemplar(undefined);
        setQResOpen(false);
    }


    const renderButtons = (exemplar : Exemplar) => {
        const podeReservar : boolean = exemplar.situacao === 'Disponível' || exemplar.situacao === 'Emprestado';
        if (canEdit){
            if (edtNumReg === 0){
                return (
                    <Fragment>
                        <Tooltip title={podeReservar ? 'Reservar exemplar' : 'Exemplar já reservado'}>
                            <span>
                                <Button 
                                    disabled={!podeReservar}
                                    variant="contained" 
                                    onClick={(e)=>{
                                        handleQuestionReserva(exemplar);
                                }}>
                                    <UpdateIcon />
                                </Button>
                            </span>
                        </Tooltip>
                        <Tooltip title="Editar exemplar">
                            <span>
                                <Button variant="contained" onClick={(e)=>{
                                    handleEdit(exemplar);
                                }}>
                                    <EditIcon  style={{color: 'blue'}}/>
                                </Button>
                            </span>
                        </Tooltip>
                        <Tooltip title="Excluir exemplar">
                            <span>
                                <Button variant="contained" onClick={(e)=>{
                                    handleDelete(exemplar)
                                }}>
                                    <DeleteIcon  style={{color: 'red'}}/>
                                </Button>
                            </span>
                        </Tooltip>
                    </Fragment>
                );
            }else{
                if (exemplar.numRegistro === edtNumReg){
                    return (
                        <Fragment>
                            <Tooltip title="Salvar mudanças">
                                <span>
                                    <Button variant="contained" onClick={handleSave}>
                                        <SaveIcon  style={{color: 'green'}}/>
                                    </Button>
                                </span>
                            </Tooltip>
                            <Tooltip title="Cancelar operação">
                                <span>
                                    <Button variant="contained" onClick={handleCancel}>
                                        <CancelIcon />
                                    </Button>
                                </span>
                            </Tooltip>
                        </Fragment>
                    );
                }else{
                    return (<Fragment />);
                }
            }
        }
        //depois colocar botões pra reservar caso logado
        return (
            <Fragment>
                <Tooltip title={podeReservar ? 'Reservar exemplar' : 'Exemplar já reservado'}>
                    <span>
                        <Button 
                            disabled={!podeReservar}
                            variant="contained" 
                            onClick={(e)=>{
                                handleQuestionReserva(exemplar);
                            }}>
                            <UpdateIcon />
                        </Button>
                    </span>
                </Tooltip>
            </Fragment>
        );
    };

    const renderFirstColumn = (exemplar : Exemplar) => {
        if (edtNumReg < 0){
            return (
                <TextField
                    error={temErroNumReg}
                    helperText={erroNumReg}
                    type="number"
                    value={newNumReg}
                    onChange={(e)=>{
                        setNewNumReg(e.target.value);
                    }}
                />
            );
        }else{
            return <Fragment>{exemplar.numRegistro}</Fragment>
        }
    };
    
    const renderLastColumnContent = () => {
        if (canEdit){
            return(
                <td>
                    <Tooltip title="Salvar mudanças">
                        <span>
                            <Button variant="contained" onClick={handleSave}>
                                <SaveIcon  style={{color: 'green'}}/>
                            </Button>
                        </span>
                    </Tooltip>
                    <Tooltip title="Cancelar operação">
                        <span>
                            <Button variant="contained" onClick={handleCancel}>
                                <CancelIcon />
                            </Button>
                        </span>
                    </Tooltip>
                </td>
            );
        }
        return (<Fragment />);
    };

    const _renderLastColumnButtons = (exemplar : Exemplar) => {
        return (<td className={canEdit ? "gerente":"aluno"}>{renderButtons(exemplar)}</td>);
    };

    const renderRow = (exemplar: Exemplar) => {       
        if (exemplar.numRegistro && edtNumReg !== exemplar.numRegistro){
            //Only show
            return (
                <Fragment>
                    <td>{exemplar.numRegistro}</td>
                    <td>{exemplar.secao?.nome}</td>
                    <td>{exemplar.dataAquisicao?.toLocaleDateString()}</td>
                    <td>{exemplar.origem?.descricao}</td>
                    <td>{exemplar.situacao}</td>
                    {_renderLastColumnButtons(exemplar)}
                </Fragment>
            );
        }else{
            return (
                <Fragment>
                    <td>
                        {renderFirstColumn(exemplar)}
                    </td>
                    <td>
                        <Autocomplete
                            options={secoes.map((s)=>{
                                return s.nome;
                            })}
                            value={edtSecao}
                            onChange={(e, value)=>{
                                setEdtSecao(value || '');
                            }}
                            renderInput={(params) => (
                                <TextField {...params} 
                                    error={temErroSecao}
                                    helperText={erroSecao}
                                    
                                />
                            )}
                        /> 
                    </td>
                    <td>
                        <TextField
                            error={temErroDtAquis}
                            helperText={erroDtAquis}
                            type="date"
                            value={edtDataAquisicao}
                            onChange={(e)=>{
                                setEdtDataAquisicao(e.target.value);
                            }}
                            InputLabelProps={{
                                shrink: true,
                            }}
                        />
                    </td>
                    <td>
                        <Autocomplete
                            options={origens.map((s)=>{
                                return s.descricao;
                            })}
                            value={edtOrigem}
                            onChange={(e, value)=>{
                                setEdtOrigem(value || '');
                            }}
                            renderInput={(params) => (
                                <TextField {...params} 
                                    error={temErroOrigem}
                                    helperText={erroOrigem}
                                    onChange={(e)=>{
                                        setEdtOrigem(e.target.value);
                                    }}
                                />
                            )}
                        />
                    </td>
                    <td>
                        <FormControlLabel
                            label="Fixo"
                            control={
                                <Checkbox
                                    checked={edtFixo}
                                    onChange={(e)=>{
                                        setEdtFixo(e.target.checked);
                                    }}
                                    name="checkedB"
                                    color="primary"
                                />
                                }
                        />
                    </td>
                    {renderLastColumnContent()}
                </Fragment>
            );
        }
    };

    const renderLastRow = () => {
        if (edtNumReg < 0){
            return renderRow(new Exemplar());
        }
    };

    const renderInsertButton = () => {
        if (canEdit && edtNumReg === 0){
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

    const openForm = (exemplar : Exemplar) => {
        setDialogNumReg(exemplar?.numRegistro);
        setDialogOpen(true);
    };

    const closeForm = () => {
        setDialogOpen(false);
    };

    const saveFormReserva = async (exemplar : Exemplar | undefined, usuario: Usuario | undefined) => {
        if (exemplar){
            const reserva = new Reserva();
            reserva.exemplar = exemplar;
            reserva.usuario = usuario;
            reserva.dataHora = new Date();
            const response = await ReservaService.insert(reserva);
            if (response.done){
                const exemplar = response.object.exemplar;
                const newList = exemplares.map(e=>{
                    if (e.numRegistro === exemplar.numRegistro){
                        return exemplar;
                    }
                    return e;
                });
                setExemplares(newList);
                setConfMessage(`Exemplar reservado até ${response.object.dataLimite.toLocaleDateString()}`);
                setConfOpen(true);
            }else{
                setErrorMessage(response.object.message);
                setErrorOpen(true);
            }
            setDialogOpen(false);
        }
    }

    if (!livro){
        return (<div className="exemplaresContainer"><h1>Não autorizado!</h1></div>);
    }

    return(
        <div className="exemplaresContainer">
            {bcMaker.render()}
            <h2>{livro.titulo}</h2>
            <h3>Exemplares:</h3>
            <table>
                <thead>
                    <tr>
                        <th>Nº Registro</th>
                        <th>Seção</th>
                        <th>Data de aquisição</th>
                        <th>Origem</th>
                        <th>Situação</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {exemplares.map(exemplar=>{
                        return(
                            <tr key={exemplar.numRegistro}>
                                {renderRow(exemplar)}
                            </tr>
                        );
                    })}
                    <tr>{renderLastRow()}</tr>
                </tbody>
            </table>
            {renderInsertButton()}
            <QuestionDialog 
                title="Atenção!"
                message="Confirma reserva do exemplar?"
                open={qResOpen}
                onConfirm={handleConfirmReserva}
                onClose={handleCancelReserva}
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
            <DialogReserva 
                title="Nova reserva"
                message="Número de registro está localizado na etiqueta do exemplar"
                numReg={dialogNumReg}
                onSave={saveFormReserva}
                onClose={closeForm}
                canChangeNumReg={false}
                verificaReserva={false}
                open={dialogOpen}
             />
        </div>
    );
};

export default ExemplaresPage;