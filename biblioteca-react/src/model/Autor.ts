function _firstUpperWord(text : string):string{
    return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
}

export class Autor{

    constructor(
        public id?: number | null,
        public nome?: string,
        public sobrenome?: string,
        public info?: string | null
    ){}

    public static createAutorIfValid(nomeCompleto : string):Autor|null{
        const array = nomeCompleto.split(',');
        if (array.length === 2){
            const sn = array[0];
            const sna = sn.split(' ');
            let sobrenome = '';
            sna.forEach(word=>{
                if (sobrenome.length > 0){
                    sobrenome += ' ';
                }
                if (word.length > 2){
                    sobrenome += _firstUpperWord(word);
                }else{
                    sobrenome += word.toLowerCase();
                }
            });
            //const sobrenome = sn.charAt(0).toUpperCase() + sn.slice(1).toLowerCase();
            return new Autor(null, array[1].trim(), sobrenome, null);
        }
        return null;
    }

    public static createAutor(nomeCompleto : string):Autor{
        const array = nomeCompleto.split(',');
        const sn = array[0];
        const sna = sn.split(' ');
        let sobrenome = '';
        sna.forEach(word=>{
            if (sobrenome.length > 0){
                sobrenome += ' ';
            }
            if (word.length > 2){
                sobrenome += _firstUpperWord(word);
            }else{
                sobrenome += word.toLowerCase();
            }
        });
        return new Autor(null, array[1].trim(), sobrenome, null);
    }


    public static validateAutor(nomeCompleto : string):boolean{
        return nomeCompleto.split(',').length === 2;
    }


    public static createFromData(data : any): Autor{
        return new Autor(data.id, data.nome, data.sobrenome, data.info);
    }
}