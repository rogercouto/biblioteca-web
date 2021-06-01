export class Usuario{
    
    constructor(
        public id?: number,
        public nome?: string,
        public email?: string,
        public senha?: string,
        public numTel?: string,
        public ativo?: boolean,
        public gerente?: boolean,
        public admin?: boolean
    ){}

    public static createFromData(data : any): Usuario{
        const u = new Usuario(
            data.id, data.nome, data.email, '', data.numTel, data.ativo, data.gerente, data.admin
        );
        return u;
    }

}