import { useState, useEffect } from 'react';
import PropTypes from 'prop-types';

import { TextField, Button } from '@material-ui/core';
import { Autocomplete } from '@material-ui/lab';

import DoneIcon from '@material-ui/icons/Done';
import CloseIcon from '@material-ui/icons/Close';

import { Exemplar, Usuario } from '../../model';
import { ExemplarService, ReservaService, UsuarioService } from '../../services';

import './style.css';
import Cookies from 'js-cookie';

const FormMovimento = ( props : any) => {
    
    const isGerente = Cookies.get('isGerente') === 'true';

    const [numRegistro, setNumRegistro] = useState<number | undefined>(props.numReg);
    const [idUsuario, setIdUsuario] = useState<number | undefined>(props.idUsuario);

    const [temErroNumReg, setTemErroNumReg] = useState<boolean>(false);
    const [erroNumReg, setErroNumReg] = useState<String>('');
    const [info, setInfo] = useState<string>('');
    const [exemplar, setExemplar] = useState<Exemplar | undefined>(undefined);
    const [numRegDisabled, setNumRegDisabled] = useState<boolean>(false);

    const [usuarios, setUsuarios] = useState<Array<Usuario>>([]);
    const [nomesUsuarios, setNomesUsuarios] = useState<Array<string>>([]);

    const [buscaUsuario, setBuscaUsuario] = useState<string>('');
    const [nomeUsuario, setNomeUsuario] = useState<string | null | undefined>(null);
    const [usuario, setUsuario] = useState<Usuario | undefined>(undefined);
    const [temErroUsuario, setTemErroUsuario] = useState<boolean>(false);
    const [erroUsuario, setErroUsusuario] = useState<string>('');
    
    useEffect(()=>{
        if (numRegistro){
            ExemplarService.findById(numRegistro).then((e : Exemplar | null)=>{
                if (e){
                    setExemplar(e);
                    setInfo(e.livro?.titulo || '');
                    setErroNumReg('');
                    setTemErroNumReg(false);
                    if (!props.canChangeNumReg){
                        setNumRegDisabled(true);
                    }
                }
            });
        }
        if (buscaUsuario && buscaUsuario.trim().length > 0){
            UsuarioService.find(buscaUsuario, isGerente).then((list : Array<Usuario>) => {
                setUsuarios(list);
                const nomes = list.map((u:Usuario)=>{return u.nome || '';});
                setNomesUsuarios(nomes);
                if (idUsuario){
                    let userName : string | undefined;
                    list.forEach((u : Usuario)=>{ 
                        if (u.id === idUsuario){
                            userName = u.nome;
                        }
                    });
                    if (userName){
                        const users = list.filter((u:Usuario)=>{
                            return u.nome === userName;
                        });
                        if (users.length === 1){
                            const user = users[0];
                            setUsuario(user);
                            setIdUsuario(user.id);
                        }
                        setNomeUsuario(userName);
                    }
                }
            })
        }else{
            if (idUsuario){
                UsuarioService.findById(idUsuario).then((user: Usuario)=>{
                    setUsuarios( [ user ] );
                    setNomesUsuarios( [ user.nome || '' ]);
                    if (user.nome){
                        setUsuario(user);
                    }
                });
            }else{
                setUsuarios([]);
            }
            setNomesUsuarios([]);
        }
    },[numRegistro, buscaUsuario, props.canChangeNumReg, isGerente, idUsuario]);

    const handleChangeNumReg = (event : any) => {
        const num = +event.target.value;
        setNumRegistro(num);
    }

    const handleNumRegExit = () => {
        if (numRegistro){    
            ExemplarService.findById(numRegistro).then((e : Exemplar | null)=>{
                if (e){
                    setExemplar(e);
                    setInfo(e.livro?.titulo || '');
                    if (e.situacao === 'Disponível' || e.situacao === 'Reservado'){
                        setErroNumReg('');
                        setTemErroNumReg(false);
                    }else{
                        setErroNumReg('Exemplar indisponível!');
                        setTemErroNumReg(true);
                    }
                }else{
                    setErroNumReg('Exemplar com esse número não existe!');
                    setTemErroNumReg(true);
                    setInfo('');
                }
            });
        }else{
            setErroNumReg('Número de registro deve ser informado!');
            setTemErroNumReg(true);
        }
    }

    const handleBuscaUsuario = (event : any) => {
        const text = event.target.value;
        setBuscaUsuario(text);
    }
    
    const handleSelectUsuario = (value: String | null) => {
        if (value){
            const nome = value.toString();
            setNomeUsuario(nome);
            if (nome && temErroNumReg){
                setErroUsusuario('');
                setTemErroUsuario(false);
            }
            const index = nomesUsuarios.indexOf(nome);
            if (index >= 0){
                const usr = usuarios[index];
                setUsuario(usr);
                if (usr){
                    setIdUsuario(usr.id);
                }
            }
        }else{
            setNomeUsuario(undefined);
            setUsuario(undefined);
            setIdUsuario(undefined);
        }
    };

    const handleUsuarioExit = () => {
        if (!nomeUsuario){
            setErroUsusuario('Usuário deve ser selecionado!');
            setTemErroUsuario(true);
        }else{
            setErroUsusuario('');
            setTemErroUsuario(false);
        }
    };

    const renderUserSelector = () => {
        if (props.canSelectUser){
            return(
                <Autocomplete
                    disabled={!props.canChangeUser}
                    noOptionsText={'Buscar usuário por nome...'}
                    options={nomesUsuarios}
                    value={nomeUsuario}
                    onChange={(e, value) => {
                        handleSelectUsuario(value);
                    }}
                    onBlur={handleUsuarioExit}
                    renderInput={(params) => (
                        <TextField {...params} 
                            error={temErroUsuario}
                            helperText={erroUsuario}
                            label="Usuário*" 
                            variant="outlined" 
                            className="formControl"
                            onChange={handleBuscaUsuario}
                        />
                    )}
                />
            );
        }
    }

    return (
        <form className="formMovimento">
            <TextField 
                disabled={numRegDisabled}
                error={temErroNumReg}
                helperText={erroNumReg}
                type="number"
                label="Nº registro*" 
                variant="outlined" 
                className="formControl"  
                value={numRegistro}
                onChange={handleChangeNumReg}
                onBlur={handleNumRegExit}
                />
            <TextField 
                label="Info" 
                variant="outlined" 
                className="formControl"
                disabled={true}  
                value={info}
                />                
            {renderUserSelector()} 
            <div className="formButtons">
                <Button 
                    disabled={temErroNumReg || temErroUsuario}
                    onClick={()=>{
                        if (exemplar && usuario){
                            if (props.verificaReserva){
                                ReservaService.verificaReserva(exemplar, usuario).then((resp:boolean)=>{
                                    if (resp){
                                        props.onSave(exemplar, usuario);
                                    }else{
                                        setErroNumReg('Exemplar reservado para outro usuário!');
                                        setTemErroNumReg(true);
                                    }
                                });
                            }else{
                                if (exemplar.situacao === 'Disponível' || exemplar.situacao === 'Emprestado'){
                                    props.onSave(exemplar, usuario);
                                }else{
                                    setErroNumReg('Exemplar indisponível!');
                                    setTemErroNumReg(true);
                                }
                            }
                        }
                    }} 
                    variant="contained">
                    <DoneIcon/>
                    Salvar
                </Button>
                <Button onClick={props.onClose} variant="contained">
                    <CloseIcon />
                    Cancelar
                </Button>                
            </div>
        </form>
    );
}

FormMovimento.propTypes = {
    numReg: PropTypes.number,
    idUsuario: PropTypes.number,
    canSelectUser: PropTypes.bool,
    canChangeNumReg: PropTypes.bool,
    canChangeUser: PropTypes.bool,
    verificaReserva: PropTypes.bool,
    onSave: PropTypes.func.isRequired,
    onClose: PropTypes.func.isRequired,
};

FormMovimento.defaultProps = {
    canSelectUser: true,
    canChangeNumReg: true,
    canChangeUser: true,
    verificaReserva: false
}

export default FormMovimento;