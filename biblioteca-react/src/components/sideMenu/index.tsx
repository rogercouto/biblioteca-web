import { Fragment, useState } from 'react';
import { createBrowserHistory } from 'history';

import clsx from 'clsx';
import { makeStyles, Theme, createStyles } from '@material-ui/core/styles';
import { List, Divider, Collapse, ListItem, ListItemIcon, ListItemText } from '@material-ui/core';

import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';

import HomeIcon from '@material-ui/icons/Home';
import MenuBookIcon from '@material-ui/icons/MenuBook';
import CollectionsBookmarkIcon from '@material-ui/icons/CollectionsBookmark';
import PersonPinIcon from '@material-ui/icons/PersonPin';
import GroupIcon from '@material-ui/icons/Group';
import BusinessIcon from '@material-ui/icons/Business';
import CategoryIcon from '@material-ui/icons/Category';
import GetAppIcon from '@material-ui/icons/GetApp';
import BookmarksIcon from '@material-ui/icons/Bookmarks';
import SettingsEthernetIcon from '@material-ui/icons/SettingsEthernet';
import CompareArrowsIcon from '@material-ui/icons/CompareArrows';
import AssignmentReturnIcon from '@material-ui/icons/AssignmentReturn';
import HistoryIcon from '@material-ui/icons/History';
import MonetizationOnIcon from '@material-ui/icons/MonetizationOn';
import Cookies from 'js-cookie';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      width: '100%',
      maxWidth: 360,
      backgroundColor: theme.palette.background.paper,
    },
    default: {},
    nested: {
      paddingLeft: theme.spacing(4),
    },
    selected: {
      backgroundColor: theme.palette.action.hover,
    },
  }),
);

const PrimaryListItems = ( classes : any, pathname : string = '' ) => {

  return (
    <div>
      <ListItem button className={clsx(classes.default, pathname === '/' && classes.selected)} component="a" href="/">
          <ListItemIcon>
            <HomeIcon />
          </ListItemIcon>
          <ListItemText primary="Início" />      
      </ListItem>
      <ListItem button 
        className={clsx(classes.default, (pathname === '/livros' || pathname === '/exemplares') && classes.selected)} 
        component="a" href="/livros">
        <ListItemIcon>
          <MenuBookIcon />
        </ListItemIcon>
        <ListItemText primary="Livros" />
      </ListItem>
    </div>
  );
};

const CollapseListMovimentos = ( classes : any, pathname : string = '' ) => {
  const paths = ['/emprestimos', '/reservas', '/pendencias'];
  const [open, setOpen] = useState(paths.includes(pathname));
  const handleClick = () => {
    setOpen(!open);
  };
  return(
    <List>
      <ListItem button onClick={handleClick}>
        <ListItemIcon>
          <CompareArrowsIcon />
        </ListItemIcon>
        <ListItemText primary="Movimentação" />
        {open ? <ExpandLess /> : <ExpandMore />}
      </ListItem>
      <Collapse in={open} timeout="auto" unmountOnExit>
        <List component="div" disablePadding>
          <ListItem button className={clsx(classes.nested, pathname === '/emprestimos' && classes.selected)} component="a" href="/emprestimos">
            <ListItemIcon>
              <AssignmentReturnIcon className="flipH"/>
            </ListItemIcon>
            <ListItemText primary="Empréstimos" />
          </ListItem>
          <ListItem button className={clsx(classes.nested, pathname === '/reservas' && classes.selected)} component="a" href="/reservas">
            <ListItemIcon>
              <HistoryIcon />
            </ListItemIcon>
            <ListItemText primary="Reservas" />
          </ListItem>
          <ListItem button className={clsx(classes.nested, pathname === '/pendencias' && classes.selected)} component="a" href="/pendencias">
            <ListItemIcon>
              <MonetizationOnIcon />
            </ListItemIcon>
            <ListItemText primary="Pendências" />
          </ListItem>
        </List>
      </Collapse>    
    </List>
  );
};

const CollapseListGerencia = ( classes : any, pathname : string = '') => {
  const paths = ['/assuntos', '/autores', '/categorias', '/editoras', '/secoes', '/origens', '/usuarios'];
  const [open, setOpen] = useState(paths.includes(pathname));
  const handleClick = () => {
    setOpen(!open);
  };
  //clsx(classes.nested, true && classes.selected)
  return(
    <List>
      <ListItem button onClick={handleClick}>
        <ListItemIcon>
          <SettingsEthernetIcon />
        </ListItemIcon>
        <ListItemText primary="Gerenciar" />
        {open ? <ExpandLess /> : <ExpandMore />}
      </ListItem>
      <Collapse in={open} timeout="auto" unmountOnExit>
        <List component="div" disablePadding>
          <ListItem button className={clsx(classes.nested, pathname === '/assuntos' && classes.selected)} component="a" href="/assuntos">
            <ListItemIcon>
              <CollectionsBookmarkIcon />
            </ListItemIcon>
            <ListItemText primary="Assuntos" />
          </ListItem>
          <ListItem button className={clsx(classes.nested, pathname === '/autores' && classes.selected)} component="a" href="/autores">
            <ListItemIcon>
              <PersonPinIcon />
            </ListItemIcon>
            <ListItemText primary="Autores" />
          </ListItem>
          <ListItem button className={clsx(classes.nested, pathname === '/editoras' && classes.selected)} component="a" href="/editoras">
            <ListItemIcon>
              <BusinessIcon />
            </ListItemIcon>
            <ListItemText primary="Editoras" />
          </ListItem>
          <ListItem button className={clsx(classes.nested, pathname === '/categorias' && classes.selected)} component="a" href="/categorias">
            <ListItemIcon>
              <CategoryIcon />
            </ListItemIcon>
            <ListItemText primary="Categorias" />
          </ListItem>
          <ListItem button className={clsx(classes.nested, pathname === '/secoes' && classes.selected)} component="a" href="/secoes">
            <ListItemIcon>
              <BookmarksIcon />
            </ListItemIcon>
            <ListItemText primary="Seções" />
          </ListItem>
          <ListItem button className={clsx(classes.nested, pathname === '/origens' && classes.selected)} component="a" href="/origens">
            <ListItemIcon>
              <GetAppIcon />
            </ListItemIcon>
            <ListItemText primary="Origens" />
          </ListItem>
          <ListItem button className={clsx(classes.nested, pathname === '/usuarios' && classes.selected)} component="a" href="/usuarios">
            <ListItemIcon>
              <GroupIcon />
            </ListItemIcon>
            <ListItemText primary="Usuários" />
          </ListItem>
        </List>
      </Collapse>    
    </List>
  );
};

const SideMenu = () => {

  const canEdit : boolean = Cookies.get('isGerente') === 'true';
  const isLogado : boolean = Cookies.get('username') !== undefined;

  const history = createBrowserHistory();

  const pathname = history.location.pathname;

  const classes = useStyles();
  return (
    <Fragment>
      <Divider />
      <List>{PrimaryListItems(classes, pathname)}</List>
      <Divider />
      {isLogado ? CollapseListMovimentos(classes, pathname) : <Fragment/>}
      <Divider />
      {canEdit? CollapseListGerencia(classes, pathname) : <Fragment/>}
    </Fragment>
  );
};

export default SideMenu;