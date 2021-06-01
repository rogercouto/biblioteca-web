import Header from '../header';
import Footer from '../footer';

import './styles.css';

export default function Template( props ){
    return(
        <div className="appContainer">
            <Header />
            <main className="mainContainer">
                {props.children}
            </main>
            <footer>
                <Footer />
            </footer>
        </div>
    );
}