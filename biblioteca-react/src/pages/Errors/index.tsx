
export default function ErrorsPage(props: any){ 
    const state = props.location.state;
    return(
        <div style={{ margin: '2rem'}}>
            <h1>{state.error}</h1>
        </div>
    );

}
