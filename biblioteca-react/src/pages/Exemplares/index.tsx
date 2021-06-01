import { Fragment, useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import Cookies from 'js-cookie';

import { TextField, Button, FormControlLabel, Checkbox } from '@material-ui/core';
import { Autocomplete } from '@material-ui/lab';

import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import SaveIcon from '@material-ui/icons/Save';
import CancelIcon from '@material-ui/icons/Cancel';

import { BreadcrumbsMaker } from '../../components/breadcrumbs'
import { Exemplar, Secao, Origem } from '../../model';
import ExemplarService from '../../services/ExemplarService';

import './style.css';

export default function ExemplaresPage(props : any){

    const livro = props.location.state;

    const canEdit = Cookies.get('isGerente');

    const history = useHistory();

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

    const [edtDataAquisicao, setEdtDataAquisicao] = useState('');
    const [temErroDtAquis, setTemErroDtAquis] = useState(false);
    const [erroDtAquis, setErroDtAquis] = useState('');
    
    const [edtOrigem, setEdtOrigem] = useState('');
    const [temErroOrigem, setTemErroOrigem] = useState(false);
    const [erroOrigem, setErroOrigem] = useState('');

    const [edtFixo, setEdtFixo] = useState(false);

    const bcMaker = new BreadcrumbsMaker('Exemplares');

    useEffect(()=>{
        ExemplarService.findExemplares(livro.id).then(list =>{
            setExemplares(list);
        });
        ExemplarService.findSecoes().then(list=>{
            setSecoes(list);
        });
        ExemplarService.findOrigens().then(list=>{
            setOrigens(list);
        });
    },[livro.id]);

    bcMaker.addHrefBreadcrumb('Home', '/');
    bcMaker.addHrefBreadcrumb('Livros', '/livros');
    
    function _handleBack(){
        history.push({
            pathname: '/livros/show',
            state: livro
        });
    }
    
    if (livro && livro.id && livro.id > 0){
        bcMaker.addFunctionBreadcrumb(livro.titulo || 'Livro', _handleBack);
    }

    function _getIndex(numRegistro : number | undefined) : number{
        if (!numRegistro){
            return -1;
        }
        const nrs = exemplares.map((e)=>{
            return e.numRegistro;
        });           
        return nrs.indexOf(numRegistro);
    }

    function _getSecao(nome : string): Secao | undefined{
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
    }

    function _getOrigem(descr : string): Origem | undefined{
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
    }

    function _clearErrors(){
        setTemErroNumReg(false);
        setErroNumReg('');
        setTemErroSecao(false);
        setErroSecao('');
        setTemErroDtAquis(false);
        setErroDtAquis('');
        setTemErroOrigem(false);
        setErroOrigem('');
    }

    function _handleEdit(exemplar : Exemplar){
        if (exemplar !== null){
            _clearErrors();
            if (exemplar.secao?.nome){
                setEdtSecao(exemplar.secao.nome);
            }
            if (exemplar.dataAquisicao){
                setEdtDataAquisicao(exemplar.getDataAquisicaoAsString());
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
    }

    function _handleInsert(){
        _clearErrors();
        setNewNumReg('');
        setEdtSecao('');
        setEdtDataAquisicao('');
        setEdtOrigem('');
        setEdtFixo(false);
        setEdtNumReg(-1);
    }

    async function _handleDelete(exemplar : Exemplar){
        if (window.confirm('Tem certeza que deseja remover o exemplar?')) {
            const resp = await ExemplarService.delete(exemplar);
            if (resp.done){
                const newArray = exemplares.filter((e)=>{
                    return e.numRegistro !== exemplar.numRegistro
                });
                setExemplares(newArray);
            }else{
                alert(resp.message);
            }
        } 
    }

    function _handleCancel(){
        setEdtNumReg(0);
    }

    function _validate(){
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
        if (edtDataAquisicao.trim().length === 0){
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
    }

    async function _handleSave(){
        if (_validate()){
            const exemplar = new Exemplar();
            exemplar.secao = _getSecao(edtSecao);
            exemplar.dataAquisicao = Exemplar.getDataAquisicao(edtDataAquisicao);
            exemplar.origem = _getOrigem(edtOrigem);
            exemplar.fixo = edtFixo;
            exemplar.livro = { id: livro.id};
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
                    let oldSituacao = exemplares[_getIndex(edtNumReg)].situacao;
                    if (oldSituacao === 'Fixo' && !edtFixo){
                        oldSituacao = 'Disponível';
                    }
                    exemplar.situacao = edtFixo? 'Fixo' : oldSituacao;
                    exemplares[_getIndex(edtNumReg)] = exemplar;
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
                alert(resp.errors.join('\r'));
            }
            console.log(exemplares);
        }
    }

    function _renderButtons(exemplar : Exemplar){
        if (canEdit){
            if (edtNumReg === 0){
                return (
                    <Fragment>
                        <Button variant="contained" onClick={(e)=>{
                            _handleEdit(exemplar);
                        }}>
                            <EditIcon  style={{color: 'blue'}}/>
                        </Button>
                        <Button variant="contained" onClick={(e)=>{
                            _handleDelete(exemplar)
                        }}>
                            <DeleteIcon  style={{color: 'red'}}/>
                        </Button>
                    </Fragment>
                );
            }else{
                if (exemplar.numRegistro === edtNumReg){
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
                }else{
                    return (<Fragment />);
                }
            }
        }
        //depois colocar botões pra reservar caso logado
        return (<Fragment />);
    }

    function _renderFirstColumn(exemplar : Exemplar){
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
    }
    
    function _renderLastColumnContent(){
        if (canEdit){
            return(
                <td>
                    <Button variant="contained" onClick={_handleSave}>
                        <SaveIcon  style={{color: 'green'}}/>
                    </Button>
                    <Button variant="contained" onClick={_handleCancel}>
                        <CancelIcon />
                    </Button>
                </td>
            );
        }
        return (<Fragment />);
    }

    function _renderLastColumnButtons(exemplar : Exemplar){
        if (canEdit){
            return (<td>{_renderButtons(exemplar)}</td>);
        }
        return (<Fragment />);
    }

    function _renderRow(exemplar: Exemplar){       
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
                        {_renderFirstColumn(exemplar)}
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
                    {_renderLastColumnContent()}
                </Fragment>
            );
        }
    }

    function _renderLastRow(){
        if (edtNumReg < 0){
            return _renderRow(new Exemplar());
        }
    }

    function _renderInsertButton(){
        if (canEdit && edtNumReg === 0){
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

    function _renderLastColumn(){
        if (canEdit){
            return (<th></th>);
        }
        return (<Fragment/>);
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
                        {_renderLastColumn()}
                    </tr>
                </thead>
                <tbody>
                    {exemplares.map(exemplar=>{
                        return(
                            <tr key={exemplar.numRegistro}>
                                {_renderRow(exemplar)}
                            </tr>
                        );
                    })}
                    <tr>{_renderLastRow()}</tr>
                </tbody>
            </table>
            {_renderInsertButton()}
        </div>
    );
}