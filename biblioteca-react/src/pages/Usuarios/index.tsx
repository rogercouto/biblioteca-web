import { Fragment, useState, useEffect } from 'react';
import Cookies from 'js-cookie';

import { TextField, Button, FormControlLabel, Switch, Paper } from '@material-ui/core';
import Pagination from '@material-ui/lab/Pagination';

import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline';
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import SaveIcon from '@material-ui/icons/Save';
import CancelIcon from '@material-ui/icons/Cancel';

import { BreadcrumbsMaker } from '../../components/breadcrumbs'
import { Usuario } from '../../model';
import { UsuarioService } from '../../services';

import './style.css';

const UsuariosPage = (props: any) => {
    const canEdit : boolean = Cookies.get('isGerente') === 'true';

    const [usuarios, setUsuarios] = useState(new Array<Usuario>());
    const [pagNum, setPagNum] = useState(1);
    const [totalPag, setTotalPag] = useState(1);

    const [edtId, setEdtId] = useState(0);

    const [nome, setNome] = useState('');
    const [temErroNome, setTemErroNome] = useState(false);
    const [erroNome, setErroNome] = useState('');

    const [email, setEmail] = useState('');
    const [temErroEmail, setTemErroEmail] = useState(false);
    const [erroEmail, setErroEmail] = useState('');

    const [numTel, setNumTel] = useState('');
    const [gerente, setGerente] = useState(false);
    const [senha, setSenha] = useState('');
    const [ativo, setAtivo] = useState(true);


    const bcMaker = new BreadcrumbsMaker('Usuarios');

    useEffect(()=>{
        UsuarioService.getList(pagNum).then((resp)=>{
            setUsuarios(resp.list);
            setTotalPag(resp.totalPag);
            console.log(resp.list);
        })
    },[pagNum])

    const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
        setEdtId(0);
        setPagNum(value);
        window.scrollTo({top: 0, behavior: 'smooth'});
    };

    const handleEdit = (usuario : Usuario) => {
        setTemErroNome(false);
        setErroNome('');
        if (usuario.id){
            setNome(usuario.nome || '');
            setEmail(usuario.email || '');
            setNumTel(usuario.numTel || '');
            setGerente(usuario.gerente || false);
            setSenha('');
            setAtivo(usuario.ativo || true);
            setEdtId(usuario.id);
        }
    };

    const handleInsert = () => {
        setTemErroNome(false);
        setErroNome('');
        setNome('');
        setEmail('');
        setNumTel('');
        setGerente(false);
        setSenha('');
        setAtivo(true);
        setEdtId(-1);
    };

    const validate = (): boolean => {
        let valid = true;
        if (nome.trim().length === 0){
            setTemErroNome(true);
            setErroNome('Nome não pode ficar em branco');
            valid = false;
        }
        if (email.trim().length === 0){
            setTemErroEmail(true);
            setErroEmail('E-mail não pode ficar em branco');
            valid = false;
        }
        if (!valid){
            return false;
        }
        setTemErroNome(false);
        setErroNome('');
        return true;
    };

    const indexOf = (usuario : Usuario) => {
        const ids = usuarios.map((a : Usuario)=>{return a.id;});
        return ids.indexOf(usuario.id);
    };

    const handleSave = async () => {
        if (validate()){
            let sUsuario = new Usuario();
            let insert = true;
            if (edtId > 0){
                sUsuario.id = edtId;
                insert = false;
            }
            sUsuario.nome = nome;
            sUsuario.email = email;
            if (numTel && numTel.trim().length > 0){
                sUsuario.numTel = numTel;
            }
            if (senha && senha.trim().length > 0){
                sUsuario.senha = senha;
            }
            sUsuario.gerente = gerente;
            sUsuario.ativo = ativo;
            const resp = await UsuarioService.save(sUsuario);
            if (resp.done){
                const savedUsuario = Usuario.createFromData(resp.data)
                if (insert){
                    const array = [...usuarios, savedUsuario];
                    setUsuarios(array);
                }else{
                    const array = [...usuarios];
                    const index = indexOf(savedUsuario);
                    array[index] = savedUsuario;
                    setUsuarios(array);
                }
                setEdtId(0);
            }else{
                alert(resp.errors);
            }
        }
    };

    const handleDelete = async (usuario : Usuario) => {
        if (window.confirm('Tem certeza que deseja remover o usuario?')) {
            const resp = await UsuarioService.delete(usuario);
            if (resp.done){
                const array = usuarios.filter((a: Usuario)=>{
                    return a.id !== usuario.id;
                });
                setUsuarios(array);
            }
        }
    };

    const handleCancel = () => {
        setEdtId(0);

    };

    const renderButtons = (usuario : Usuario) => {
        if (edtId === 0){
            return (
                <Fragment>
                    <Button variant="contained" onClick={(e)=>{
                        handleEdit(usuario);
                    }}>
                        <EditIcon  style={{color: 'blue'}}/>
                    </Button>
                    <Button variant="contained" onClick={(e)=>{
                        handleDelete(usuario)
                    }}>
                        <DeleteIcon  style={{color: 'red'}}/>
                    </Button>
                </Fragment>
            );
        }else if (edtId === usuario.id){
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

    const renderRow = (usuario : Usuario) => {
        if (usuario.id && usuario.id > 0 && edtId !== usuario.id){
            return(
                <tr key={usuario.id}>
                    <td>{usuario.nome}</td>
                    <td>{usuario.email}</td>
                    <td>{usuario.numTel}</td>
                    <td>{usuario.gerente ? 'Sim' : 'Não' }</td>
                    <td>*****</td>
                    <td>{usuario.ativo ? 'Sim' : 'Não' }</td>
                    <td>{renderButtons(usuario)}</td>
                </tr>
            );
        }else{
            return(
                <tr key={usuario.id}>
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
                    <td>
                        <TextField
                            error={temErroEmail}
                            helperText={erroEmail}
                            type="text"
                            value={email}
                            fullWidth
                            onChange={(e)=>{
                                setEmail(e.target.value);
                            }}
                        />
                    </td>
                    <td>
                        <TextField
                            type="text"
                            value={numTel}
                            fullWidth
                            onChange={(e)=>{
                                setNumTel(e.target.value);
                            }}
                        />
                    </td>
                    <td>
                        <FormControlLabel
                            control={
                            <Switch
                                checked={gerente}
                                onChange={(e)=>{setGerente(e.target.checked)}}
                                name="checkedGerente"
                                color="primary"
                            />
                            }
                            label={gerente?'Sim': 'Não'}
                        />
                    </td>
                    <td>
                        <TextField
                            type="password"
                            value={senha}
                            fullWidth
                            onChange={(e)=>{
                                setSenha(e.target.value);
                            }}
                        />
                    </td>
                    <td>
                        <FormControlLabel
                            control={
                            <Switch
                                checked={ativo}
                                onChange={(e)=>{setAtivo(e.target.checked)}}
                                name="checkedAtivo"
                                color="primary"
                            />
                            }
                            label={ativo?'Sim': 'Não'}
                        />
                    </td>
                    <td>{renderButtons(usuario)}</td>
                </tr>
            );
        }
    };

    const renderInsertRow = () => {
        if (edtId < 0){
            return renderRow(new Usuario(-1));
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
        return (<div className="usuariosContainer"><h1>Não autorizado!</h1></div>);
    }

    bcMaker.addHrefBreadcrumb('Home', '/');

    return (
        <Fragment>
            {bcMaker.render()}
            <Paper className="usuariosContainer">
                <h2>Usuarios</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Nome</th>
                            <th className="big">E-mail</th>
                            <th>Telefone</th>
                            <th>Gerente</th>
                            <th>Senha</th>
                            <th>Ativo</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        {usuarios.map((a:Usuario)=>{
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

export default UsuariosPage;