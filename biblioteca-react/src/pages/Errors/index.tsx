
export default function ErrorsPage(props: any){ 
    const state = props.location.state;
    if (!state){
        return (<div style={{ margin: '2rem'}}><h1>Não autorizado!</h1></div>);
    }
    return(
        <div style={{ margin: '2rem'}}>
            <h1>{state.error}</h1>
        </div>
    );

}
