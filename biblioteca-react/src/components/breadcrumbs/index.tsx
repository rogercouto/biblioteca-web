import { Breadcrumbs, Button } from '@material-ui/core';

class Breadcrumb{
    constructor(
        public label: string,
        public href?: string,
        public onClick?: Function
    ){}
}

export class BreadcrumbsMaker{

    private _breadcrumbs : Array<Breadcrumb> = new Array<Breadcrumb>();

    constructor(
        public thisPage:string
    ){}

    public addHrefBreadcrumb(label: string, href : string){
        this._breadcrumbs.push(new Breadcrumb(label, href));
    }

    public addFunctionBreadcrumb(label: string, onClick : Function){
        const bc = new Breadcrumb(label);
        bc.onClick = onClick;
        this._breadcrumbs.push(bc);
    }

    private get breadcrumbs(){
        return this._breadcrumbs;
    }

    public render(){
        return(
            <div className="breadcrumbs">
                <Breadcrumbs aria-label="Breadcrumb" separator=">">
                    {this._breadcrumbs.map((bc : Breadcrumb)=>{
                        if (bc.href){
                            return(
                                <Button href={bc.href} key={bc.label}>
                                    {bc.label}
                                </Button>
                            );
                        }else{
                            return(
                                <Button key={bc.label} onClick={(e)=>{
                                    if (bc.onClick){
                                        bc.onClick();
                                    }
                                }}>
                                    {bc.label}
                                </Button>
                            );
                        }
                    })}
                    <span>{this.thisPage}</span>
                </Breadcrumbs>
            </div>
        );
    }

}