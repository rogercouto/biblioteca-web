import { useState, useEffect } from 'react';
import { useHistory } from 'react-router-dom';

import { TextField, Button } from '@material-ui/core';
import { Autocomplete } from '@material-ui/lab';

import { BreadcrumbsMaker } from '../../components/breadcrumbs'
import { Assunto, Autor, Categoria, Editora, Livro } from '../../model';
import { LivroService } from '../../services';

import Loader from '../../components/Loader';

import './styles.css';

export default function LivroForm(props : any){
    
    const livro = Livro.createLivroFromState(props.location.state);

    const history = useHistory();
        
    const formType = livro && livro.id && livro.id > 0 ? 'Editar' : 'Inserir';

    const [nomesEditoras, setNomesEditoras] = useState(Array<string>());
    const [assuntos, setAssuntos] = useState(Array<Assunto>());
    const [descrsAssuntos, setDescrsAssuntos] = useState(Array<string>());
    const [descrsCategorias, setDescrsCategorias] = useState(Array<string>());
    const [nomesAutores, setNomesAutores] = useState(Array<string>());  

    const [titulo, setTitulo] = useState(livro.titulo || '');
    const [erroTitulo, setErroTitulo] = useState('');  
    const [temErroTitulo, setTemErroTitulo] = useState(false);
    const [autores, setAutor] = useState(livro.autores ? livro.getArrayNomesAutores() : []);
    const [txtAutor, setTxtAutor] = useState('');
    const [erroAutor, setErroAutor] = useState('');
    const [temErroAutor, setTemErroAutor] = useState(false);
    const [resumo, setResumo] = useState(livro.resumo);
    const [isbn, setIsbn] = useState(livro.isbn ? livro.isbn : '');
    const [cutter, setCutter] = useState(livro.cutter);
    const [editora, setEditora] = useState<string | null>(livro.editora?.nome || '');
    const [erroEditora, setErroEditora] = useState('');
    const [temErroEditora, setTemErroEditora] = useState(false);
    const [edicao, setEdicao] = useState(livro.edicao);
    const [volume, setVolume] = useState(livro.volume);
    const [assunto, setAssunto] = useState<string | null>(livro.assunto?.descricao || '');
    const [erroAssunto, setErroAssunto] = useState('');
    const [temErroAssunto, setTemErroAssunto] = useState(false);
    const [anoPublicacao, setAnoPublicacao] = useState(livro.anoPublicacao ? livro.anoPublicacao : '');
    const [numPaginas, setNumPaginas] = useState(livro.numPaginas ? livro.anoPublicacao : '');
    
    const [categorias, setCategoria] = useState(
        livro.categorias ?
        livro.categorias.map((c : any)=>{
            return c.descricao;
        })
        :
        new Array<Categoria>()
    );
    const [txtCategoria, setTxtCategoria] = useState('');
          
    const [isLoading, setIsLoading] = useState(false);

    const bcMaker = new BreadcrumbsMaker(formType);
    
    useEffect(()=>{
        LivroService.findAllAssuntos().then((tAssuntos : any)=>{
            setAssuntos(tAssuntos);
            const tDescrs : Array<string> = tAssuntos.map((a : any)=>{
                return a.descricao;
            });
            setDescrsAssuntos(tDescrs);
        })
    },[]);

    bcMaker.addHrefBreadcrumb('Home', '/');
    bcMaker.addHrefBreadcrumb('Livros', '/livros');
    
    function _handleBack(){
        history.push({
            pathname: '/livros/show',
            state: livro
        });
    }
    
    if (livro && livro.id && livro.id > 0){
        bcMaker.addFunctionBreadcrumb(livro.titulo || 'Livro', _handleBack);
    }

    function _validate() : boolean{
        let isValid = true;
        if (titulo.trim().length === 0){
            setErroTitulo('Titulo não pode ficar em branco!');
            setTemErroTitulo(true);
            isValid = false;
        }
        if (editora && editora.trim().length === 0){
            setErroEditora('Editora deve ser informada!')
            setTemErroEditora(true);
            isValid = false;
        }
        if (assunto == null || assunto.trim().length === 0){
            setErroAssunto('Assunto deve ser selecionado!');
            setTemErroAssunto(true);
            isValid = false;
        }
        if (autores.length > 0){
            autores.forEach((a:string)=>{
                if (!Autor.validateAutor(a)){
                    setErroAutor('Nome do autor deve seguir o seguinte formato: SOBRENOME, Nome!');
                    setTemErroAutor(true);
                    isValid = false;
                }
            });
        }else{
            setErroAutor('Ao menos um autor deve ser informado!');
            setTemErroAutor(true);
            isValid = false;
        }
        const tAssunto = _getAssunto(assunto);
        if (tAssunto == null){
            setErroAssunto('Erro na recuperação do assunto!');
            setTemErroAssunto(true);
            isValid = false;
        }
        return isValid;
    }

    function _handleTituloChange(e : any){
        const tmpTitulo = e.target.value;
        if (tmpTitulo.trim().length === 0){
            setErroTitulo('Titulo não pode ficar em branco!');
            setTemErroTitulo(true);
        }else{
            setErroTitulo('');
            setTemErroTitulo(false);
        }
        setTitulo(tmpTitulo);
    }

    function _handleTituloExit(){
        if (titulo.trim().length === 0){
            setErroTitulo('Titulo não pode ficar em branco!');
            setTemErroTitulo(true);
        }
    }

    function _handleAssuntoChange(value : string | null){
        if (value === null || value.trim().length === 0){
            setErroAssunto('Assunto deve ser selecionado!');
            setTemErroAssunto(true);
        }else{
            setErroAssunto('')
            setTemErroAssunto(false);
        }
        setAssunto(value);
    }

    function _handleAssuntoExit(){
        if (assunto == null || assunto.trim().length === 0){
            setErroAssunto('Assunto deve ser selecionado!');
            setTemErroAssunto(true);
        }
    }

    function _handleEditoraChange(value : string | null){
        if (value === null || value.trim().length === 0){
            setErroEditora('Editora deve ser informada!');
            setTemErroEditora(true);
        }else{
            setErroEditora('')
            setTemErroEditora(false);
        }
        setEditora(value);
    }

    function _handleEditoraExit(){
        if (editora === null || editora.trim().length === 0){
            setEditora('');
            setErroEditora('Editora deve ser informada!');
            setTemErroEditora(true);
        }
    }

    async function _handleBuscaAutor(value: string){
        if (value !== '' && value.length >= 3){
            const nomesAutores = await LivroService.findNomesAutores(value)
            setNomesAutores(nomesAutores);
        }else{
            setNomesAutores([]);
        }
    }

    async function _handleBuscaEditora(value : string){
        if (value !== '' && value.length >= 2){
            const nomesEditoras = await LivroService.findAllNomesEditoras(value);
            setNomesEditoras(nomesEditoras);
        }else{
            setNomesEditoras([]);
        }
    }

    async function _handleBuscaCategoria(value: string){
        if (value !== '' && value.length >= 2){
            const descrsCategorias = await LivroService.findAllDescrsCategorias(value)
            setDescrsCategorias(descrsCategorias);
        }else{
            setDescrsCategorias([]);
        }
    }

    function _getAssunto(descricao : string | null): Assunto | null{
        if (descricao == null){
            return null;
        }
        let assunto = null;
        assuntos.forEach((a)=>{
            if (a.descricao === descricao){
                assunto = a;
            }
        });
        return assunto;
    }

    function _validateAutor(){
        let tIsValid = true;
        let tNomesAutores = autores.concat([txtAutor]).filter((a : any)=>{
            return (a.trim().length > 0);
        });
        if (tNomesAutores.length > 0){
            tNomesAutores.forEach((a:string)=>{
                if (!Autor.validateAutor(a)){
                    setErroAutor('Nome(s) do(s) autor(es) deve seguir o seguinte formato: SOBRENOME, Nome!');
                    setTemErroAutor(true);
                    tIsValid = false;
                }
            });
        }else{
            setErroAutor('Ao menos um autor deve ser informado!');
            setTemErroAutor(true);
            tIsValid = false;
        }
        if (tIsValid){
            setErroAutor('');
            setTemErroAutor(false);
        }
    }

    async function _handleSubmit(e : any){
        e.preventDefault();
        if (txtAutor.trim().length > 0 && !autores.includes(txtAutor)){
            autores.push(txtAutor);
        }
        if (txtCategoria.trim().length > 0 && !categorias.includes(txtCategoria)){
            categorias.push(txtCategoria);
        }
        if (_validate()){
            livro.titulo = titulo;
            
            livro.autores = autores.filter((a : any)=>{
                if (Autor.validateAutor(a)){
                    return true;
                }else{
                    return false;
                }
            }).map((a : any)=>{
                return Autor.createAutor(a);
            });
            livro.resumo = resumo;
            livro.isbn = +isbn;
            livro.cutter = cutter;
            if (editora){
                livro.editora = new Editora(undefined, editora);
            }
            livro.edicao = edicao;
            livro.volume = volume;
            livro.numPaginas =  numPaginas ? +numPaginas : null;
            const tmpAssunto = _getAssunto(assunto);
            if (tmpAssunto !== null){
                livro.assunto = { id: tmpAssunto.id };
            }
            livro.anoPublicacao = +anoPublicacao;
            livro.autores = autores.map((a: any)=>{
                return Autor.createAutor(a);
            });
            livro.categorias = categorias.map((c : any)=>{
                return new Categoria(undefined, c);
            });
            //console.log(livro);
            if (livro.id === 0){
                setIsLoading(true);
                const newLivro = await LivroService.insertLivro(livro);
                setIsLoading(false);
                console.log(newLivro);
                history.push({
                    pathname: '/livros/show',
                    state: newLivro
                });
            }else{
                setIsLoading(true);
                const updatedLivro = await LivroService.updateLivro(livro);
                setIsLoading(false);
                console.log(updatedLivro);
                history.push({
                    pathname: '/livros/show',
                    state: updatedLivro
                });
            }
        }
    }
    
    return (
        <div className="formContainer">
            {bcMaker.render()}
            <h2>{formType} livro</h2>
            <hr />
            <form onSubmit={_handleSubmit}>
                <TextField 
                    error={temErroTitulo}
                    helperText={erroTitulo}
                    label="Título *" 
                    variant="outlined" 
                    className="formControl"
                    type="text"
                    value={titulo}
                    onChange={_handleTituloChange}
                    onBlur={_handleTituloExit}
                />
                <Autocomplete
                    multiple
                    freeSolo
                    options={nomesAutores}
                    value={autores || null}
                    onChange={(e, value)=>{
                        setAutor(value as string[]);
                        setTxtAutor('');
                    }}
                    onBlur={(e) => {
                        _validateAutor();
                    }}
                    renderInput={(params) => (
                        <TextField {...params} 
                            error={temErroAutor}
                            helperText={erroAutor}
                            variant="outlined" 
                            label="Autores *" 
                            className="formControl"
                            value={txtAutor}
                            onChange={(e)=>{
                                _handleBuscaAutor(e.target.value);
                                setTxtAutor(e.target.value);
                            }}
                        />
                    )}
                />
                <TextField 
                    label="Resumo" 
                    variant="outlined" 
                    className="formControl"
                    type="text"
                    value={resumo}
                    multiline
                    rows={3}
                    onChange={e=>{
                        setResumo(e.target.value);
                    }}
                />
                <div className="row">
                    <TextField 
                        label="ISBN" 
                        variant="outlined"  
                        className="formControl"
                        value={isbn}
                        onChange={e=>{
                            setIsbn(e.target.value);
                        }}
                    />
                    <TextField 
                        label="Cutter" 
                        variant="outlined"  
                        className="formControl"
                        value={cutter}
                        onChange={e=>{
                            setCutter(e.target.value);
                        }}
                    />
                </div>  
                <Autocomplete
                    freeSolo
                    options={nomesEditoras}
                    value={editora}
                    onChange={(e, value)=>{
                        _handleEditoraChange(value);
                    }}
                    onBlur={_handleEditoraExit}
                    renderInput={(params) => (
                        <TextField {...params} 
                            error={temErroEditora}
                            helperText={erroEditora}
                            label="Editora *" 
                            variant="outlined" 
                            className="formControl"
                            onChange={(e)=>{
                                _handleBuscaEditora(e.target.value);
                                _handleEditoraChange(e.target.value);
                            }}
                        />
                    )}
                /> 
                <div className="row">
                    <TextField 
                        label="Edição" 
                        variant="outlined"  
                        className="formControl"
                        value={edicao}
                        onChange={e=>{
                            setEdicao(e.target.value);
                        }}
                    />
                    <TextField 
                        label="Volume" 
                        variant="outlined"  
                        className="formControl"
                        value={volume}
                        onChange={e=>{
                            setVolume(e.target.value);
                        }}
                    />
                </div>            
                <Autocomplete
                    options={descrsAssuntos}
                    value={assunto || null}
                    onChange={(e, value)=>{
                        _handleAssuntoChange(value);
                    }}
                    //onFocus={_handleFocusOnAssuntos}
                    onBlur={_handleAssuntoExit}
                    renderInput={(params) => (
                        <TextField {...params} 
                            error={temErroAssunto}
                            helperText={erroAssunto}
                            label="Assunto *" 
                            variant="outlined" 
                            className="formControl"
                            onChange={(e)=>{
                                _handleAssuntoExit();
                            }}
                        />
                    )}
                />
                <div className="row">
                    <TextField 
                        label="Ano publicação" 
                        variant="outlined"  
                        className="formControl"
                        value={anoPublicacao}
                        onChange={e=>{
                            setAnoPublicacao(e.target.value);
                        }}
                    />
                    <TextField 
                        label="Nº Páginas" 
                        variant="outlined"  
                        className="formControl"
                        value={numPaginas}
                        onChange={e=>{
                            setNumPaginas(e.target.value);
                        }}
                    />
                </div> 
                <Autocomplete
                    multiple
                    freeSolo
                    options={descrsCategorias}
                    value={categorias}
                    onChange={(e, value)=>{
                        setCategoria(value as string[]);
                        setTxtCategoria('');
                    }}
                    renderInput={(params) => (
                        <TextField {...params} 
                            variant="outlined" 
                            label="Categorias" 
                            className="formControl"
                            placeholder="Categorias" 
                            value={txtCategoria}
                            onChange={(e)=>{
                                _handleBuscaCategoria(e.target.value);
                                setTxtCategoria(e.target.value);
                            }}
                        />
                    )}
                />
                <Button variant="contained" type="submit" color="primary">
                    Salvar
                </Button>
            </form>
            <Loader open={isLoading} />
        </div>
    );
}