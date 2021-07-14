import { BrowserRouter, Route, Switch } from 'react-router-dom';

import HomePage from './pages/Home';
import LoginPage from './pages/Login';
import LivrosPage from './pages/Livros';
import LivroPage from './pages/Livro';
import LivroForm from './pages/LivroForm';
import ExemplaresPage from './pages/Exemplares';
import AssuntosPage from './pages/Assuntos';
import AutoresPage from './pages/Autores';
import EditorasPage from './pages/Editoras';
import SecoesPage from './pages/Secoes';
import CategoriasPage from './pages/Categorias';
import OrigensPage from './pages/Origens';
import UsuariosPage from './pages/Usuarios';
import EmprestimosPage from './pages/Emprestimos'
import ReservasPage from './pages/Reservas';
import ErrorsPage from './pages/Errors';
import PendenciasPage from './pages/Pendencias';

export default function Routes(){
    
    return(
        <BrowserRouter>
            <Switch>
                <Route path="/" exact component={HomePage} />
                <Route path="/login" component={LoginPage} />
                <Route path="/livros" exact component={LivrosPage} />
                <Route path="/livros/show" component={LivroPage}/>
                <Route path="/livros/insert" component={LivroForm}/>
                <Route path="/livros/edit" component={LivroForm}/>
                <Route path="/exemplares" component={ExemplaresPage}/>
                <Route path="/emprestimos" component={EmprestimosPage}/>
                <Route path="/reservas" component={ReservasPage}/>
                <Route path="/pendencias" component={PendenciasPage}/>
                <Route path="/assuntos" component={AssuntosPage}/>
                <Route path="/autores" component={AutoresPage}/>
                <Route path="/editoras" component={EditorasPage}/>
                <Route path="/secoes" component={SecoesPage}/>
                <Route path="/categorias" component={CategoriasPage}/>
                <Route path="/origens" component={OrigensPage}/>
                <Route path="/usuarios" component={UsuariosPage}/>
                <Route path="/errors" component={ErrorsPage} />               
            </Switch>
        </BrowserRouter>
    );
}