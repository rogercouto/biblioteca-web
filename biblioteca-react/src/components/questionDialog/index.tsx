
import { Button, Dialog, DialogActions, DialogContent,DialogContentText, DialogTitle } from '@material-ui/core';

const QuestionDialog = (props: any) =>{
    return(
        <div>
            <Dialog open={props.open} onClose={props.onClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">{props.title}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {props.message}
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                <Button onClick={props.onConfirm} variant="contained">
                    Sim
                </Button>
                <Button onClick={props.onClose} variant="contained">
                    Não
                </Button>
                </DialogActions>
            </Dialog>
        </div>
    );
}

export default QuestionDialog;