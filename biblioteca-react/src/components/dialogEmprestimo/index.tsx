import { Dialog, DialogActions, DialogContent,DialogContentText, DialogTitle } from '@material-ui/core';
import PropTypes from 'prop-types';

import FormMovimento from '../formMovimento';

const DialogEmprestimo = ( props : any ) => {

    return (
        <Dialog open={props.open} onClose={props.onClose} aria-labelledby="form-dialog-title">
            <DialogTitle id="form-dialog-title">{props.title}</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    {props.message}
                </DialogContentText>
                <FormMovimento
                    numReg={props.numReg} 
                    canSelectUser={props.canSelectUser}
                    verificaReserva={true}
                    canChangeNumReg={props.canChangeNumReg}
                    onSave={props.onSave}
                    onClose={props.onClose}
                />
            </DialogContent>
            <DialogActions>
            </DialogActions>
        </Dialog>
    );
};

DialogEmprestimo.propTypes = {
    title: PropTypes.string.isRequired,
    message: PropTypes.string.isRequired,
    numReg: PropTypes.number,
    canSelectUser: PropTypes.bool,
    canChangeNumReg: PropTypes.bool,
    onSave: PropTypes.func.isRequired,
    onClose: PropTypes.func.isRequired,
    open: PropTypes.bool
};

DialogEmprestimo.defaultProps = {
    canSelectUser: true,
    canChangeNumReg: true,
    verificaReserva: false
}

export default DialogEmprestimo;