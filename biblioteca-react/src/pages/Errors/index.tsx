import { Paper } from '@material-ui/core';

export default function ErrorsPage(props: any){ 
    const state = props.location.state;
    if (!state){
        return (<Paper style={{ margin: '2rem'}}><h1>Não autorizado!</h1></Paper>);
    }
    return(
        <Paper style={{ margin: '2rem'}}>
            <h1>{state.error}</h1>
        </Paper>
    );

}
