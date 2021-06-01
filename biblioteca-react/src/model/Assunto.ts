export class Assunto{

    constructor(
        public id?: number,
        public descricao?: string,
        public cores?: string,
        public cdu?: string
    ){}

    public static createFromData(data : any) : Assunto{
        return new Assunto(data.id, data.descricao, data.cores, data.cdu);
    }

}