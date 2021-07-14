import { useState, useEffect } from 'react';
import { createBrowserHistory } from 'history';
import clsx from 'clsx';
import { makeStyles } from '@material-ui/core/styles';
import { CssBaseline, Drawer, Box, AppBar, Toolbar, Typography,
IconButton, Tooltip, Container, Grid } from '@material-ui/core';

import MenuIcon from '@material-ui/icons/Menu';
import FaceIcon from '@material-ui/icons/Face';
import AccountCircleIcon from '@material-ui/icons/AccountCircle';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';

import SideMenu from '../../components/sideMenu';

import { useWindowDimensions } from '../../util/ScreenUtil';
import Cookies from 'js-cookie';

function Copyright() {
  return (
    <Typography variant="body2" color="textSecondary" align="center">
      {'Copyright © '}
        Polo Educacional Superior de Restinga Sêca - {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

const drawerWidth = 240;

const useStyles = makeStyles((theme) => ({
  root: {
    display: 'flex',
  },
  toolbar: {
    paddingRight: 24, // keep right padding when drawer closed
  },
  toolbarIcon: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'flex-end',
    padding: '0 8px',
    backgroundColor: '#6d8ccd',
    ...theme.mixins.toolbar,
  },
  appBar: {
    backgroundColor: '#6d8ccd',
    zIndex: theme.zIndex.drawer + 1,
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
  },
  appBarShift: {
    marginLeft: drawerWidth,
    width: '100%', //`calc(100% - ${drawerWidth}px)`
    transition: theme.transitions.create(['width', 'margin'], {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  menuButton: {
    marginRight: 36,
  },
  menuButtonHidden: {
    display: 'none',
  },
  title: {
    flexGrow: 1,
  },
  drawerPaper: {
    position: 'relative',
    whiteSpace: 'nowrap',
    width: drawerWidth,
    backgroundColor: '#EEE',
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.enteringScreen,
    }),
  },
  drawerPaperClose: {
    overflowX: 'hidden',
    transition: theme.transitions.create('width', {
      easing: theme.transitions.easing.sharp,
      duration: theme.transitions.duration.leavingScreen,
    }),
    width: theme.spacing(7),
    [theme.breakpoints.up('sm')]: {
      width: theme.spacing(9),
    },
  },
  appBarSpacer: theme.mixins.toolbar,
  content: {
    flexGrow: 1,
    height: '100vh',
    overflow: 'auto',
  },
  container: {
    paddingTop: theme.spacing(4),
    paddingBottom: theme.spacing(4),
  },
  smallContent: {
    height: 'calc(100vh - 150px)'
  },
  normalContent: {},
  paper: {
    padding: theme.spacing(2),
    display: 'flex',
    overflow: 'auto',
    flexDirection: 'column',
  },
  logoImg: { height: '50px', marginRight: '0.5rem' },
  hidden: { display: 'none'},
  userIcon: { cursor: 'default' },
  userSpan: { fontSize: '1rem', marginLeft: '0.25rem', marginRight: '1rem'}
}));

const Template = (props : any) => {

  const history = createBrowserHistory();

  const [username, setUsername] = useState('');

  const { width } = useWindowDimensions();
  const classes = useStyles();
  const [mobileDrawerOpen, setMobileDrawerOpen] = useState(false);

  const handleDrawerOpen = () => {
    setMobileDrawerOpen(true);
  };

  const handleDrawerClose = () => {
    setMobileDrawerOpen(false);
  };

  const isDrawerMaxed = () : boolean => {
    return width > 768;
  }

  const isMobile = () : boolean => {
    return width < 600;
  }

  const renderFixedDrawer = ( classes : any, screenWidth : number ) => {
    if (screenWidth > 600 && (!history.location || history.location.pathname !== '/login')){
        const open = screenWidth > 768;
        return(
            <Drawer
                variant="permanent"
                classes={{
                    paper: clsx(classes.drawerPaper, !open && classes.drawerPaperClose),
                }}
                open={open}
            >
                <div className={classes.toolbarIcon}>
                </div>
                <SideMenu />
            </Drawer>
        );
    }
  }

  const renderMobileDrawer = ( classes : any, open : boolean,  handleDrawerClose : any) => {
    return (
      <Drawer
        anchor={'left'} 
        open={open}
        onClose={handleDrawerClose}
        classes={{
            paper: classes.drawerPaper,
        }}
      >
          <div className={classes.toolbarIcon}>
          </div>
          <SideMenu />
      </Drawer>
    );
  }

  const handleLogout = () => {
    Cookies.remove('username');
    Cookies.remove('token');
    Cookies.remove('isGerente');
    setUsername('');
  };

  const renderUserButton = () => {
    if (username && username !== ''){
      return (
        <div>
          <IconButton color="inherit" className={classes.userIcon}>
            <FaceIcon />
            <span className={clsx(classes.userSpan, isMobile() && classes.hidden)}>{username}</span>
          </IconButton>
          <Tooltip title="Sair">
            <IconButton color="inherit" component="a" href="/" onClick={handleLogout}>
              <ExitToAppIcon />
            </IconButton>
          </Tooltip>
        </div>
        );
    }else{
      return (
        <Tooltip title="Entrar">
          <IconButton color="inherit" component="a" href="/login">
              <AccountCircleIcon />
          </IconButton>
        </Tooltip>
      );
    } 
  };
  
  useEffect(()=>{
    const getCookies = async () => {
      const tUsername = await Cookies.get('username');
      setUsername(tUsername || '');
      //const tIsGerente = await Cookies.get('isGerente');
    };
    getCookies();
  },[]);
  
  return (
    <div className={classes.root}>
      <CssBaseline />
      <AppBar position="absolute" className={clsx(classes.appBar, isDrawerMaxed() && classes.appBarShift)}>
        <Toolbar className={classes.toolbar}>
          <IconButton
            edge="start"
            color="inherit"
            aria-label="open drawer"
            onClick={handleDrawerOpen}
            className={clsx(classes.menuButton, !isMobile() && classes.menuButtonHidden)}
          >
            <MenuIcon />
          </IconButton>
          <img src="/logo.png" alt="Logotipo" className={clsx(classes.logoImg, isMobile() && classes.hidden)}/>
          <Typography component="h1" variant="h6" color="inherit" noWrap className={classes.title}>
            Sistema de controle de biblioteca
          </Typography>
          {renderUserButton()}
        </Toolbar>
      </AppBar>
      {renderFixedDrawer(classes, width)}
      {renderMobileDrawer(classes, mobileDrawerOpen, handleDrawerClose)}
      <main className={classes.content}>
        <div className={classes.appBarSpacer} />
        <Container maxWidth="lg" className={classes.container}>
          <Grid container spacing={3} className={classes.normalContent}>
            <Grid item xs={12}>
                {props.children}
            </Grid>
          </Grid>
          <Box pt={4}>
            <Copyright />
          </Box>
        </Container>
      </main>
    </div>
  );
};

export default Template;