import { Dialog, DialogActions, DialogContent,DialogContentText, DialogTitle } from '@material-ui/core';
import PropTypes from 'prop-types';

import FormMovimento from '../formMovimento';

const DialogReserva = ( props : any ) => {

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
                    canChangeUser={true}
                    verificaReserva={false}
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

DialogReserva.propTypes = {
    title: PropTypes.string.isRequired,
    message: PropTypes.string.isRequired,
    numReg: PropTypes.number,  
    canChangeNumReg: PropTypes.bool,
    onSave: PropTypes.func.isRequired,
    onClose: PropTypes.func.isRequired,
    open: PropTypes.bool
};

DialogReserva.defaultProps = {
    canSelectUser: true,
    canChangeNumReg: true,
    verificaReserva: false
}

export default DialogReserva;