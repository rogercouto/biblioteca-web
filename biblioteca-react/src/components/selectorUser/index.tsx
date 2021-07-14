import { useState } from 'react';
import PropTypes from 'prop-types';

import { TextField } from '@material-ui/core';
import { Autocomplete } from '@material-ui/lab';

import { Usuario } from '../../model';
import { UsuarioService } from '../../services';

const SelectorUser = ( props : any ) => {

    const [usuarios, setUsuarios] = useState<Array<Usuario>>([]);
    const [nomesUsuarios, setNomesUsuarios] = useState<Array<string>>([]);
    const [nomeUsuario, setNomeUsuario] = useState<string | null>();

    const buscaUsuarios = (value : string) => {
        UsuarioService.find(value, props.incAdmin).then((usuarios : Array<Usuario>)=>{
            setUsuarios(usuarios);
            const nomes = usuarios.map((u : Usuario)=>{return u.nome || ''});
            setNomesUsuarios(nomes);
        });
    };

    const getUserId = (value : string | null) => {
        if (value){
            const index = nomesUsuarios.indexOf(value);
            if (index >= 0){
                return usuarios[index].id;
            }
        }
        return null;
    };

    return (
        <Autocomplete
            options={nomesUsuarios}
            value={nomeUsuario}
            onChange={(e, value)=>{
                setNomeUsuario(value); 
                const id = getUserId(value);
                props.onChange(id);
            }}
            noOptionsText="Pesquisar..."
            renderInput={(params) => (
                <TextField {...params} 
                    variant="outlined" 
                    label="Usuário" 
                    onChange={(e)=>buscaUsuarios(e.target.value)}
                />
            )}
        />
    );
}

SelectorUser.propTypes = {
    onChange: PropTypes.func,
    incAdmin: PropTypes.bool
};

SelectorUser.defaultProps = {
    incAdmin: true
}

export default SelectorUser;